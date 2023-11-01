import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunTestsBoruvka {
	
	
    public static void testBoruvka(int nodes, int[][] adjacencyMatrix) {

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

    }

	public static void main(String[] args) {
	
        System.out.println("**");
        System.out.println("** ParallelBoruvka **");
    	
    	System.out.println("**");
    	System.out.println("*** Minimum Spanning Tree ***");
      int numNodes = 0;
      String fileName = "BoruvkaInput-1.txt"; 
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
        
        testBoruvka(numNodes, adjacencyMatrix);
	}

}
