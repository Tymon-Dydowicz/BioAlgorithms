package QAP.Test

import LocalSearch.*

class SteepestAcceptance : IAcceptanceCriterion {
    override fun selectNextMove(
        algorithmState: LocalSearchState,
        lazyEvaluatedMoves: List<LazyEvaluatedMove>,
        explorer: INeighborhoodExplorer,
    ): IMove? {
        val bestMove = lazyEvaluatedMoves
            .filter { it.delta < 0 } // Lazy move evaluates the delta here
            .minByOrNull { it.delta } // Lazy move has the cached delta

        return bestMove?.move
    }

    override fun getName(): String {
        return "Steepest"
    }
}