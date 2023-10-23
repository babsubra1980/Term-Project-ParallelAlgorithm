import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LLPRunner<TInput, TOutput> {
    private final List<LLPWorker<TInput>> workers;
    private final TInput input;
    private final LLPOutputBuilder<TInput, TOutput> llpOutputBuilder;
    private final int[] latticeValues;
    private final List<Thread> workerThreads = new ArrayList<Thread>();
    private final List<List<LLPWorker<TInput>>> threadAssignments = new ArrayList<List<LLPWorker<TInput>>>();
    private final int numThreads;
    private final DispatchAlgorithm dispatchAlgorithm;
    private Object ob; // for synchronization
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private Thread supervisorThread;
    private final int timeout;

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

    /**
     * Run the supervisor thread.
     */
    private void RunSupervisor() {
        ob = new Object();
        synchronized (ob) {
            while (true) {
                try {
                    ob.wait(timeout);
                } catch (InterruptedException ex) {
                    // do nothing
                }

                rwlock.writeLock().lock();
                try {
                    // check if a reader is in a forbidden state
                    boolean isForbidden = false;
                    for (LLPWorker<TInput> worker : workers) {
                        if (worker.isForbidden()) {
                            isForbidden = true;
                        }
                    }
                    if (!isForbidden) {
                        break;
                    }
                } finally {
                    rwlock.writeLock().unlock();
                }
            }
        }
    }

    /**
     * Run a worker thread.
     * 
     * @param threadWorkers The workers for this thread.
     */
    private void RunWorker(List<LLPWorker<TInput>> threadWorkers) {
        if (threadWorkers.size() == 0) {
            return;
        }
        while (true) {
            rwlock.readLock().lock();
            try {
                for (LLPWorker<TInput> worker : threadWorkers) {
                    worker.advance();
                }
            } finally {
                rwlock.readLock().unlock();
            }
            if (ob != null) {
                synchronized (ob) {
                    ob.notifyAll();
                }
            }
        }
    }

    private LLPRunner(List<LLPWorker<TInput>> workers, int[] latticeValues, TInput input,
            LLPOutputBuilder<TInput, TOutput> llpOutputBuilder, int numThreads, DispatchAlgorithm dispatchAlgorithm,
            int timeout) {
        this.workers = workers;
        this.latticeValues = latticeValues;
        this.input = input;
        this.llpOutputBuilder = llpOutputBuilder;
        this.numThreads = numThreads;
        this.dispatchAlgorithm = dispatchAlgorithm;
        this.timeout = timeout;
    }

    public static class LLPRunnerBuilder<SInput, SOutput> {
        private LLPInitializer<SInput> initializer;
        private SInput input;
        private LLPOutputBuilder<SInput, SOutput> llpOutputBuilder;
        private List<LLPWorker<SInput>> workers;
        private LLPWorker<SInput> worker;
        private int numThreads = 8;
        private int timeout = 100;
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

        /**
         * Set the timeout in ms (default 100).
         * 
         * @param timeout The timeout
         * @return This builder
         */
        public LLPRunnerBuilder<SInput, SOutput> setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Set the dispatch algorithm (default round robin).
         * 
         * @param dispatchAlgorithm Dispatch algorithm
         * @return Thils builder
         */
        public LLPRunnerBuilder<SInput, SOutput> setDispatchAlgorithm(DispatchAlgorithm dispatchAlgorithm) {
            this.dispatchAlgorithm = dispatchAlgorithm;
            return this;
        }

        /**
         * Set the number of threads (defaults to 7 if never called). The number of
         * worker threads (each of which may hold multiple workers) is equal to
         * (numThreads-1). There is also a supervisor thread which is separate from the
         * main thread, bringing the total to numThreads. Thus numThreads should
         * typically be equal to the number of cores minus one.
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
                workers.add(workerClone);
            }
            return new LLPRunner<SInput, SOutput>(
                    workers,
                    latticeValues,
                    input,
                    llpOutputBuilder,
                    numThreads,
                    dispatchAlgorithm,
                    timeout);
        }
    }

    /**
     * Round robin dispatch helper.
     */
    private void partitionWorkersRoundRobin() {
        int i = 0;
        for (LLPWorker<TInput> worker : workers) {
            threadAssignments.get(i).add(worker);
            i = (i + 1) % (threadAssignments.size());
        }
    }

    /**
     * Random dispatch helper.
     */
    private void partitionWorkersRandom() {
        for (LLPWorker<TInput> worker : workers) {
            int i = ThreadLocalRandom.current().nextInt(threadAssignments.size());
            threadAssignments.get(i).add(worker);
        }
    }

    /**
     * Naive dispatch helper.
     */
    private void partitionWorkersNaive() {
        int numWorkers = workers.size();
        int numWorkerThreads = threadAssignments.size();
        int blockSize = numWorkers / numWorkerThreads;
        int remainder = numWorkers - (blockSize * numWorkerThreads);
        int i = 0; // worker index
        int curSize = blockSize + 1;
        for (List<LLPWorker<TInput>> assignment : threadAssignments) {
            if (remainder == 0 && curSize > blockSize) {
                curSize -= 1;
            } else {
                remainder -= 1;
            }

            for (int j = 0; j < curSize; j += 1) {
                assignment.add(workers.get(i));
                i += 1;
            }
        }
    }

    /**
     * Partition the workers to respective threads using the chosen dispatch
     * algorithm.
     */
    private void partitionWorkers() {
        threadAssignments.clear();
        workerThreads.clear();
        for (int i = 0; i < numThreads - 1; i += 1) {
            threadAssignments.add(new ArrayList<LLPWorker<TInput>>());
        }
        switch (dispatchAlgorithm) {
            case ROUND_ROBIN:
                partitionWorkersRoundRobin();
                break;
            case RANDOM:
                partitionWorkersRandom();
                break;
            case NAIVE:
                partitionWorkersNaive();
                break;
            default:
                partitionWorkersRoundRobin();
                break;
        }
    }

    /**
     * Construct and start all the threads. Must not be called before
     * partitionWorkers().
     */
    private void startThreads() {
        supervisorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RunSupervisor();
            }
        });
        supervisorThread.start();

        for (List<LLPWorker<TInput>> assignment : threadAssignments) {
            Thread workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    RunWorker(assignment);
                }
            });
            workerThread.start();
            workerThreads.add(workerThread);
        }
    }

    /**
     * Run the LLP algorithm in the background.
     */
    public void computeLLP() {
        partitionWorkers();
        startThreads();
    }

    /**
     * Block the main (or calling) thread until all worker threads have completed.
     */
    public void joinAllThreads() throws InterruptedException {
        for (int i = 0; i < workerThreads.size(); i += 1) {
            workerThreads.get(i).join();
        }
        supervisorThread.join();
    }

    /**
     * Get the output. Must not be called before joinAllThreads().
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
