public interface LLPOutputBuilder<TInput, TOutput> {
    /**
     * Build the output
     * 
     * @return The output
     */
    public TOutput build();

    /**
     * Set the input
     * 
     * @param input The input
     * @return This builder
     */
    public LLPOutputBuilder<TInput, TOutput> setInput(TInput input);

    /**
     * Set the computed lattice values upon termination of LLP
     * 
     * @param latticeValues The lattice values
     * @return This builder
     */
    public LLPOutputBuilder<TInput, TOutput> setLatticeValues(int[] latticeValues);
}
