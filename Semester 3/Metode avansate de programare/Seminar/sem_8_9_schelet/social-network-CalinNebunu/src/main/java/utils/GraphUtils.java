package utils;

import java.util.*;
import java.util.function.Function;

public class GraphUtils {

    /**
     * O clasa de utilitate nu ar trebui instantiata.
     */
    private GraphUtils() {
    }

    /**
     * Exploreaza o componenta conexa a unui graf folosind BFS (Breadth-First Search).
     * Modifica 'visited' set-ul primit ca parametru, adaugand toate nodurile gasite.
     *
     * @param startNode    ID-ul nodului de start
     * @param visited      Set-ul de noduri deja vizitate (acesta va fi MODIFICAT)
     * @param getNeighbors O functie (ex: repository::findAllFriendsOf) care primeste
     * un ID de nod si returneaza un Set cu ID-urile vecinilor sai.
     */
    public static void bfs(Long startNode,
                           Set<Long> visited,
                           Function<Long, Set<Long>> getNeighbors) {

        // 1. Initializam Coada (Queue) pentru BFS
        Queue<Long> queue = new LinkedList<>();

        // 2. Adaugam nodul de start
        queue.add(startNode);
        visited.add(startNode); // Il marcam ca vizitat IMEDIAT

        // 3. Cat timp mai avem noduri de explorat in coada
        while (!queue.isEmpty()) {

            // 4. Scoatem nodul curent din coada
            Long currentNode = queue.poll();

            // 5. Obtinem toti vecinii (prietenii) nodului curent
            // Folosim functia 'getNeighbors' pe care am primit-o ca parametru
            // Aceasta este partea care decupleaza algoritmul de repository.
            Set<Long> neighbors = getNeighbors.apply(currentNode);

            // 6. Parcurgem toti vecinii
            for (Long neighborId : neighbors) {
                // Daca vecinul nu a fost deja vizitat
                if (!visited.contains(neighborId)) {
                    // Il marcam ca vizitat
                    visited.add(neighborId);
                    // Il adaugam in coada pentru a-i explora vecinii mai tarziu
                    queue.add(neighborId);
                }
            }
        }
    }

    /**
     * Calculeaza lungimea celui mai scurt drum (BFS) intre doua noduri.
     *
     * @param startNode    Nodul de start
     * @param endNode      Nodul tinta
     * @param getNeighbors Functia care returneaza vecinii unui nod
     * @return lungimea drumului (numarul de muchii), sau -1 daca nu exista drum
     */
    public static int getShortestPathLength(Long startNode,
                                            Long endNode,
                                            Function<Long, Set<Long>> getNeighbors) {

        // 1. Caz trivial: nodul de start este si nodul final
        if (startNode.equals(endNode)) {
            return 0;
        }

        // 2. Initializam structurile BFS
        Queue<Long> queue = new LinkedList<>();

        // Acest Map tine loc si de 'visited' SI de 'distanta'
        Map<Long, Integer> distance = new HashMap<>();

        // 3. Adaugam nodul de start
        queue.add(startNode);
        distance.put(startNode, 0); // Distanta de la start la start e 0

        while (!queue.isEmpty()) {
            Long currentNode = queue.poll();

            // 4. Parcurgem toti vecinii nodului curent
            for (Long neighbor : getNeighbors.apply(currentNode)) {

                // Daca vecinul nu a fost vizitat (nu e in map)
                if (!distance.containsKey(neighbor)) {

                    // 5. Il marcam ca vizitat si ii setam distanta
                    int newDistance = distance.get(currentNode) + 1;
                    distance.put(neighbor, newDistance);
                    queue.add(neighbor);

                    // 6. Am gasit tinta? Daca da, am terminat.
                    if (neighbor.equals(endNode)) {
                        return newDistance;
                    }
                }
            }
        }

        // 7. Daca am terminat coada si nu am gasit endNode, nu exista drum
        return -1;
    }
}