public interface LLPWorker<TInput> extends Cloneable {
    /**
     * Set the state container for this worker; this only needs to be called once
     * per worker, during the initialization phase. No two workers may hold the same
     * LLPWorkerState (but multiple workers may exist on the same thread).
     * 
     * @param state State held by this worker.
     */
    public void setState(LLPWorkerState<TInput> state);

    /**
     * Check whether the lattice index held by this worker is in a forbidden state.
     * 
     * @return True if it is forbidden, false otherwise.
     */
    public boolean isForbidden();

    /**
     * Set the lattice index for this worker. The typical implementation is:
     * 
     * state.setLatticeIndex(latticeIndex);
     * 
     * @param latticeIndex The new lattice index.
     */
    public void setLatticeIndex(int latticeIndex);

    /**
     * Compute the (non-negative) value to which the held index may advance.
     * 
     * @return The position to which the held index may advance. Does not need to be
     *         optimal but must be at least "one more" than the previous value if
     *         the local state is not forbidden prior to calling this method.
     */
    public int getAdvanceValue();

    /**
     * Advance the held index if it is forbidden; otherwise, do nothing. The typical
     * implementation is:
     * 
     * if (isForbidden()) state.setValue(getAdvanceValue());
     */
    public void advance();

    /**
     * Clone this worker instance.
     * 
     * @return Clone of this instance
     */
    public LLPWorker<TInput> clone();
}
