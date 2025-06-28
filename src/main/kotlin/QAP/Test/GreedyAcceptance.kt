package QAP.Test

import LocalSearch.*
import org.slf4j.LoggerFactory

class GreedyAcceptance : IAcceptanceCriterion {
    private val logger = LoggerFactory.getLogger(GreedyAcceptance::class.java)
    override fun selectNextMove(
        algorithmState: LocalSearchState,
        lazyEvaluatedMoves: List<LazyEvaluatedMove>
    ): LazyEvaluatedMove? {
        for (move in lazyEvaluatedMoves) {
            val delta = move.delta // Lazy move evaluates the delta here
            if (delta < 0) {
                logger.trace("Selected move: ${move.move} with delta: $delta")
                return move
            }
        }

        logger.info("No move selected, all moves have non-negative delta.")
        return null // No move selected, return null
    }

    override fun getName(): String {
        return "Greedy"
    }
}