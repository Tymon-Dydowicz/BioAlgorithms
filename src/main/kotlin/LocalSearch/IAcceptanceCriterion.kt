package LocalSearch

interface IAcceptanceCriterion {
//    fun selectNext(
//        current: QAPSolution,
//        neighbors: List<QAPSolution>,
//        iteration: Int,
//        bestSolutionCost: Int
//    ): Pair<Int, QAPSolution?> // evaluations, selected solution (null if no move)

    // Move-based version for efficiency
    fun selectNextMove(
        algorithmState: LocalSearchState,
        lazyEvaluatedMoves: List<LazyEvaluatedMove>
    ): LazyEvaluatedMove? // evaluations, selected move, resulting solution
    fun getName(): String
}