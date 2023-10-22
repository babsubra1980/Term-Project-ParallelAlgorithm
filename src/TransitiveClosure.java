public class TransitiveClosure {
    public class OutputBuilder implements LLPOutputBuilder<Integer[][], Integer[][]> {
        @Override
        public Integer[][] build() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'build'");
        }

        @Override
        public LLPOutputBuilder<Integer[][], Integer[][]> setInput(Integer[][] input) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setInput'");
        }

        @Override
        public LLPOutputBuilder<Integer[][], Integer[][]> setLatticeValues(int[] latticeValues) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setLatticeValues'");
        }
    }

    public class Worker implements LLPWorker<Integer[][]> {
        @Override
        public void setState(LLPWorkerState<Integer[][]> state) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setState'");
        }

        @Override
        public boolean isForbidden() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'isForbidden'");
        }

        @Override
        public int getAdvanceValue() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getAdvanceValue'");
        }

        @Override
        public void advance() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'advance'");
        }

        @Override
        public LLPWorker<Integer[][]> clone() {
            return this;
        }
    }

    public class Initializer implements LLPInitializer<Integer[][]> {
        @Override
        public int[] createInitialLatticeState(Integer[][] input) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'createInitialLatticeState'");
        }
    }

    /**
     * Run the LLP algorithm for transitive closure.
     * 
     * @param input A square matrix, each entry of which may be 0 or 1, indicating
     *              absence resp. presence of a directed edge.
     * @return The output of LLP upon completion.
     */
    public Integer[][] execute(Integer[][] input, int numThreads) throws InterruptedException {
        LLPRunner<Integer[][], Integer[][]> runner = new LLPRunner.LLPRunnerBuilder<Integer[][], Integer[][]>()
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
}
