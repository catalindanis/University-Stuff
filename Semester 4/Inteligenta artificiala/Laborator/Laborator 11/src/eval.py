from __future__ import annotations

from typing import Iterable, List, Sequence

import networkx as nx


def communities_to_labels(communities: Sequence[Sequence[int]], n_nodes: int) -> List[int]:
    labels = [-1] * n_nodes
    for community_id, community in enumerate(communities):
        for node in community:
            labels[node] = community_id
    return labels


def labels_to_communities(labels: Sequence[int]) -> List[List[int]]:
    community_map = {}
    for node, community_id in enumerate(labels):
        if community_id < 0:
            continue
        community_map.setdefault(community_id, []).append(node)
    return [community_map[key] for key in sorted(community_map)]


def normalize_communities(communities: Iterable[Iterable[int]]) -> List[List[int]]:
    normalized = []
    for community in communities:
        items = sorted(set(community))
        if items:
            normalized.append(items)
    return normalized


def modularity_score(graph: nx.Graph, communities: Sequence[Sequence[int]]) -> float:
    clean_partition = normalize_communities(communities)
    if not clean_partition:
        return 0.0
    return nx.algorithms.community.modularity(graph, [set(c) for c in clean_partition])


def labels_modularity(graph: nx.Graph, labels: Sequence[int]) -> float:
    return modularity_score(graph, labels_to_communities(labels))


def coverage_score(graph: nx.Graph, communities: Sequence[Sequence[int]]) -> float:
    clean_partition = normalize_communities(communities)
    m_total = graph.number_of_edges()
    if m_total == 0 or not clean_partition:
        return 0.0

    internal = 0
    for c in clean_partition:
        if not c:
            continue
        internal += graph.subgraph(c).number_of_edges()
    return internal / m_total


def labels_coverage(graph: nx.Graph, labels: Sequence[int]) -> float:
    return coverage_score(graph, labels_to_communities(labels))


def _community_cut_and_vol(graph: nx.Graph, community_set: set, all_nodes_set: set) -> tuple[int, int, int]:
    cut = 0
    vol_s = 0
    for u in community_set:
        deg = graph.degree(u)
        vol_s += deg
        for v in graph.neighbors(u):
            if v not in community_set:
                cut += 1
    vol_rest = sum(graph.degree(v) for v in all_nodes_set if v not in community_set)
    return cut, vol_s, vol_rest


def conductance_score(graph: nx.Graph, communities: Sequence[Sequence[int]]) -> float:
    clean_partition = normalize_communities(communities)
    if not clean_partition:
        return 0.0

    all_nodes_set = set(graph.nodes())
    phis = []
    for c in clean_partition:
        S = set(c)
        if not S:
            continue
        cut, vol_s, vol_rest = _community_cut_and_vol(graph, S, all_nodes_set)
        denom = min(vol_s, vol_rest)
        if denom == 0:
            phi = 0.0
        else:
            phi = cut / denom
            if phi < 0:
                phi = 0.0
            elif phi > 1:
                phi = 1.0
        phis.append(phi)

    if not phis:
        return 0.0
    avg_phi = sum(phis) / len(phis)
    return 1.0 - avg_phi


def labels_conductance(graph: nx.Graph, labels: Sequence[int]) -> float:
    return conductance_score(graph, labels_to_communities(labels))


def coverage_score(graph: nx.Graph, communities: Sequence[Sequence[int]]) -> float:
    clean_partition = normalize_communities(communities)
    m_total = graph.number_of_edges()
    if m_total == 0 or not clean_partition:
        return 0.0

    internal = 0
    for c in clean_partition:
        if not c:
            continue
        internal += graph.subgraph(c).number_of_edges()
    return internal / m_total


def labels_coverage(graph: nx.Graph, labels: Sequence[int]) -> float:
    return coverage_score(graph, labels_to_communities(labels))


def _community_cut_and_vol(graph: nx.Graph, community_set: set, all_nodes_set: set) -> tuple[int, int, int]:
    cut = 0
    vol_s = 0
    for u in community_set:
        deg = graph.degree(u)
        vol_s += deg
        for v in graph.neighbors(u):
            if v not in community_set:
                cut += 1
    vol_rest = sum(graph.degree(v) for v in all_nodes_set if v not in community_set)
    return cut, vol_s, vol_rest


def conductance_score(graph: nx.Graph, communities: Sequence[Sequence[int]]) -> float:
    clean_partition = normalize_communities(communities)
    if not clean_partition:
        return 0.0

    all_nodes_set = set(graph.nodes())
    phis = []
    for c in clean_partition:
        S = set(c)
        if not S:
            continue
        cut, vol_s, vol_rest = _community_cut_and_vol(graph, S, all_nodes_set)
        denom = min(vol_s, vol_rest)
        if denom == 0:
            phi = 0.0
        else:
            phi = cut / denom
            if phi < 0:
                phi = 0.0
            elif phi > 1:
                phi = 1.0
        phis.append(phi)

    if not phis:
        return 0.0
    avg_phi = sum(phis) / len(phis)
    return 1.0 - avg_phi


def labels_conductance(graph: nx.Graph, labels: Sequence[int]) -> float:
    return conductance_score(graph, labels_to_communities(labels))