from __future__ import annotations

from dataclasses import dataclass
from typing import List, Sequence

import networkx as nx

from src import eval as eval_mod

@dataclass
class BaselineResult:
    communities: List[List[int]]
    labels: List[int]
    modularity: float
    score: float
    score_name: str
    n_communities: int
    method: str = "girvan_newman"

def _partition_from_generator_step(step) -> List[List[int]]:
    return [sorted(list(community)) for community in step]

def girvan_newman_best_partition(graph: nx.Graph, max_levels: int | None = None) -> BaselineResult:
    if graph.number_of_nodes() == 0:
        return BaselineResult([], [], 0.0, 0.0, "modularity", 0)

    best_communities = [sorted(list(graph.nodes()))]
    best_modularity = eval_mod.modularity_score(graph, best_communities)
    levels_evaluated = 0

    generator = nx.algorithms.community.girvan_newman(graph)
    for step in generator:
        communities = _partition_from_generator_step(step)
        score = eval_mod.modularity_score(graph, communities)
        levels_evaluated += 1
        if score > best_modularity:
            best_modularity = score
            best_communities = communities
        if max_levels is not None and levels_evaluated >= max_levels:
            break

    labels = eval_mod.communities_to_labels(best_communities, graph.number_of_nodes())
    return BaselineResult(
        communities=best_communities,
        labels=labels,
        modularity=best_modularity,
        score=best_modularity,
        score_name="modularity",
        n_communities=len(best_communities),
    )