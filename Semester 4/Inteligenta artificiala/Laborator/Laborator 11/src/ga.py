from __future__ import annotations

import random
import time
from dataclasses import dataclass
from typing import List, Optional, Union, Callable

import networkx as nx

from src import eval as eval_mod
from src import baseline as baseline_mod


def _compact_labels(labels: List[int]) -> List[int]:
    mapping = {}
    next_id = 0
    out = []
    for v in labels:
        if v not in mapping:
            mapping[v] = next_id
            next_id += 1
        out.append(mapping[v])
    return out


def _random_individual(n_nodes: int, max_communities: int = 6) -> List[int]:
    k = random.randint(2, min(max_communities, n_nodes))
    return [random.randrange(k) for _ in range(n_nodes)]


def _tournament_selection(population, fitnesses, k: int = 3):
    idxs = random.sample(range(len(population)), k)
    best = max(idxs, key=lambda i: fitnesses[i])
    return population[best]


def _uniform_crossover(a: List[int], b: List[int], crossover_rate: float) -> List[int]:
    if random.random() > crossover_rate:
        return a.copy()
    n = len(a)
    child = [a[i] if random.random() < 0.5 else b[i] for i in range(n)]
    return child


def _mutation(graph: nx.Graph, labels: List[int], mutation_rate: float) -> None:
    n = len(labels)
    for i in range(n):
        if random.random() < mutation_rate:
            neigh = list(graph.neighbors(i))
            if neigh and random.random() < 0.7:
                labels[i] = labels[random.choice(neigh)]
            else:
                labels[i] = random.choice(labels)

def run_ga(
    graph: nx.Graph,
    pop_size: int = 50,
    generations: int = 100,
    crossover_rate: float = 0.8,
    mutation_rate: float = 0.02,
    tournament_k: int = 3,
    elitism: int = 2,
    seed: Optional[int] = None,
    init_with_baseline: bool = True,
    fitness: Union[str, Callable[[nx.Graph, List[int]], float]] = "modularity",
) -> baseline_mod.BaselineResult:
    if seed is not None:
        random.seed(seed)

    if callable(fitness):
        fitness_func = fitness
        score_name = "custom"
    else:
        if fitness == "coverage":
            fitness_func = eval_mod.labels_coverage
            score_name = "coverage"
        elif fitness == "conductance":
            fitness_func = eval_mod.labels_conductance
            score_name = "conductance"
        else:
            fitness_func = eval_mod.labels_modularity
            score_name = "modularity"

    n_nodes = graph.number_of_nodes()

    population: List[List[int]] = []
    if init_with_baseline:
        base = baseline_mod.girvan_newman_best_partition(graph, max_levels=10)
        population.append(base.labels)
    while len(population) < pop_size:
        population.append(_random_individual(n_nodes))

    fitnesses = [fitness_func(graph, ind) for ind in population]

    best_idx = max(range(len(population)), key=lambda i: fitnesses[i])
    best = population[best_idx].copy()
    best_score = fitnesses[best_idx]

    start = time.time()
    for gen in range(generations):
        new_pop = []

        elites = sorted(range(len(population)), key=lambda i: fitnesses[i], reverse=True)[:elitism]
        for i in elites:
            new_pop.append(population[i].copy())

        while len(new_pop) < pop_size:
            parent_a = _tournament_selection(population, fitnesses, k=tournament_k)
            parent_b = _tournament_selection(population, fitnesses, k=tournament_k)
            child = _uniform_crossover(parent_a, parent_b, crossover_rate)
            _mutation(graph, child, mutation_rate)
            child = _compact_labels(child)
            new_pop.append(child)

        population = new_pop
        fitnesses = [fitness_func(graph, ind) for ind in population]
        
        current_best_idx = max(range(len(population)), key=lambda i: fitnesses[i])
        if fitnesses[current_best_idx] > best_score:
            best_score = fitnesses[current_best_idx]
            best = population[current_best_idx].copy()

    elapsed = time.time() - start

    communities = eval_mod.labels_to_communities(best)
    result = baseline_mod.BaselineResult(
        communities=communities,
        labels=best,
        modularity=eval_mod.labels_modularity(graph, best),
        score=best_score,
        score_name=score_name,
        n_communities=len(communities),
        method="ga",
    )
    return result