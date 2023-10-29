
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TopologicalSort {
    
	private final Object ob = new Object(); // for synchronization
	private Boolean[] fixed = new Boolean[0];
	private int[] inDegrees = new int[0];
	private int[] G = new int[0];
	private List<List<Integer>> predecessorList;
	private int vertices;
    /**
     * Run the LLP algorithm for Topological Sort.
     * @param nodes 
     * 
     * @param input      Adjacency list, with node and it's predecessor
     * 
     * @param numThreads The number of threads (separate from the main thread). This
     *                   should typically be equal to the total number of cores
     *                   minus one (for full utilization off the main thread).
     * @return The output of LLP upon completion.
     */
    public List<Integer> execute(int nodes, List<List<Integer>> input, int numThreads) throws InterruptedException {
    	vertices = nodes;
        LLPRunner<List<List<Integer>>, List<Integer>> runner = new LLPRunner.LLPRunnerBuilder<List<List<Integer>>, List<Integer>>()
                .setInitializer(new Initializer())
                .setInput(input)
                .setNumThreads(numThreads)
                .setWorkerPrototype(new Worker())
                .setOutputBuilder(new OutputBuilder())
                .build();
        runner.computeLLP();
        runner.joinAllThreads();

        return runner.getOutput();
 
    }

    public class OutputBuilder implements LLPOutputBuilder<List<List<Integer>>, List<Integer>> {
        private List<List<Integer>> input;
        private int size;
        private int[] latticeValues;


        public List<Integer> build() {
//        	System.out.println("Entering into Output Build()");
        	List<Integer> output = new ArrayList<>();
            for (int i = 0; i < size; i += 1) {
                    
                    output.add(latticeValues[i]);
                }
            
            return output;
        }

        @Override
        public LLPOutputBuilder<List<List<Integer>>, List<Integer>> setInput(List<List<Integer>> input) {
            this.input = input;
            this.size = this.input.size();
            return this;
        }

        @Override
        public LLPOutputBuilder<List<List<Integer>>, List<Integer>> setLatticeValues(int[] latticeValues) {
            this.latticeValues = latticeValues;
            return this;
        }
    }

    public class Worker implements LLPWorker<List<List<Integer>>> {
        private LLPWorkerState<List<List<Integer>>> state;
        private int currIdx;
        
        @Override
        public void setState(LLPWorkerState<List<List<Integer>>> state) {
//        	System.out.println("I am in worker state now: "+ state.getValue());
            this.state = state;
            

        }

        
        @Override
        public boolean isForbidden() {
        	
        	synchronized (ob) {
        	currIdx = state.getLatticeIndex();
//        	System.out.println("Entering into isForbidden "+ currIdx);

//            System.out.println(fixed[currIdx]);

//            	prevIdx = state.input.get(currIdx);
            	
//                	value = inDegrees[currIdx];
//                	System.out.println(fixed[currIdx]);

            //forbidden(j): (fixed[j] = false) ^ for all pre(j) : fixed[i]
            
        	if(fixed[currIdx] == false) {
//        		System.out.println("Entering into isForbidden "+ currIdx);
        		for (int pre:predecessorList.get(currIdx)) {
        			if (fixed[pre] == false)
        				return false;
        		}
        		return true;
            }
        	}
            return false;
        }

        @Override
        public int getAdvanceValue() {
            if (!isForbidden()) {
                return state.getValue();
            }

            List<Integer> predecessors = predecessorList.get(state.getLatticeIndex());
            int maxVal = 0;
            for (int i : predecessors) {
                maxVal = Math.max(state.getValue(i), maxVal);
            }

            return maxVal + 1;
        }

        @Override
        public void advance() {
        	
            int newValue = getAdvanceValue();
            if (newValue > state.getValue()) {
//                state.setValue(state.getLatticeIndex());
            	state.setValue(newValue);

                // must assign fixed _after_ state to avoid possible data race
                fixed[state.getLatticeIndex()] = true;
            }
        }

        @Override
        public LLPWorker<List<List<Integer>>> clone() {
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

    public class Initializer implements LLPInitializer<List<List<Integer>>> {
    	
        public int[] createInitialLatticeState(List<List<Integer>> input) {
        	int size = vertices;
        	
            G = new int[size];
            fixed = new Boolean[size];
            inDegrees = new int[size];
            
            for (List<Integer> neighbors : input) {

                for (int neighbor : neighbors) {
                    inDegrees[neighbor]++;
                    fixed[neighbor] = false;
                    G[neighbor] = 0;
           
                }
            }
            for (int i = 0; i < size; i++) {

                if (inDegrees[i] == 0) {
                    fixed[i] = true;
                }
            }
            
            if(!Arrays.stream(inDegrees).anyMatch(num -> num == 0))
            {
            	throw new IllegalArgumentException("The graph contains a cycle.");
            }
      
            
                predecessorList = new ArrayList<>(size);

                for (int i = 0; i < size; i++) {
                    predecessorList.add(new ArrayList<>());
                }

                for (int u = 0; u < size; u++) {
                    for (int v : input.get(u)) {
                        predecessorList.get(v).add(u);
                    }
                }
            
            return G;
        }
    }

}
