package com.llp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelTopologicalSort {
    @SuppressWarnings("unchecked")
	public List<Integer> parallelTopologicalSort(int numNodes, List<List<Integer>> adjacencyList) {
        int[] inDegrees = new int[numNodes];
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> topologicalOrder = new ArrayList<>();

        for (List<Integer> neighbors : adjacencyList) {
            for (int neighbor : neighbors) {
                inDegrees[neighbor]++;
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < numNodes; i++) {
            if (inDegrees[i] == 0) {
                final int node = i;
                futures.add((Future<Integer>) executorService.submit(() -> {
                    queue.offer(node);
//                    return node;
                }));
            }
        }

        try {
            for (Future<Integer> future : futures) {
                future.get();
//                System.out.println(future.get());
            }

            futures.clear();

            while (!queue.isEmpty()) {
                int node = queue.poll();
                topologicalOrder.add(node);

                for (int neighbor : adjacencyList.get(node)) {
                    final int n = neighbor;
                    futures.add((Future<Integer>) executorService.submit(() -> {
                        if (--inDegrees[n] == 0) {
                            queue.offer(n);
                        }
//                        return n;
                    }));
                }

                for (Future<Integer> future : futures) {
                    future.get();
//                    System.out.println(future.get());
                }
                futures.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        if (topologicalOrder.size() < numNodes) {
            throw new IllegalArgumentException("The graph contains a cycle.");
        }

        return topologicalOrder;
    }

    public void topoSort(int nodes, List<List<Integer>> adjacencyList) {

    	
        // Record the start time
//        long startTime = System.nanoTime();
        int numNodes = nodes;


        List<Integer> result = parallelTopologicalSort(numNodes, adjacencyList);
        System.out.println("Topological Sort: " + result);
        
//        // Record the end time
//        long endTime = System.nanoTime();
//        // Calculate the elapsed time in milliseconds
//        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
//
//        System.out.println("Code execution time: " + elapsedTime + " milliseconds");
    }
}
