

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


class Graph2 {
    private int V; 
    private static List<List<Integer>> adj;

    public Graph2(int v) {
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
		Graph2.adj = adj;
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



public class JUnitTopologicalSort {
	
    public static List<Integer> testTopologicalSort(int nodes, List<List<Integer>> list) throws InterruptedException {

    	TopologicalSort ptsort = new TopologicalSort();

    	
    	List<Integer> out = ptsort.execute(nodes,list, 2);
       
		return out;
    }

	public static int inputProcess(String testName, String file) {
        System.out.println("**");
        System.out.println("** ParallelTopologicalSort - JUnit Testing : "+ testName + " **");
    	System.out.println("**");
		Set<Integer> uniqueVertices = new HashSet<>();
    	String fileName = file;
    
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
            
        Graph2 graph = new Graph2(lines-1);
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
                    }
                }

		}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return uniqueVertices.size();
//        testTopologicalSort(lines, Graph.getAdj());


        
        
	}
	
	//Simple Linear Graph - JUnit Test
	@Test
	public void test1() throws InterruptedException {
		
		int l = inputProcess("Simple Linear Graph","TopoSort.txt");
		List<Integer> result = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		assertEquals(result,testTopologicalSort(l,Graph2.getAdj()));
		
	}
	
	//Multiple Independent Chains
	@Test
	public void test2() throws InterruptedException {
		
		int l = inputProcess("Multiple Independent Chains","TopoSort-1.txt");
		List<Integer> result = new ArrayList<>(Arrays.asList(0, 1, 1, 2, 3, 3, 4, 0, 1, 2, 3));
		assertEquals(result,testTopologicalSort(l,Graph2.getAdj()));
		
	}
	
	//DAG with Cycles
	 @Test(expected = IllegalArgumentException.class)
	public void test3() throws InterruptedException {
		
		int l = inputProcess("DAG with Cycles","TopoSort-2.txt");
		testTopologicalSort(l,Graph2.getAdj());
		
	}
	 
	//Tree Structure - JUnit Test
	@Test
	public void test4() throws InterruptedException {
			
			int l = inputProcess("Tree Structure","TopoSort-3.txt");
			List<Integer> result = new ArrayList<>(Arrays.asList(0, 1, 1, 2, 3, 3, 4, 5, 6, 7, 8));
			assertEquals(result,testTopologicalSort(l,Graph2.getAdj()));
			
	}
	
	//Empty Graph - JUnit Test
	@Test(expected = IllegalArgumentException.class)
	public void test5() throws InterruptedException {
			
			int l = inputProcess("Empty Graph","TopoSort-4.txt");
			testTopologicalSort(l,Graph2.getAdj());
	}

}
