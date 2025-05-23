package QAP.Test

import LocalSearch.IAcceptanceCriterion
import LocalSearch.LocalSearchState
import LocalSearch.IMove
import LocalSearch.INeighborhoodExplorer
import QAP.SA.ICoolingSchedule
import QAP.SA.IReheatingSchedule
import QAP.SA.TemperatureWrapper
import org.slf4j.LoggerFactory

class SimulatedAnnealingAcceptance(
    private val temperatureWrapper: TemperatureWrapper,
    private val reheatingSchedule: IReheatingSchedule,
    private val coolingSchedule: ICoolingSchedule
): IAcceptanceCriterion {
    private val logger = LoggerFactory.getLogger(SimulatedAnnealingAcceptance::class.java)

    override fun selectNextMove(
        algorithmState: LocalSearchState,
        moves: List<IMove>,
        explorer: INeighborhoodExplorer,
    ): Pair<Int, IMove?> {
        var evaluations = 0
        val temperature = temperatureWrapper.currentTemperature
        var bestMove: IMove? = null
        var bestCost = Int.MAX_VALUE

        for (move in moves) {
            val delta = explorer.calculateDelta(algorithmState.currentSolution, move)
            evaluations++

            if (delta < 0) {
                logger.info("Accepted improving move with delta: $delta, temperature: $temperature")
                bestMove = move
                bestCost = delta
                algorithmState.iterationsWithoutImprovement = 0
                break
            } else {
                val acceptanceProbability = Math.exp(-delta / temperature)
                if (Math.random() < acceptanceProbability) {
                    logger.info("Accepted deteriorating move with delta: $delta, temperature: $temperature")
                    bestMove = move
                    bestCost = delta
                    algorithmState.iterationsWithoutImprovement++
                    break
                }
            }
        }

        if (reheatingSchedule.shouldReheat(algorithmState.iteration, temperature, bestCost.toDouble())) {
            temperatureWrapper.currentTemperature = reheatingSchedule.reheat(temperatureWrapper.initialTemperature, temperature)
        } else {
            temperatureWrapper.currentTemperature = coolingSchedule.cool(temperature)
        }

        algorithmState.temperature = temperatureWrapper.currentTemperature

        return Pair(evaluations, bestMove)
    }

    override fun getName(): String {
        return "SimulatedAnnealing"
    }
}