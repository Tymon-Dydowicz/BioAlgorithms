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
            val delta = lazyMove.delta //Lazy move evaluates the data here //explorer.calculateDelta(algorithmState.currentSolution, move)

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

        //TODO probably split into 2 ifs
        if (reheatingSchedule.shouldReheat(algorithmState.iteration, temperature, bestCost.toDouble())) {
            temperatureWrapper.currentTemperature = reheatingSchedule.reheat(temperatureWrapper.initialTemperature, temperature)
        } else if (coolingSchedule.shouldCool(algorithmState.iteration, temperature)){
            temperatureWrapper.currentTemperature = coolingSchedule.cool(temperature)
        }

        algorithmState.temperature = temperatureWrapper.currentTemperature

        return bestMove
    }

    override fun getName(): String {
        return "SimulatedAnnealing"
    }
}