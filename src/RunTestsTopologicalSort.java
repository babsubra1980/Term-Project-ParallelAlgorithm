

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



class Graph {
    private int V; 
    private static List<List<Integer>> adj;

    public Graph(int v) {
        V = v;
        setAdj(new ArrayList<>(V));
        for (int i = 0; i < V; i++) {
            getAdj().add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        getAdj().get(u).add(v);
    }

	public static List<List<Integer>> getAdj() {
		return adj;
	}

	public static void setAdj(List<List<Integer>> adj) {
		Graph.adj = adj;
	}

    public List<List<Integer>> createPredecessorList() {
        List<List<Integer>> predecessorList = new ArrayList<>(V);

        for (int i = 0; i < V; i++) {
            predecessorList.add(new ArrayList<>());
        }

        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                predecessorList.get(v).add(u);
            }
        }

        return predecessorList;
    }

    
}



public class RunTestsTopologicalSort {
	
	
    public static void testTopologicalSort(int nodes, List<List<Integer>> list) {
        System.out.println("**");
        System.out.println("** ParallelTopologicalSort **");
    	TopologicalSort ptsort = new TopologicalSort();
    	System.out.println("**");

        try {
			List<Integer> out = ptsort.execute(nodes,list, 2);
            for (Integer val : out) {
                    System.out.print(val + " ");
                }
                System.out.println();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	public static void main(String[] args) {
		Set<Integer> uniqueVertices = new HashSet<>();
    	String fileName = "TopoSort.txt";
    
    	int lines = 0;
    	String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)) ) {
            
            while ((line = br.readLine()) != null) {
            	lines++;
            }
            br.close();
         }
            catch (IOException e) {
                e.printStackTrace();
            }
            
        Graph graph = new Graph(lines-1);
        line = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)) ) {
            while ((line = br.readLine()) != null) {
		if (line.contains(":")) {
                String[] parts = line.split(":");
                int node = Integer.parseInt(parts[0]);
                uniqueVertices.add(node);
                if (parts.length > 1) {
                    String[] neighborStrings = parts[1].split(",");
                    for (String neighborStr : neighborStrings) {
                        int neighbor = Integer.parseInt(neighborStr);
                        graph.addEdge(neighbor,node);
                        uniqueVertices.add(neighbor);
                        
//                        System.out.println("Neighbor: "+neighbor+" Node: "+node);
                    }
                }
		}

            }
//            System.out.println("Number of vertices: "+uniqueVertices.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        testTopologicalSort(uniqueVertices.size(), Graph.getAdj());


        
        
	}

}
