package com.llp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

class EdgePool {
    int src, dest, weight;

    public EdgePool(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}

class ComponentPool {
    int representative;
    List<EdgePool> minEdges;

    public ComponentPool(int representative) {
        this.representative = representative;
        this.minEdges = new ArrayList<>();
    }
}

public class ParallelBoruvkaMSTPool {
    public static List<EdgePool> parallelBoruvkaMST(List<EdgePool> edges, int numNodes) {
    	ComponentPool[] components = new ComponentPool[numNodes];
        List<EdgePool> minSpanningTree = new ArrayList<>();
        ForkJoinPool pool = new ForkJoinPool();

        for (int i = 0; i < numNodes; i++) {
            components[i] = new ComponentPool(i);
        }

        while (minSpanningTree.size() < numNodes - 1) {
            // Initialize the minimum-weight edges for each component in parallel
            List<CompletableFuture<Void>> tasks = new ArrayList<>();
            for (EdgePool edge : edges) {
                int component1 = findComponent(components, edge.src);
                int component2 = findComponent(components, edge.dest);

                if (component1 != component2) {
                    CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                        if (components[component1].minEdges.isEmpty() || edge.weight < components[component1].minEdges.get(0).weight) {
                            components[component1].minEdges.clear();
                            components[component1].minEdges.add(edge);
                        }
                        if (components[component2].minEdges.isEmpty() || edge.weight < components[component2].minEdges.get(0).weight) {
                            components[component2].minEdges.clear();
                            components[component2].minEdges.add(edge);
                        }
                    }, pool);
                    tasks.add(task);
                }
            }

            @SuppressWarnings("unchecked")
			CompletableFuture<Void>[] taskArray = tasks.toArray(new CompletableFuture[0]);

            CompletableFuture<Void> allOf = CompletableFuture.allOf(taskArray);
            try {
                allOf.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // Add the minimum-weight edges to the MST and merge components
            tasks.clear();
            for (int i = 0; i < numNodes; i++) {
                if (!components[i].minEdges.isEmpty()) {
                	EdgePool minEdge = components[i].minEdges.get(0);
                    int component1 = findComponent(components, minEdge.src);
                    int component2 = findComponent(components, minEdge.dest);

                    if (component1 != component2) {
                        CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
                            synchronized (minSpanningTree) {
                                minSpanningTree.add(minEdge);
                            }
                            components[component1].minEdges.clear();
                            components[component2].minEdges.clear();
                            components[component1].minEdges.add(minEdge);
                            components[component1].minEdges.addAll(components[component2].minEdges);
                            components[component2] = components[component1];
                        }, pool);
                        tasks.add(task);
                    }
                }
            }

            @SuppressWarnings("unchecked")
			CompletableFuture<Void>[] addToMSTTasks = tasks.toArray(new CompletableFuture[0]);

            CompletableFuture<Void> allOfAddToMST = CompletableFuture.allOf(addToMSTTasks);
            try {
                allOfAddToMST.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
        return minSpanningTree;
    }

    private static int findComponent(ComponentPool[] components, int node) {
        return components[node].representative;
    }

    public void boruvkaMST(int nodes, int[][] adjacencyMatrix) {
    	
        // Record the start time
//        long startTime = System.nanoTime();

    	

        int numNodes = nodes;
        
        List<EdgePool> edges = new ArrayList<>();
        
        
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
            	if (adjacencyMatrix[i][j] == 0) 
            			adjacencyMatrix[i][j] = Integer.MAX_VALUE;
            	
            	edges.add(new EdgePool(i, j, adjacencyMatrix[i][j]));
            }
            
        }

        List<EdgePool> minSpanningTree = parallelBoruvkaMST(edges, numNodes);

        System.out.println("Minimum Spanning Tree:");
        for (EdgePool edge : minSpanningTree) {
            System.out.println(edge.src + " - " + edge.dest + " Weight: " + edge.weight);
        }
//        // Record the end time
//        long endTime = System.nanoTime();
//        // Calculate the elapsed time in milliseconds
//        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
//
//        System.out.println("Code execution time: " + elapsedTime + " milliseconds");
    }
}
