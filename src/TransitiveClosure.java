public class TransitiveClosure {
    private final Object ob = new Object(); // for synchronization

    /**
     * Run the LLP algorithm for transitive closure.
     * 
     * @param input      A square matrix, each entry of which may be 0 or 1,
     *                   indicating absence resp. presence of a directed edge.
     * @param numThreads The number of threads (separate from the main thread). This
     *                   should typically be equal to the total number of cores
     *                   minus one (for full utilization off the main thread).
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

    public class OutputBuilder implements LLPOutputBuilder<Integer[][], Integer[][]> {
        private Integer[][] input;
        private int size;
        private int[] latticeValues;

        @Override
        public Integer[][] build() {
            Integer[][] output = new Integer[size][size];
            for (int i = 0; i < size; i += 1) {
                for (int j = 0; j < size; j += 1) {
                    int k = getLinearIndex(i, j, size);
                    output[i][j] = latticeValues[k];
                }
            }
            return output;
        }

        @Override
        public LLPOutputBuilder<Integer[][], Integer[][]> setInput(Integer[][] input) {
            this.input = input;
            this.size = this.input.length;
            return this;
        }

        @Override
        public LLPOutputBuilder<Integer[][], Integer[][]> setLatticeValues(int[] latticeValues) {
            this.latticeValues = latticeValues;
            return this;
        }
    }

    public class Worker implements LLPWorker<Integer[][]> {
        private LLPWorkerState<Integer[][]> state;
        private int[] linearIndicesIK;
        private int[] linearIndicesKJ;

        @Override
        public void setState(LLPWorkerState<Integer[][]> state) {
            this.state = state;
            recomputeLatticeIndices();
        }

        private void recomputeLatticeIndices() {
            int size = state.input.length;
            int latticeIndex = state.getLatticeIndex();
            int rowIdx = getRowIdx(latticeIndex, size);
            int colIdx = getColIdx(latticeIndex, size);
            linearIndicesIK = new int[size];
            linearIndicesKJ = new int[size];
            for (int k = 0; k < size; k += 1) {
                linearIndicesIK[k] = getLinearIndex(rowIdx, k, size);
                linearIndicesKJ[k] = getLinearIndex(k, colIdx, size);
            }
        }

        @Override
        public boolean isForbidden() {
            int size = state.input.length;

            for (int k = 0; k < size; k += 1) {
                int value = 0;
                int linearIndexIK = linearIndicesIK[k];
                int linearIndexKJ = linearIndicesKJ[k];
                int edgeIK = 0;
                int edgeKJ = 0;
                synchronized (ob) {
                    value = state.getValue();
                    edgeIK = state.getValue(linearIndexIK);
                    edgeKJ = state.getValue(linearIndexKJ);
                }
                if (value == 1) {
                    return false;
                }
                if (edgeIK == 1 && edgeKJ == 1) {
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
            int newValue = getAdvanceValue();
            if (newValue > state.getValue()) {
                synchronized (ob) {
                    state.setValue(newValue);
                }
            }
        }

        @Override
        public LLPWorker<Integer[][]> clone() {
            Worker workerClone = new Worker();
            if (state != null) {
                workerClone.setState(state.clone());
            }
            return workerClone;
        }

        @Override
        public void setLatticeIndex(int latticeIndex) {
            state.setLatticeIndex(latticeIndex);
            recomputeLatticeIndices();
        }
    }

    public class Initializer implements LLPInitializer<Integer[][]> {
        @Override
        public int[] createInitialLatticeState(Integer[][] input) {
            int size = input.length;
            int[] latticeState = new int[size * size];
            for (int i = 0; i < size; i += 1) {
                for (int j = 0; j < size; j += 1) {
                    int k = getLinearIndex(i, j, size);
                    latticeState[k] = input[i][j];
                }
            }
            return latticeState;
        }
    }

    private static int getLinearIndex(int rowIdx, int colIdx, int size) {
        return size * rowIdx + colIdx; // row major order
    }

    private static int getColIdx(int linearIndex, int size) {
        return linearIndex % size;
    }

    private static int getRowIdx(int linearIndex, int size) {
        return linearIndex / size;
    }
}
