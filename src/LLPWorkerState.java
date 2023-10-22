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
    private int latticeIndex;

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
     * Get the value held by any worker.
     * 
     * @param i The lattice index to retrieve.
     * @return The value.
     */
    public int getValue(int i) {
        return latticeValues[i];
    }

    /**
     * Set the lattice index.
     * 
     * @param latticeIndex The new lattice index.
     */
    public void setLatticeIndex(int latticeIndex) {
        this.latticeIndex = latticeIndex;
    }

    /**
     * Get the lattice index.
     * 
     * @return The lattice index.
     */
    public int getLatticeIndex() {
        return latticeIndex;
    }

    public LLPWorkerState<TInput> clone() {
        return new LLPWorkerState<TInput>(latticeIndex, latticeValues, input);
    }
}
