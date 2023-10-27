import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			List<Integer> out = ptsort.execute(list, 2);
            for (Integer val : out) {
                    System.out.print(val + " ");
                }
                System.out.println();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	public static void main(String[] args) {
		
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
            
        Graph graph = new Graph(lines);
        line = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)) ) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                int node = Integer.parseInt(parts[0]);
                if (parts.length > 1) {
                    String[] neighborStrings = parts[1].split(",");
                    for (String neighborStr : neighborStrings) {
                        int neighbor = Integer.parseInt(neighborStr);
                        graph.addEdge(neighbor,node);
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        testTopologicalSort(lines-1, Graph.getAdj());

	}

}
