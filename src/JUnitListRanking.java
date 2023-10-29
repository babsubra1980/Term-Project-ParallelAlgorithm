

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;


public class JUnitListRanking {
	
    public static List<Integer> testListRanking(int nodes, Map<Integer,Integer> childParent) throws InterruptedException {
    	ListRanking lr = new ListRanking();
    	List<Integer> ch = new ArrayList<>(childParent.size());
        for (Map.Entry<Integer, Integer> chiPar : childParent.entrySet()) {
        		ch.add(chiPar.getKey());
        	}
        List<Integer> out = lr.execute(childParent, 2);
//        try {
//        	List<Integer> out = lr.execute(childParent, 2);
        	int k = 0;
            for (int val : out) {
                    System.out.println("Node "+ch.get(k)+" has distance "+val + " ");
                    k++;
                }
                System.out.println();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
        return out;
    }

    public static Map<Integer,Integer> inputProcess(String testName, String file) {
        System.out.println("**");
        System.out.println("** ParallelListRanking - JUnit Testing : "+ testName + " **");
    	System.out.println("**");
        Map<Integer, Integer> childParentMap = new HashMap<>();
//        String fileName = "ListRanking.txt"; 
        String fileName = file;
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
        
        return childParentMap;
    
//        testListRanking(childParentMap.size(), childParentMap);

	}
	
	//Simple Linear Graph - JUnit Test
	@Test
	public void test1() throws InterruptedException {
		
		Map<Integer,Integer> childParent = inputProcess("List Ranking of a Simple Linear Graph","ListRanking.txt");
		
		List<Integer> result = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
		assertEquals(result,testListRanking(childParent.size(), childParent));
		
	}
	
	//Tree Structure - JUnit Test
	@Test
	public void test2() throws InterruptedException {
		
		Map<Integer,Integer> childParent = inputProcess("List Ranking of a Tree Structure","ListRanking-1.txt");
		
		List<Integer> result = new ArrayList<>(Arrays.asList(1, 2, 2, 3, 4, 4, 5));
		assertEquals(result,testListRanking(childParent.size(), childParent));
		
	}

	//Single Node Graph - JUnit Test
	@Test
	public void test3() throws InterruptedException {
		
		Map<Integer,Integer> childParent = inputProcess("List Ranking of a Single Node Graph","ListRanking-2.txt");
		
		List<Integer> result = new ArrayList<>(Arrays.asList(1));
		assertEquals(result,testListRanking(childParent.size(), childParent));
		
	}
	
	//Empty Graph - JUnit Test
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void test4() throws InterruptedException {
		
		Map<Integer,Integer> childParent = inputProcess("List Ranking of a Empty Graph","ListRanking-3.txt");
		
		List<Integer> result = new ArrayList<>(Arrays.asList());
		assertEquals(result,testListRanking(childParent.size(), childParent));
		
	}
	

}
