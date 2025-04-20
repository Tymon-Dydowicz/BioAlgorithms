package QAP.Test

import LocalSearch.IAcceptanceCriterion
import LocalSearch.LocalSearchState
import LocalSearch.IMove
import LocalSearch.INeighborhoodExplorer

class GreedyAcceptance : IAcceptanceCriterion {
    override fun selectNextMove(
        algorithmState: LocalSearchState,
        moves: List<IMove>,
        explorer: INeighborhoodExplorer,
    ): Pair<Int, IMove?> {
        var evaluations = 0
        for (move in moves) {
            val delta = explorer.calculateDelta(algorithmState.currentSolution, move)
            evaluations++
            if (delta < 0) {
                return Pair(evaluations, move)
            }
        }

        return Pair(evaluations, null) // No move selected, return null
    }

    override fun getName(): String {
        return "Greedy"
    }
}