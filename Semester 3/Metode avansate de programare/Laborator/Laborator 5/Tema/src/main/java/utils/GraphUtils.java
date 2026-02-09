package utils;

import models.Friendship;

import java.util.*;

public class GraphUtils {

    public static int getNumberOfConnectedGraphs(Map<Long, Set<Long>> adjacencyList) {
        Set<Long> visited = new HashSet<>();
        int count = 0;

        for(Long node : adjacencyList.keySet())
            if(!visited.contains(node)) {
                count++;
                GraphUtils.dfs(node, adjacencyList, visited);
            }

        return count;
    }

    public static int getLengthOfLongestConnectedGraph(Map<Long, Set<Long>> adjacencyList) {
        Set<Long> visited = new HashSet<>();
        int max = 0;

        for(Long node : adjacencyList.keySet())
            if(!visited.contains(node)) {
                int length = dfsWithCount(node, adjacencyList, visited);
                if(length > max)
                    max = length;
            }

        return max;
    }

    public static Map<Long, Set<Long>> getAdjacencyList(Set<Long> nodes, List<Friendship> friendships) {
        Map<Long, Set<Long>> result = new HashMap<>();
        for(Long user : nodes)
            result.put(user, new HashSet<>());

        for(Friendship friendship : friendships) {
            result.get(friendship.getUsers()[0]).add(friendship.getUsers()[1]);
            result.get(friendship.getUsers()[1]).add(friendship.getUsers()[0]);
        }

        return result;
    }

    public static void dfs(long node, Map<Long, Set<Long>> adjacencyList, Set<Long> visited) {
        visited.add(node);
        for(Long neighbour : adjacencyList.get(node))
            if(!visited.contains(neighbour))
                dfs(neighbour, adjacencyList, visited);
    }

    public static int dfsWithCount(long node, Map<Long, Set<Long>> adjacencyList, Set<Long> visited) {
        visited.add(node);
        int length = 1;

        for(Long neighbour : adjacencyList.get(node))
            if(!visited.contains(neighbour))
                length += dfsWithCount(neighbour, adjacencyList, visited);

        return length;
    }
}
