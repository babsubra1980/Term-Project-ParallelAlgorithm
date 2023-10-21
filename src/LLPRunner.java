import java.util.ArrayList;
import java.util.List;

public class LLPRunner<TInput, TOutput> {
    private final List<LLPWorker<TInput>> workers;
    private final TInput input;
    private final LLPOutputBuilder<TInput, TOutput> llpOutputBuilder;
    private final int[] latticeValues;

    private LLPRunner(List<LLPWorker<TInput>> workers, int[] latticeValues, TInput input,
            LLPOutputBuilder<TInput, TOutput> llpOutputBuilder) {
        this.workers = workers;
        this.latticeValues = latticeValues;
        this.input = input;
        this.llpOutputBuilder = llpOutputBuilder;
    }

    public class LLPRunnerBuilder {
        private LLPInitializer<TInput> initializer;
        private TInput input;
        private LLPOutputBuilder<TInput, TOutput> llpOutputBuilder;
        private List<LLPWorker<TInput>> workers;
        private LLPWorker<TInput> worker;

        public LLPRunnerBuilder() {
        }

        /**
         * Set the initializer
         * 
         * @param initializer The initializer
         * @return This builder
         */
        public LLPRunnerBuilder setInitializer(LLPInitializer<TInput> initializer) {
            this.initializer = initializer;
            return this;
        }

        /**
         * Set the input
         * 
         * @param input The input
         * @return This builder
         */
        public LLPRunnerBuilder setInput(TInput input) {
            this.input = input;
            return this;
        }

        /**
         * Set the worker prototype
         * 
         * @param worker A prototype of a worker
         * @return This builder
         */
        public LLPRunnerBuilder setWorkerPrototype(LLPWorker<TInput> worker) {
            this.worker = worker;
            return this;
        }

        /**
         * Set the output builder
         * 
         * @param llpOutputBuilder The output builder
         * @return This builder
         */
        public LLPRunnerBuilder setOutputBuilder(LLPOutputBuilder<TInput, TOutput> llpOutputBuilder) {
            this.llpOutputBuilder = llpOutputBuilder;
            return this;
        }

        /**
         * Build the runner
         * 
         * @return The runner
         */
        public LLPRunner<TInput, TOutput> build() {
            int[] latticeValues = initializer.createInitialLatticeState(input);
            workers = new ArrayList<LLPWorker<TInput>>(latticeValues.length);
            for (int i = 0; i < latticeValues.length; i += 1) {
                LLPWorker<TInput> workerClone = worker.clone();
                workerClone.setState(new LLPWorkerState<TInput>(i, latticeValues, input));
                workers.set(i, workerClone);
            }
            return new LLPRunner<TInput, TOutput>(
                    workers,
                    latticeValues,
                    input,
                    llpOutputBuilder);
        }
    }

    /**
     * Run the LLP algorithm.
     */
    public void computeLLP() {

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
