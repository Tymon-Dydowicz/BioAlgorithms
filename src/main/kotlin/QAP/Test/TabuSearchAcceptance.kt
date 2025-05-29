package QAP.Test

import LocalSearch.*
import QAP.TabuSearch.*
import org.slf4j.LoggerFactory

class TabuSearchAcceptance(
    val tabuList: ITabuList,
    private val aspirationCriterion: IAspirationCriterion,
    private val tenureSchedule: ITabuTenureSchedule,
) : IAcceptanceCriterion {
    private val logger = LoggerFactory.getLogger(TabuSearchAcceptance::class.java)

    override fun selectNextMove(
        algorithmState: LocalSearchState,
        lazyEvaluatedMoves: List<LazyEvaluatedMove>
    ): LazyEvaluatedMove? {
        var bestMove: LazyEvaluatedMove? = null
        var bestDelta = Int.MAX_VALUE
        val currentTenure = tenureSchedule.calculateTenure(algorithmState)

        for (lazyMove in lazyEvaluatedMoves) {
            val delta = lazyMove.delta

            val isTabu = tabuList.isTabu(lazyMove.move, algorithmState.iteration)
            val passesAspiration = aspirationCriterion.satisfies(lazyMove.move, delta, algorithmState)

            if (!isTabu || passesAspiration) {
                if (delta < bestDelta) {
                    bestDelta = delta
                    bestMove = lazyMove
                }
            }
        }

        if (bestMove != null) {
            tabuList.addMove(bestMove.move, algorithmState.iteration, currentTenure)
            tabuList.cleanExpired(algorithmState.iteration)

            val isImproving = algorithmState.currentSolution.solutionCost + bestDelta < algorithmState.bestSolutionCost
            tenureSchedule.notifyMoveSelected(bestDelta, isImproving)

            logger.trace("Selected move with delta: $bestDelta, tabu list size: ${tabuList.getTabuListSize()}")
        } else {
            logger.info("No valid move found - all moves are tabu and don't pass aspiration")
        }

        return bestMove
    }

    override fun getName(): String {
        return "TabuSearch(${tabuList.getName()},${aspirationCriterion.getName()},${tenureSchedule.getName()})"
    }

}