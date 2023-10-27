import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  
    public static void main(String[] args) {


      Map<Integer, Integer> childParentMap = new HashMap<>();
  
        childParentMap.put(0, -1);
        childParentMap.put(1, 0);
        childParentMap.put(2, 1);
        childParentMap.put(3, 2);
        childParentMap.put(4, 3);
        childParentMap.put(5, 4);
    
        testListRanking(childParentMap.size(), childParentMap);
        
    }
    



  	
}
