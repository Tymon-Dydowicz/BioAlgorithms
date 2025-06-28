package QAP.Test

import LocalSearch.*
import QAP.SA.ICoolingSchedule
import QAP.SA.IReheatingSchedule
import QAP.SA.TemperatureWrapper
import org.slf4j.LoggerFactory
import kotlin.math.exp

class SimulatedAnnealingAcceptance(
    val temperatureWrapper: TemperatureWrapper,
    private val reheatingSchedule: IReheatingSchedule,
    private val coolingSchedule: ICoolingSchedule
): IAcceptanceCriterion {
    private val logger = LoggerFactory.getLogger(SimulatedAnnealingAcceptance::class.java)

    override fun selectNextMove(
        algorithmState: LocalSearchState,
        lazyEvaluatedMoves: List<LazyEvaluatedMove>
    ): LazyEvaluatedMove? {
        val temperature = temperatureWrapper.currentTemperature
        var bestMove: LazyEvaluatedMove? = null
        var bestCost = Int.MAX_VALUE

        for (lazyMove in lazyEvaluatedMoves) {
            val delta = lazyMove.delta

            if (delta < 0) {
                logger.trace("Accepted improving move with delta: $delta, temperature: $temperature")
                bestMove = lazyMove
                bestCost = delta
                algorithmState.iterationsWithoutImprovement = 0
                break
            } else {
                val acceptanceProbability = exp(-delta / temperature)
                if (Math.random() < acceptanceProbability) {
                    logger.trace("Accepted deteriorating move with delta: $delta, temperature: $temperature")
                    bestMove = lazyMove
                    bestCost = delta
                    algorithmState.iterationsWithoutImprovement++
                    break
                }
            }
        }

        temperatureWrapper.updateTemperature(algorithmState.iteration, bestCost.toDouble(), coolingSchedule, reheatingSchedule)
        algorithmState.temperature = temperatureWrapper.currentTemperature

        if (bestMove == null) logger.info("No move selected, all moves have non-negative delta or were rejected.")

        return bestMove
    }

    override fun getName(): String {
        return "SimulatedAnnealing"
    }
}