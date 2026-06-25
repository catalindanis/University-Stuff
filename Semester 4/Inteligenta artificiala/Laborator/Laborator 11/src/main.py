from __future__ import annotations

import argparse
import os
import sys


if __package__ is None or __package__ == "":
    sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from src import baseline as baseline_mod
from src import io as io_mod
from src import ga as ga_mod
from src import report as report_mod


def _find_gml_file(dataset_folder: str) -> str:
    gmls = [f for f in os.listdir(dataset_folder) if f.endswith(".gml")]
    if not gmls:
        raise FileNotFoundError("No GML file found in dataset folder")
    return os.path.join(dataset_folder, gmls[0])


def _print_summary(dataset_name: str, graph, result: baseline_mod.BaselineResult) -> None:
    print(f"Dataset: {dataset_name}")
    print(f"  nodes: {graph.number_of_nodes()}")
    print(f"  edges: {graph.number_of_edges()}")
    print(f"  baseline method: {result.method}")
    print(f"  communities: {result.n_communities}")
    # print the chosen score (score_name) and also modularity for reference
    if hasattr(result, 'score_name') and hasattr(result, 'score'):
        print(f"  {result.score_name}: {result.score:.6f}")
    print(f"  modularity: {result.modularity:.6f}")


def _print_partition_details(result: baseline_mod.BaselineResult) -> None:
    print(f"  labels: {result.labels}")
    print(f"  communities(list form): {result.communities}")


def main() -> None:
    parser = argparse.ArgumentParser(description="Lab11: community detection")
    parser.add_argument("--dataset", help="Path to one dataset folder (contains the GML)")
    parser.add_argument("--base", help="Path to the folder that contains the 4 datasets")
    parser.add_argument("--max-levels", type=int, default=None, help="Optional cap for Girvan-Newman refinements")
    parser.add_argument("--show-partition", action="store_true", help="Print the full partition details")
    parser.add_argument("--method", choices=["baseline", "ga"], default="baseline", help="Which method to run (baseline or ga)")
    parser.add_argument("--output-dir", help="Directory to write outputs (CSV + metrics)")

    # GA specific args
    parser.add_argument("--pop-size", type=int, default=30)
    parser.add_argument("--generations", type=int, default=50)
    parser.add_argument("--crossover-rate", type=float, default=0.8)
    parser.add_argument("--mutation-rate", type=float, default=0.02)
    parser.add_argument("--tournament-k", type=int, default=3)
    parser.add_argument("--elitism", type=int, default=2)
    parser.add_argument("--seed", type=int, default=None)
    parser.add_argument("--fitness", choices=["modularity", "coverage", "conductance"], default="modularity", help="Fitness function for GA")
    args = parser.parse_args()

    if args.dataset:
        dataset_folders = [args.dataset]
    elif args.base:
        dataset_folders = [
            os.path.join(args.base, name)
            for name in sorted(os.listdir(args.base))
            if os.path.isdir(os.path.join(args.base, name))
        ]
    else:
        print("No dataset provided. Use --dataset <folder> or --base <folder>")
        return

    for dataset_folder in dataset_folders:
        gml_path = _find_gml_file(dataset_folder)
        graph, original_id_map = io_mod.load_gml(gml_path)
        if args.method == 'baseline':
            result = baseline_mod.girvan_newman_best_partition(graph, max_levels=args.max_levels)
        else:
            result = ga_mod.run_ga(
                graph,
                pop_size=args.pop_size,
                generations=args.generations,
                crossover_rate=args.crossover_rate,
                mutation_rate=args.mutation_rate,
                tournament_k=args.tournament_k,
                elitism=args.elitism,
                seed=args.seed,
                fitness=args.fitness
            )

        dataset_name = os.path.basename(dataset_folder.rstrip(os.sep))
        _print_summary(dataset_name, graph, result)
        if args.show_partition or graph.number_of_nodes() <= 40:
            _print_partition_details(result)

        if args.output_dir:
            outdir = os.path.join(args.output_dir, dataset_name)
            os.makedirs(outdir, exist_ok=True)
            csv_path = os.path.join(outdir, f"{dataset_name}_partition.csv")
            report_mod.export_partition_csv(csv_path, result.labels, None)
            metrics_path = os.path.join(outdir, f"{dataset_name}_metrics.txt")
            with open(metrics_path, 'w', encoding='utf-8') as mf:
                mf.write(f"dataset: {dataset_name}\n")
                mf.write(f"method: {result.method}\n")
                mf.write(f"nodes: {graph.number_of_nodes()}\n")
                mf.write(f"edges: {graph.number_of_edges()}\n")
                mf.write(f"communities: {result.n_communities}\n")
                mf.write(f"modularity: {result.modularity:.6f}\n")
                if hasattr(result, 'score_name') and hasattr(result, 'score'):
                    mf.write(f"{result.score_name}: {result.score:.6f}\n")

            image_path = os.path.join(outdir, f"{dataset_name}_graph.png")
            report_mod.export_graph_image(
                graph,
                result.labels,
                image_path,
                title=f"{dataset_name} - {result.method}",
                original_id_map=original_id_map,
            )
            print(f"  exported graph image: {image_path}")

        if args.output_dir:
            csv_path = os.path.join(args.output_dir, f"{dataset_name}_{result.method}.csv")
            report_mod.export_partition_csv(csv_path, result.labels, original_id_map=original_id_map)
            print(f"  exported csv: {csv_path}")


if __name__ == "__main__":
    main()
