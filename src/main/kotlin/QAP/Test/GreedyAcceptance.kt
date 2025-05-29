package QAP.Test

import LocalSearch.*

class GreedyAcceptance : IAcceptanceCriterion {
    override fun selectNextMove(
        algorithmState: LocalSearchState,
        lazyEvaluatedMoves: List<LazyEvaluatedMove>,
        explorer: INeighborhoodExplorer,
    ): IMove? {
        for (move in lazyEvaluatedMoves) {
            val delta = move.delta // Lazy move evaluates the delta here
            if (delta < 0) {
                return move.move
            }
        }

        return null // No move selected, return null
    }

    override fun getName(): String {
        return "Greedy"
    }
}