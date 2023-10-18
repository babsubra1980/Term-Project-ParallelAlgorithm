package com.llp;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;





public class ParallelListRanking {
	class ListNodeP {
	    int value;
	    ListNodeP next;

	    public ListNodeP(int value) {
	        this.value = value;
	        this.next = null;
	    }
	}


	private ListNodeP head;

	// Append a new node with the given data to the end of the linked list
	public void append(int data) {
		ListNodeP newNode = new ListNodeP(data);

	    if (head == null) {
	        // If the list is empty, the new node becomes the head
	        head = newNode;
	    } else {
	        // Traverse to the end of the list and add the new node
	    	ListNodeP current = head;
	        while (current.next != null) {
	            current = current.next;
	        }
	        current.next = newNode;
	    }
	}
    @SuppressWarnings("serial")
	public  class RankListTask extends RecursiveTask<ListNodeP> {
        private ListNodeP head;

        public RankListTask(ListNodeP head) {
            this.head = head;
        }

        @Override
        protected ListNodeP compute() {
            if (head == null || head.next == null) {
                return head; // List is already sorted
            }

            ListNodeP mid = getMiddle(head);
            ListNodeP left = head;
            ListNodeP right = mid.next;
            mid.next = null; // Split the list into two halves

            RankListTask leftTask = new RankListTask(left);
            RankListTask rightTask = new RankListTask(right);

            leftTask.fork();
            ListNodeP rightResult = rightTask.compute();
            ListNodeP leftResult = leftTask.join();

            return merge(leftResult, rightResult);
        }

        private ListNodeP getMiddle(ListNodeP head) {
            if (head == null) {
                return null;
            }
            ListNodeP slow = head;
            ListNodeP fast = head.next;

            while (fast != null) {
                fast = fast.next;
                if (fast != null) {
                    slow = slow.next;
                    fast = fast.next;
                }
            }
            return slow;
        }

        private ListNodeP merge(ListNodeP left, ListNodeP right) {
        	ListNodeP dummy = new ListNodeP(0);
        	ListNodeP current = dummy;

            while (left != null && right != null) {
                if (left.value < right.value) {
                    current.next = left;
                    left = left.next;
                } else {
                    current.next = right;
                    right = right.next;
                }
                current = current.next;
            }

            if (left != null) {
                current.next = left;
            } else if (right != null) {
                current.next = right;
            }

            return dummy.next;
        }
    }

    public static void printList(ListNodeP head) {
    	ListNodeP current = head;
        while (current != null) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }

    public void listRanking(List<Integer> array) {

        // Record the start time
//        long startTime = System.nanoTime();
  
        
        // Create a sample linked list
//        ListNodeP head = new ListNodeP(array.get(0));
//        head.next = new ListNodeP(array.get(1));
//        head.next.next = new ListNodeP(array.get(2));
//        head.next.next.next = new ListNodeP(array.get(3));
        
        ParallelListRanking plr = new ParallelListRanking();
        // append nodes to the linked-list
        for (int i=0;i<array.size();i++)
        {
        	plr.append(array.get(i));
        }
        System.out.println("Original List:");
        printList(plr.head);

        ForkJoinPool pool = ForkJoinPool.commonPool();
        ListNodeP sortedList = pool.invoke(new RankListTask(plr.head));

        System.out.println("Sorted List:");
        printList(sortedList);
//        // Record the end time
//        long endTime = System.nanoTime();
//        // Calculate the elapsed time in milliseconds
//        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
//
//        System.out.println("Code execution time: " + elapsedTime + " milliseconds");
    }
}
