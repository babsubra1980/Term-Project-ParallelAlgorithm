
import com.llp.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// LLP API

public class LLP {

	public static void main(String[] args) {
		
		System.out.println();
		System.out.println("******************************************************");
		System.out.println("************ ParallelTransitiveClosure ***************");
		System.out.println("******************************************************");
		
		ParallelTransitiveClosure pt = new ParallelTransitiveClosure();
		
		int[][] graph =
		{ 	{1, 1, 0, 1},
            {0, 1, 1, 0},
            {0, 0, 1, 1},
            {0, 0, 0, 1}
          };
		pt.transClosure(graph);
		
		System.out.println();
		System.out.println("****************************************************");
		System.out.println("************ ParallelTopologicalSort ***************");
		System.out.println("****************************************************");
		
		ParallelTopologicalSort ptsort = new ParallelTopologicalSort();
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
        adjacencyList.get(4).add(5);
        adjacencyList.get(8).add(9);
        adjacencyList.get(9).add(10);
        adjacencyList.get(5).add(6);
        adjacencyList.get(5).add(7);
        adjacencyList.get(6).add(8);
        adjacencyList.get(7).add(8);

		ptsort.topoSort(nodes,adjacencyList);
		
		System.out.println();
		System.out.println("***********************************************");
		System.out.println("************ ParallelBoruvkaMST ***************");
		System.out.println("***********************************************");
		
		ParallelBoruvkaMSTPool pbmst = new ParallelBoruvkaMSTPool();
//        List<EdgePool> edges = new ArrayList<>();
//        edges.add(new EdgePool(0, 1, 2));
//        edges.add(new EdgePool(0, 2, 3));
//        edges.add(new EdgePool(1, 2, 1));
//        edges.add(new EdgePool(1, 3, 4));
//        edges.add(new EdgePool(2, 3, 5));
//        edges.add(new EdgePool(0, 3, 5));
        int numNodes = 4;
        int[][] adjacencyMatrix = new int[numNodes][numNodes];

        // Initialize the adjacency matrix with edge weights
        adjacencyMatrix[0][1] = 2; // Edge from node 0 to node 1 with weight 2
        adjacencyMatrix[0][2] = 3; // Edge from node 0 to node 2 with weight 3
        adjacencyMatrix[1][2] = 1; // Edge from node 1 to node 2 with weight 1
        adjacencyMatrix[1][3] = 4; // Edge from node 1 to node 3 with weight 4
        adjacencyMatrix[2][3] = 5; // Edge from node 2 to node 3 with weight 5
        adjacencyMatrix[0][3] = 5; // Edge from node 0 to node 3 with weight 5
		pbmst.boruvkaMST(numNodes, adjacencyMatrix);
		
		System.out.println();
		System.out.println("************************************************");
		System.out.println("************ ParallelListRanking ***************");
		System.out.println("************************************************");
		
		ParallelListRanking plstrank = new ParallelListRanking();
		List<Integer> ll = new LinkedList<Integer>();
		ll.addAll(Arrays.asList(3,4,1,2,0,7));
	
		plstrank.listRanking(ll);
	}

}
