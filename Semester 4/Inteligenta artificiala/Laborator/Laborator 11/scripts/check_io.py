"""Small script to validate IO on the provided real-networks datasets.

Usage:
  python scripts/check_io.py --base <path-to-real-networks/real>

It will load each network, its `real.dat` and `classLabel*.txt` (if present),
and print basic statistics and consistency checks.
"""
import argparse
import os
import sys
from pprint import pprint

project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
sys.path.insert(0, project_root)

from src import io as io_mod
import networkx as nx


def check_dataset(folder: str):
    print(f"\nChecking: {folder}")
    # find gml file
    gml_file = None
    for f in os.listdir(folder):
        if f.endswith('.gml'):
            gml_file = os.path.join(folder, f)
            break
    if gml_file is None:
        print("  No GML file found; skipping")
        return

    G, original_map = io_mod.load_gml(gml_file)
    print(f"  G nodes: {G.number_of_nodes()}, edges: {G.number_of_edges()}")

    real_dat = os.path.join(folder, 'real.dat')
    if os.path.exists(real_dat):
        communities = io_mod.load_real_dat(real_dat)
        print(f"  real.dat communities: {len(communities)} (total members listed: {sum(len(c) for c in communities)})")
    else:
        communities = None
        print("  real.dat not present")

    # find class label file
    class_label = None
    for f in os.listdir(folder):
        if f.startswith('classLabel') and f.endswith('.txt'):
            class_label = os.path.join(folder, f)
            break
    if class_label and os.path.exists(class_label):
        labels = io_mod.load_class_labels(class_label, G.number_of_nodes())
        print(f"  classLabel entries: {sum(1 for v in labels if v!=-1)}")
    else:
        labels = None
        print("  classLabel file not present")

    checks = io_mod.validate_consistency(G, communities=communities, labels=labels)
    print("  Consistency checks:")
    pprint(checks)


def main():
    p = argparse.ArgumentParser()
    p.add_argument('--base', default='real-networks/real', help='Path to folder with datasets')
    args = p.parse_args()

    base = args.base
    if not os.path.exists(base):
        print(f"Base folder not found: {base}")
        return
    for sub in sorted(os.listdir(base)):
        subp = os.path.join(base, sub)
        if os.path.isdir(subp):
            check_dataset(subp)


if __name__ == '__main__':
    main()
