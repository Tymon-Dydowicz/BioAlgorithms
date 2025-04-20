package QAP.Test

import LocalSearch.IAcceptanceCriterion
import LocalSearch.LocalSearchState
import LocalSearch.IMove
import LocalSearch.INeighborhoodExplorer

class SteepestAcceptance : IAcceptanceCriterion {
    override fun selectNextMove(
        algorithmState: LocalSearchState,
        moves: List<IMove>,
        explorer: INeighborhoodExplorer,
    ): Pair<Int, IMove?> {
        var evaluations = 0
        val bestMove = moves
            .filter { explorer.calculateDelta(algorithmState.currentSolution, it) <= 0 }
            .minByOrNull { explorer.calculateDelta(algorithmState.currentSolution, it) }

        return Pair(evaluations, bestMove)
    }

    override fun getName(): String {
        return "Steepest"
    }
}