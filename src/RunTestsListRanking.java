import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RunTestsListRanking {
	
    public static void testListRanking(int nodes, Map<Integer,Integer> childParent) {
        System.out.println("**");
        System.out.println("** ParallelListRanking **");
    	ListRanking lr = new ListRanking();
    	System.out.println("**");
    	List<Integer> ch = new ArrayList<>(childParent.size());
        for (Map.Entry<Integer, Integer> chiPar : childParent.entrySet()) {
        		ch.add(chiPar.getKey());
        	}
        try {
        	List<Integer> out = lr.execute(childParent, 2);
        	int k = 0;
            for (int val : out) {
                    System.out.println("Node "+ch.get(k)+" has distance "+val + " ");
                    k++;
                }
                System.out.println();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	public static void main(String[] args) {
        Map<Integer, Integer> childParentMap = new HashMap<>();
        String fileName = "ListRanking.txt"; 
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int key = Integer.parseInt(parts[0]);
                    int value = Integer.parseInt(parts[1]);
                    childParentMap.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    
        testListRanking(childParentMap.size(), childParentMap);

	}

}
