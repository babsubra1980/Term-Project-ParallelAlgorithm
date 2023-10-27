
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ListRanking {
	
	private final Object ob = new Object(); // for synchronization
	private Node[] G;
    private int[] par;
    private int[] chi;
    /**
     * Run the LLP algorithm for List Ranking.
     * @param child 
     * 
     * @param nodes2      List of Nodes
     * 
     * @param numThreads The number of threads (separate from the main thread). This
     *                   should typically be equal to the total number of cores
     *                   minus one (for full utilization off the main thread).
     * @return The output of LLP upon completion.
     */
    public List<Integer> execute(Map<Integer,Integer> childParent, int numThreads) throws InterruptedException {

    	
        LLPRunner<Map<Integer,Integer>, List<Integer>> runner = new LLPRunner.LLPRunnerBuilder<Map<Integer,Integer>, List<Integer>>()
                .setInitializer(new Initializer())
                .setInput(childParent)
                .setNumThreads(numThreads)
                .setWorkerPrototype(new Worker())
                .setOutputBuilder(new OutputBuilder())
                .build();
        runner.computeLLP();
        runner.joinAllThreads();

        return runner.getOutput();
 
    }


	public class OutputBuilder implements LLPOutputBuilder<Map<Integer,Integer>, List<Integer>> {
        private Map<Integer,Integer> input;
        private int size;
        private int[] latticeValues;


        public List<Integer> build() {
//        	System.out.println("Entering into Output Build()");
        	List<Integer> output = new ArrayList<>();
        	output.add(latticeValues[0]+1);
            for (int i = 1; i < size; i += 1) {
                   
                   output.add(latticeValues[i]);
                }
            
            return output;
        }

        @Override
        public LLPOutputBuilder<Map<Integer,Integer>, List<Integer>> setInput(Map<Integer,Integer> input) {
            this.input = input;
            this.size = this.input.size();
            return this;
        }

        @Override
        public LLPOutputBuilder<Map<Integer,Integer>, List<Integer>> setLatticeValues(int[] latticeValues) {
            this.latticeValues = latticeValues;
            return this;
        }
    }

    public class Worker implements LLPWorker<Map<Integer,Integer>> {
        private LLPWorkerState<Map<Integer,Integer>> state;
        private int currIdx;
        
        @Override
        public void setState(LLPWorkerState<Map<Integer,Integer>> state) {
//        	System.out.println("I am in worker state now: "+ state.getValue());
            this.state = state;
            

        }

        public boolean isComplete() {
            for (int j = 0; j < G.length; j++) {
                if (!isForbidden()) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean isForbidden() {
        	
        	synchronized (ob) {
        		currIdx = state.getLatticeIndex();
//    			System.out.println("STATE at index: "+currIdx+" "+(G[currIdx]));
    			return (G[currIdx].next != -1);
        	}
        }

        @Override
        public int getAdvanceValue() {
            return isForbidden() ? 1 : 0;
        }

        @Override
        public void advance() {
        	int value=0;
        	synchronized (ob) {
        	int newValue = getAdvanceValue();
        	if(newValue > 0) {
        	value = state.getValue();
            G[value].dist += G[G[value].next].dist;
            G[value].next = G[G[value].next].next;
            state.setValue(G[value].dist);
//            System.out.println("Entering Advance()" + value + " "+ G[value].dist);
        		}
        	}
        }

        @Override
        public LLPWorker<Map<Integer,Integer>> clone() {
            Worker workerClone = new Worker();
            if (state != null) {
                workerClone.setState(state.clone());
            }
            return workerClone;
        }

        @Override
        public void setLatticeIndex(int latticeIndex) {
            state.setLatticeIndex(latticeIndex);
        }
    }


    
    public class Initializer implements LLPInitializer<Map<Integer,Integer>> {
    	
        public int[] createInitialLatticeState(Map<Integer,Integer> input) {

                int n = input.size();
                G = new Node[n];
                chi= new int[n];
                par= new int[n];
                int i = 0;
                // Initialize G
                for (Map.Entry<Integer, Integer> chiPar : input.entrySet()) {
                	G[chiPar.getKey()] = new Node(1, chiPar.getValue());
                	chi[i] = chiPar.getKey();
                	par[i] = chiPar.getValue();
                	i++;
            }

            return chi;
        }
    }
    
    class Node {
        int dist;
        int next;

        public Node(int dist, int next) {
            this.dist = dist;
            this.next = next;
        }
    }
    

}
