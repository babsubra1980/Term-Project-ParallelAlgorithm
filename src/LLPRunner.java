import java.util.ArrayList;
import java.util.List;

public class LLPRunner<TInput, TOutput> {
    private final List<LLPWorker<TInput>> workers;
    private final TInput input;
    private final LLPOutputBuilder<TInput, TOutput> llpOutputBuilder;
    private final int[] latticeValues;
    private List<Thread> threads;
    private final int numThreads;
    private final DispatchAlgorithm dispatchAlgorithm;

    public static enum DispatchAlgorithm {
        /**
         * Cycle through the threads, adding a worker to each in succession (this is the
         * default).
         */
        ROUND_ROBIN,

        /**
         * Allocate workers to threads randomly.
         */
        RANDOM,

        /**
         * Split the workers into numThreads consecutive blocks and allocate each block
         * to the same thread.
         */
        NAIVE
    }

    private LLPRunner(List<LLPWorker<TInput>> workers, int[] latticeValues, TInput input,
            LLPOutputBuilder<TInput, TOutput> llpOutputBuilder, int numThreads, DispatchAlgorithm dispatchAlgorithm) {
        this.workers = workers;
        this.latticeValues = latticeValues;
        this.input = input;
        this.llpOutputBuilder = llpOutputBuilder;
        this.numThreads = numThreads;
        this.dispatchAlgorithm = dispatchAlgorithm;
    }

    public static class LLPRunnerBuilder<SInput, SOutput> {
        private LLPInitializer<SInput> initializer;
        private SInput input;
        private LLPOutputBuilder<SInput, SOutput> llpOutputBuilder;
        private List<LLPWorker<SInput>> workers;
        private LLPWorker<SInput> worker;
        private int numThreads = 8;
        private DispatchAlgorithm dispatchAlgorithm = DispatchAlgorithm.ROUND_ROBIN;

        /**
         * Builder constructor
         */
        public LLPRunnerBuilder() {
        }

        /**
         * Set the initializer
         * 
         * @param initializer The initializer
         * @return This builder
         */
        public LLPRunnerBuilder<SInput, SOutput> setInitializer(LLPInitializer<SInput> initializer) {
            this.initializer = initializer;
            return this;
        }

        /**
         * Set the input
         * 
         * @param input The input
         * @return This builder
         */
        public LLPRunnerBuilder<SInput, SOutput> setInput(SInput input) {
            this.input = input;
            return this;
        }

        public LLPRunnerBuilder<SInput, SOutput> setDispatchAlgorithm(DispatchAlgorithm dispatchAlgorithm) {
            this.dispatchAlgorithm = dispatchAlgorithm;
            return this;
        }

        /**
         * Set the number of threads (defaults to 8 if never called)
         * 
         * @param numThreads The number of threads
         * @return This builder
         */
        public LLPRunnerBuilder<SInput, SOutput> setNumThreads(int numThreads) {
            this.numThreads = numThreads;
            return this;
        }

        /**
         * Set the worker prototype
         * 
         * @param worker A prototype of a worker
         * @return This builder
         */
        public LLPRunnerBuilder<SInput, SOutput> setWorkerPrototype(LLPWorker<SInput> worker) {
            this.worker = worker;
            return this;
        }

        /**
         * Set the output builder
         * 
         * @param llpOutputBuilder The output builder
         * @return This builder
         */
        public LLPRunnerBuilder<SInput, SOutput> setOutputBuilder(LLPOutputBuilder<SInput, SOutput> llpOutputBuilder) {
            this.llpOutputBuilder = llpOutputBuilder;
            return this;
        }

        /**
         * Build the runner
         * 
         * @return The runner
         */
        public LLPRunner<SInput, SOutput> build() {
            int[] latticeValues = initializer.createInitialLatticeState(input);
            workers = new ArrayList<LLPWorker<SInput>>(latticeValues.length);
            for (int i = 0; i < latticeValues.length; i += 1) {
                LLPWorker<SInput> workerClone = worker.clone();
                workerClone.setState(new LLPWorkerState<SInput>(i, latticeValues, input));
                workers.set(i, workerClone);
            }
            return new LLPRunner<SInput, SOutput>(
                    workers,
                    latticeValues,
                    input,
                    llpOutputBuilder,
                    numThreads,
                    dispatchAlgorithm);
        }
    }

    /**
     * Run the LLP algorithm in the background.
     */
    public void computeLLP() {
        // step 1: partition the workers
        // step 2: create and start the threads
    }

    /**
     * Block the main (or calling) thread until all worker threads have completed.
     */
    public void joinAllThreads() throws InterruptedException {
        for (int i = 0; i < threads.size(); i += 1) {
            threads.get(i).join();
        }
    }

    /**
     * Get the output
     * 
     * @return The output
     */
    public TOutput getOutput() {
        return llpOutputBuilder
                .setInput(input)
                .setLatticeValues(latticeValues)
                .build();
    }
}
