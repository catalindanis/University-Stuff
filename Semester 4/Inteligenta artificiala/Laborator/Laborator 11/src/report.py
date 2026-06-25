from __future__ import annotations

import csv
import os
from typing import Dict, Iterable, Optional

import matplotlib.pyplot as plt
import networkx as nx


def _build_index_to_original_id(original_id_map: Optional[Dict]) -> Dict[int, object]:
    if not original_id_map:
        return {}
    return {index: original_id for original_id, index in original_id_map.items()}


def export_partition_csv(
    output_path: str,
    labels: Iterable[int],
    original_id_map: Optional[Dict] = None,
) -> None:
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    index_to_original_id = _build_index_to_original_id(original_id_map)

    with open(output_path, "w", newline="", encoding="utf-8") as fh:
        writer = csv.writer(fh)
        writer.writerow(["node_index", "original_id", "community"])
        for node_index, community in enumerate(labels):
            writer.writerow([node_index, index_to_original_id.get(node_index, ""), community])


def export_graph_image(
    graph: nx.Graph,
    labels: Iterable[int],
    output_path: str,
    title: Optional[str] = None,
    seed: int = 42,
    original_id_map: Optional[Dict] = None,
) -> None:
    os.makedirs(os.path.dirname(output_path), exist_ok=True)

    labels_list = list(labels)
    if graph.number_of_nodes() == 0:
        fig, ax = plt.subplots(figsize=(8, 6))
        ax.set_axis_off()
        if title:
            ax.set_title(title)
        fig.savefig(output_path, dpi=220, bbox_inches="tight", facecolor="white")
        plt.close(fig)
        return

    # Use a stable layout and color nodes by community.
    layout_seed = seed
    if graph.number_of_nodes() > 200:
        layout_seed = seed + 1
    positions = nx.spring_layout(graph, seed=layout_seed, k=1.0 / max(1, graph.number_of_nodes() ** 0.5))

    unique_labels = sorted(set(labels_list))
    label_to_color = {label: index for index, label in enumerate(unique_labels)}
    node_colors = [label_to_color.get(label, 0) for label in labels_list]

    node_count = graph.number_of_nodes()
    fig_size = max(8.0, min(18.0, node_count / 3.5))
    fig, ax = plt.subplots(figsize=(fig_size, fig_size))
    ax.set_axis_off()
    if title:
        ax.set_title(title, fontsize=14, pad=12)

    nx.draw_networkx_edges(
        graph,
        positions,
        ax=ax,
        width=0.8 if node_count < 60 else 0.5,
        alpha=0.35,
        edge_color="#8a8a8a",
    )
    nx.draw_networkx_nodes(
        graph,
        positions,
        ax=ax,
        node_color=node_colors,
        cmap=plt.cm.tab20,
        node_size=max(180, min(900, 5200 // max(1, node_count))),
        linewidths=0.8,
        edgecolors="#1f1f1f",
    )

    if original_id_map:
        index_to_original = _build_index_to_original_id(original_id_map)
        node_labels = {node: str(index_to_original.get(node, node)) for node in graph.nodes()}
    else:
        node_labels = {node: str(node) for node in graph.nodes()}

    font_size = 8 if node_count <= 25 else 6 if node_count <= 60 else 4
    nx.draw_networkx_labels(
        graph,
        positions,
        labels=node_labels,
        font_size=font_size,
        font_color="#111111",
        bbox=dict(boxstyle="round,pad=0.18", facecolor="white", edgecolor="none", alpha=0.85),
        ax=ax,
    )

    fig.tight_layout()
    fig.savefig(output_path, dpi=220, bbox_inches="tight", facecolor="white")
    plt.close(fig)
