public interface LLPInitializer<TInput> {
    /**
     * Create the initial lattice state for LLP.
     * 
     * @return The initial lattice state.
     */
    public int[] createInitialLatticeState(TInput input);
}
