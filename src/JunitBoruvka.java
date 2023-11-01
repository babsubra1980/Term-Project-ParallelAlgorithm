import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import org.junit.Test;



public class JunitBoruvka {
	
	
    public static Boruvka.WeightedGraph testBoruvka(int nodes, int[][] adjacencyMatrix) {

    	Boruvka lr = new Boruvka();
    	
        int numNodes = nodes;
        
        List<Boruvka.WeightedEdge> edges = new ArrayList<>();
        
        
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
            	if (adjacencyMatrix[i][j] == 0) 
            			adjacencyMatrix[i][j] = Integer.MAX_VALUE;
            	
            	edges.add(new Boruvka.WeightedEdge(i, j, adjacencyMatrix[i][j]));
            }
            
        }
        
        Boruvka.WeightedGraph wr = new Boruvka.WeightedGraph(numNodes, edges);
        
        

        Boruvka.WeightedGraph out = null;
		try {
			out = lr.execute(wr,2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(out.edges.isEmpty())
			System.out.println("Empty Graph");
		
       for (Boruvka.WeightedEdge val : out.edges) {
            	System.out.println("Node "+val.source + " <-> " + "Node "+val.dest + " " + "Weight: "+val.weight);
                 
                }
            
            return out;

    }

	public static Integer[][] generateRandomArray(int numRows, int numCols) {
        Integer[][] randomArray = new Integer[numRows][numCols];
        Random rand = new Random();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                randomArray[i][j] = rand.nextInt(2); // Generates 0 or 1
            }
        }

        return randomArray;
    }
	
	public static int[][] inputProcess(String testName, String file) {
	
        System.out.println("**");
        System.out.println("** ParallelBoruvka - JUnit Testing : "+ testName + " **");
    	System.out.println("**");
		System.out.println("*** Minimum Spanning Tree ***");
      int numNodes = 0;
      String fileName = file; 
      String line;
      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      
      while ((line = br.readLine()) != null) {
          String[] parts = line.split(" ");
          if (parts.length == 1) {
              numNodes = Integer.parseInt(parts[0]);
          }
      	}
      br.close();
      } catch (IOException e) {
      e.printStackTrace();
      }
      
//      if(numNodes == 0) {
//    	  System.out.println("Empty Graph");
//      }
        int[][] adjacencyMatrix = new int[numNodes][numNodes];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    int src = Integer.parseInt(parts[0]);
                    int dst = Integer.parseInt(parts[1]);
        			int w = Integer.parseInt(parts[2]);
                    adjacencyMatrix[src][dst] = w;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return adjacencyMatrix;
//        testBoruvka(numNodes, adjacencyMatrix);
	}
	
	int numRows,numCols = 100;
	Integer[][] randomGraph = generateRandomArray(numRows, numCols);
	
	//Simple Linear Graph - JUnit Test
	@Test
	public void test1() throws InterruptedException {
		
		int[][] adj = inputProcess("Boruvka's algorithm of a Simple Linear Graph", "BoruvkaInput-1.txt");
		
		List<Boruvka.WeightedEdge> resultEdges = new ArrayList<>();
	
		resultEdges.add(new Boruvka.WeightedEdge(0, 3, 5.0));
		resultEdges.add(new Boruvka.WeightedEdge(1, 0, 10.0));
		resultEdges.add(new Boruvka.WeightedEdge(3, 2, 4.0));
		
		Boruvka.WeightedGraph expectedResult = new Boruvka.WeightedGraph(3,resultEdges);
		
		Boruvka.WeightedGraph actualResult = testBoruvka(adj.length, adj);
		
        for (int k=0;k<resultEdges.size();k++) {
        	assertEquals(expectedResult.edges.get(k).source,actualResult.edges.get(k).source);
        	assertEquals(expectedResult.edges.get(k).dest,actualResult.edges.get(k).dest);
        	assertEquals(expectedResult.edges.get(k).weight,actualResult.edges.get(k).weight,0.01);
         
        }
	}
	
	@Test
	public void test2() throws InterruptedException {
		
		int[][] adj = inputProcess("Boruvka's algorithm of a Tree Structure", "BoruvkaInput-2.txt");
		
		List<Boruvka.WeightedEdge> resultEdges = new ArrayList<>();
	
		resultEdges.add(new Boruvka.WeightedEdge(0, 1, 2.0));
		resultEdges.add(new Boruvka.WeightedEdge(2, 3, 1.0));
		resultEdges.add(new Boruvka.WeightedEdge(5, 6, 4.0));
		resultEdges.add(new Boruvka.WeightedEdge(6, 4, 2.0));
		resultEdges.add(new Boruvka.WeightedEdge(7, 8, 7.0));
		resultEdges.add(new Boruvka.WeightedEdge(9, 10, 3.0));
		
		Boruvka.WeightedGraph expectedResult = new Boruvka.WeightedGraph(3,resultEdges);
		
		Boruvka.WeightedGraph actualResult = testBoruvka(adj.length, adj);
		

		
        for (int k=0;k<resultEdges.size();k++) {
//        	System.out.println(expectedResult.edges.get(k).weight + " " +actualResult.edges.get(k).weight);
        	assertEquals(expectedResult.edges.get(k).source,actualResult.edges.get(k).source);
        	assertEquals(expectedResult.edges.get(k).dest,actualResult.edges.get(k).dest);
        	assertEquals(expectedResult.edges.get(k).weight,actualResult.edges.get(k).weight,0.01);
         
        }
	}

	
	@Test
	public void test3() throws InterruptedException {
		
		int[][] adj = inputProcess("Boruvka's algorithm of a Large Graph - MST distance", "BoruvkaInput-4.txt");
		int totalDistance = 0;
		
		Boruvka.WeightedGraph actualResult = testBoruvka(adj.length, adj);
		
        for (Boruvka.WeightedEdge val : actualResult.edges) {
        	totalDistance += val.weight;
             
            }

		assertEquals(46,totalDistance);

        
	}
	
	@Test
	public void test4() throws InterruptedException {
		
		int[][] adj = inputProcess("Boruvka's algorithm of Empty Graph", "BoruvkaInput-3.txt");
		
		Boruvka.WeightedGraph actualResult = testBoruvka(adj.length, adj);
		
		assertTrue(actualResult.edges.isEmpty());
		

	}
}
