
import java.util.ArrayList;
import java.util.List;


public class TopologicalSort {
    
//	private final Object ob = new Object(); // for synchronization
	private Boolean[] fixed = new Boolean[0];
	private Integer[] inDegrees = new Integer[0];
    /**
     * Run the LLP algorithm for Topological Sort.
     * 
     * @param input      Adjacency list, with node and it's predecessor
     * 
     * @param numThreads The number of threads (separate from the main thread). This
     *                   should typically be equal to the total number of cores
     *                   minus one (for full utilization off the main thread).
     * @return The output of LLP upon completion.
     */
    public List<Integer> execute(List<List<Integer>> input, int numThreads) throws InterruptedException {
    	
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
        private List<Integer> prevIdx;
   
        
        
        @Override
        public void setState(LLPWorkerState<List<List<Integer>>> state) {
//        	System.out.println("I am in worker state now: "+ state.getValue());
            this.state = state;
            

        }

        
        @Override
        public boolean isForbidden() {
            currIdx = state.getLatticeIndex();
            prevIdx = state.input.get(currIdx);
            System.out.println("Entering into isForbidden "+ currIdx);

            if(fixed[currIdx] == false && inDegrees[currIdx] > 0) {
            	for (int pre:prevIdx) {
            		if (fixed[pre] == true)
                		return true;
            }
        }
            return false;
        }

        @Override
        public int getAdvanceValue() {
            return isForbidden() ? 1 : 0;
        }

        @Override
        public void advance() {
        	System.out.println("Entering into advance() " + state.getLatticeIndex());
        	
        	fixed[state.getLatticeIndex()] = true;
            state.setValue(state.getLatticeIndex());
        	
        
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
    	

        @Override
        public int[] createInitialLatticeState(List<List<Integer>> input) {
        	int size = input.size();
            int[] G = new int[size];
            fixed = new Boolean[size];
       
            int[] inDegrees = new int[size];
            

            for (List<Integer> neighbors : input) {

                for (int neighbor : neighbors) {
//                	System.out.print(neighbors+ " ");
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
            
//            for (int i = 0; i < size; i++) {
//            	System.out.println("Fixed: "+i+" "+fixed[i]);
//            	System.out.println("inDegrees: "+i+" "+inDegrees[i]);
//
//                }
            
            return inDegrees;
        }
    }

}
