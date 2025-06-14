package LocalSearch

interface IAcceptanceCriterion {
    fun selectNextMove(
        algorithmState: LocalSearchState,
        lazyEvaluatedMoves: List<LazyEvaluatedMove>
    ): LazyEvaluatedMove?
    fun getName(): String
}