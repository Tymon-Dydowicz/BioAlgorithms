package QAP.Test

import LocalSearch.IAcceptanceCriterion
import LocalSearch.LocalSearchState
import LocalSearch.IMove
import LocalSearch.INeighborhoodExplorer
import QAP.TabuSearch.IAspirationCriterion
import QAP.TabuSearch.IMoveFeatureExtractor
import QAP.TabuSearch.ITabuList
import QAP.TabuSearch.ITabuTenureSchedule
import org.slf4j.LoggerFactory

class TabuSearchAcceptance(
    private val tabuList: ITabuList,
    private val aspirationCriterion: IAspirationCriterion,
    private val tenureSchedule: ITabuTenureSchedule
) : IAcceptanceCriterion {
    private val logger = LoggerFactory.getLogger(TabuSearchAcceptance::class.java)

    override fun selectNextMove(
        algorithmState: LocalSearchState,
        moves: List<IMove>,
        explorer: INeighborhoodExplorer
    ): Pair<Int, IMove?> {
        var evaluations = 0
        var bestMove: IMove? = null
        var bestDelta = Int.MAX_VALUE

        val currentTenure = tenureSchedule.calculateTenure(algorithmState)

        for (move in moves) {
            val delta = explorer.calculateDelta(algorithmState.currentSolution, move)
            evaluations++

            val isTabu = tabuList.isTabu(move, algorithmState.iteration)
            val passesAspiration = aspirationCriterion.satisfies(move, delta, algorithmState)

            if (!isTabu || passesAspiration) {
                if (delta < bestDelta) {
                    bestDelta = delta
                    bestMove = move
                }
            }
        }

        if (bestMove != null) {
            tabuList.addMove(bestMove, algorithmState.iteration, currentTenure)
            tabuList.cleanExpired(algorithmState.iteration)

            val isImproving = algorithmState.currentSolution.solutionCost + bestDelta < algorithmState.bestSolutionCost
            tenureSchedule.notifyMoveSelected(bestDelta, isImproving)

            logger.info("Selected move with delta: $bestDelta, tabu list size: ${tabuList.getTabuListSize()}")
        } else {
            logger.info("No valid move found - all moves are tabu and don't pass aspiration")
        }

        return Pair(evaluations, bestMove)
    }

    override fun getName(): String {
        return "TabuSearch(${tabuList.getName()},${aspirationCriterion.getName()},${tenureSchedule.getName()})"
    }

}