package com.llp;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelTransitiveClosure {
	
	public int[][] generateRandomArray(int numRows, int numCols) {
        int[][] randomArray = new int[numRows][numCols];
        Random rand = new Random();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                randomArray[i][j] = rand.nextInt(2); // Generates 0 or 1
            }
        }

        return randomArray;
    }
	
	public void transClosure(int[][] randomArray) {
        // Record the start time
//        long startTime = System.nanoTime();
		
//		 	int numRows = 1000;
//	        int numCols = 1000;

//	        int[][] randomArray = generateRandomArray(numRows, numCols);
//			int[][] randomArray =
//				{ 	{1, 1, 0, 1},
//	                {0, 1, 1, 0},
//	                {0, 0, 1, 1},
//	                {0, 0, 0, 1}
//	              };
			
		int numVertices = randomArray.length;
		System.out.println("Graph Length: " + numVertices);
		
		//Use a thread pool for parallel processing
		
		ExecutorService executor = Executors.newFixedThreadPool(numVertices);
		
		
		boolean[][] transitiveClosure = new boolean[numVertices][numVertices];
		
		for(int i=0;i<numVertices;i++) {
			for(int j=0; j<numVertices;j++) {
				transitiveClosure[i][j] = (randomArray[i][j]==1);
			}
		}
	
		
		for(int k=0;k<numVertices;k++) {
		    int finalK = k;
		    
		    executor.execute(() -> {
//		    System.out.println("in thread");
			for(int i=0;i<numVertices;i++) {
				if(transitiveClosure[i][finalK]) {
					
						for(int j=0; j<numVertices;j++) {
							if(transitiveClosure[finalK][j]) {
								transitiveClosure[i][j] = true ;
						}
						}
					}
				}
		});
	 }
		executor.shutdown();
		
		
		
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Transitive Closure: ");
		for(int i=0;i<numVertices;i++) {
			for(int j=0; j<numVertices;j++) {
				System.out.print((transitiveClosure[i][j] ? 1:0) + " ");
		}
		System.out.println();
	}
//        // Record the end time
//        long endTime = System.nanoTime();
//        // Calculate the elapsed time in milliseconds
//        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
//
//        System.out.println("Code execution time: " + elapsedTime + " milliseconds");
}

}
