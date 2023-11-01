import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.*;

public class JUnitTransitiveClosure {
	
	
	public static Integer[][] testTransitiveClosure(Integer[][] graph) throws InterruptedException {

        TransitiveClosure pt = new TransitiveClosure();
        return pt.execute(graph, 2);
    }

	public static Integer[][] inputProcess(String testName, String file) {
		
//    	String fileName = "TransitiveInput.txt";
        System.out.println("**");
        System.out.println("** ParallelTransitiveClosure - JUnit Testing : "+ testName + " **");
        System.out.println("**");
		String fileName = file; 
        Integer[][] array;

        // Count the number of rows and columns in the file
        int numRows = 0;
        int numCols = 0;
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (numCols == 0) {
                    numCols = values.length;
                }
                numRows++;
            }
            
            // Reset the file reader
            br.close();
            
        }catch (IOException e) {
            e.printStackTrace();
        }

        // Create the 2D array
        array = new Integer[numRows][numCols];
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
          
            int row = 0;
            while ((line = br.readLine()) != null) {
            	
                String[] values = line.split(",");
                for (int col = 0; col < numCols; col++) {
                    array[row][col] = Integer.parseInt(values[col]);

                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if(numRows != numCols) {
//        	System.out.println("Not a square matrix");
        	throw new IllegalArgumentException("Not a SQUARE Matrix Graph");
        }
        return array;
        //testTransitiveClosure(array);

	}

	
	
	// Transitive Closure of a Graph with 4 nodes - Linear Graph
	
	@Test 
	public void test1() throws InterruptedException {
	      
	      Integer[][] output = {
	                {
	                    1, 1, 1, 1
	            },
	            {
	                    0, 1, 1, 1
	            },
	            {
	                    0, 0, 1, 1
	            },
	            {
	                    0, 0, 0, 1
	            }
	    };
	      Integer[][] graph = inputProcess("Transitive Closure of a Graph with 4 nodes - Linear Graph","TransitiveInput.txt");

	      assertArrayEquals(output,testTransitiveClosure(graph));
   
    
}
	
	// Transitive Closure of a Empty Graph with 0 nodes
	
	@Test
	public void test2() throws InterruptedException {
		
	    
	      Integer[][] output = {};

	      Integer[][] graph = inputProcess("Transitive Closure of a Empty Graph with 0 nodes","TransitiveInput-1.txt");

	      
	      assertArrayEquals(output,testTransitiveClosure(graph));
		
	}
	
	// Transitive Closure of a Graph with non square matrix
	
	@Test(expected = IllegalArgumentException.class)
	public void test3() throws InterruptedException {

	      Integer[][] graph = inputProcess("Transitive Closure of a Graph without square matrix","TransitiveInput-2.txt");
	      testTransitiveClosure(graph);
	     
		
	}
	
	// Transitive Closure of a Cyclic Graph
	   @Test
	    public void test4() throws InterruptedException {
	        // Create a graph with disconnected components

	        int[][] expectedTransitiveClosure = {
	        	     {1, 1, 1, 1},
	                 {1, 1, 1, 1},
	                 {1, 1, 1, 1},
	                 {1, 1, 1, 1}
	        };
	        
	        Integer[][] graph = inputProcess("Transitive Closure of a Cyclic Graph","TransitiveInput-3.txt");
	        
	        assertArrayEquals(expectedTransitiveClosure, testTransitiveClosure(graph));
	    }
	   
		// Transitive Closure of a Cyclic Graph
	   @Test
	    public void test5() throws InterruptedException {
	        // Create a graph with disconnected components

	        int[][] expectedTransitiveClosure = {
	        		{0, 1, 0, 0},
	                {0, 0, 0, 0},
	                {0, 0, 0, 0},
	                {0, 0, 0, 0}
	        };
	        
	        Integer[][] graph = inputProcess("Transitive Closure of a Graph with disconnected components","TransitiveInput-4.txt");
	        
	        assertArrayEquals(expectedTransitiveClosure, testTransitiveClosure(graph));
	    }
}
