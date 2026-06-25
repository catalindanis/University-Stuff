# Lab11 — Community Detection with Genetic Algorithms

This workspace contains an incremental implementation for community detection using a Genetic Algorithm (GA) and baseline methods.

Start by validating I/O and dataset consistency:

```bash
python scripts/check_io.py --base "real-networks/real"
```

Run the Girvan-Newman baseline on one dataset:

```bash
python src/main.py --dataset "real-networks/real/karate"
```

Add `--show-partition` if you want to print the full node-to-community mapping.

Run the baseline over all four lab datasets:

```bash
python src/main.py --base "real-networks/real"
```

Export CSV results for every dataset into `results/`:

```bash
python src/main.py --base "real-networks/real" --output-dir results
```

Each CSV contains `node_index`, `original_id`, and `community`.

The project structure:
- `src/io.py` — I/O helpers
- `src/eval.py` — partition conversion and modularity scoring
- `src/baseline.py` — Girvan-Newman baseline
- `src/report.py` — CSV export helpers
- `scripts/check_io.py` — dataset validation script
- `src/main.py` — CLI entrypoint for loading and baseline runs
