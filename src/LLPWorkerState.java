public class LLPWorkerState<TInput> {
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
    public final int latticeIndex;

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
}
