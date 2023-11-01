import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RunTestsTransitiveClosure {
	
    public static void testTransitiveClosure(Integer[][] graph) {
        System.out.println("**");
        System.out.println("** ParallelTransitiveClosure **");
        TransitiveClosure pt = new TransitiveClosure();
        System.out.println("**");
        


        try {
            Integer[][] output = pt.execute(graph, 2);
            for (Integer[] row : output) {
                for (Integer val : row) {
                    System.out.print(val + " ");
                }
                System.out.println();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

	public static void main(String[] args) {
		
    	String fileName = "TransitiveInput.txt"; 
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
    
        testTransitiveClosure(array);

	}

}
