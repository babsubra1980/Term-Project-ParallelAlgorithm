public class RunTests {
	

    public static void testTopologicalSort(int nodes, List<List<Integer>> list) {
		TopologicalSort ptsort = new TopologicalSort();
		

        try {
			List<Integer> out = ptsort.execute(list, 2);
            for (Integer val : out) {
                    System.out.print(val + " ");
                }
                System.out.println();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void main(String[] args) {
    	
  
        int nodes = 11;

    	
        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < nodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        adjacencyList.get(0).add(1);
        adjacencyList.get(0).add(2);
        adjacencyList.get(1).add(3);
        adjacencyList.get(2).add(3);
        adjacencyList.get(3).add(4);
        adjacencyList.get(3).add(5);
        adjacencyList.get(8).add(9);
        adjacencyList.get(9).add(10);
        adjacencyList.get(4).add(6);
        adjacencyList.get(5).add(6);
        adjacencyList.get(6).add(8);
        adjacencyList.get(7).add(8);
        
        testTopologicalSort(nodes, adjacencyList);
        
    }
    



  	
}
