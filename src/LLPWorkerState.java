public class LLPWorkerState<TInput> implements Cloneable {
    public LLPWorkerState(int latticeIndex, int[] latticeValues, TInput input) {
        this.latticeIndex = latticeIndex;
        this.latticeValues = latticeValues;
        this.input = input;
    }

    /**
     * Input object
     */
    public final TInput input;

    /**
     * The lattice index held by this worker.
     */
    public int latticeIndex;

    /**
     * Pointer to the shared memory location of the lattice vector (non-negative
     * integers). This worker may read any entry at any time, but it only allowed to
     * write to the location at index latticeIndex.
     */
    public final int[] latticeValues;

    /**
     * Set the value held by this worker.
     * 
     * @param value The new value.
     */
    public void setValue(int value) {
        latticeValues[latticeIndex] = value;
    }

    /**
     * Get the value held by this worker.
     * 
     * @return The current value.
     */
    public int getValue() {
        return latticeValues[latticeIndex];
    }

    /**
     * Set the lattice index.
     * 
     * @param latticeIndex The new lattice index.
     */
    public void setLatticeIndex(int latticeIndex) {
        this.latticeIndex = latticeIndex;
    }

    public LLPWorkerState<TInput> clone() {
        return new LLPWorkerState<TInput>(latticeIndex, latticeValues, input);
    }
}
