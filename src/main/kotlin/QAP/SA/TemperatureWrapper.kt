package QAP.SA

import LocalSearch.AbstrNeighborhoodExplorer
import LocalSearch.INeighborhoodExplorer
import LocalSearch.Resettable
import LocalSearch.Stateful
import QAP.QAPSolution
import QAP.Test.SwapMove

class TemperatureWrapper(val initialTemperature: Double) : Stateful, Resettable {
    var currentTemperature: Double = initialTemperature
    private var initialTemperatureMemory: Double = initialTemperature

    companion object {
        fun calculateInitialTemperature(
            solution: QAPSolution,
            explorer: INeighborhoodExplorer,
            initialAcceptanceRatio: Double = 0.9,
            sampleSize: Int = 100
        ): Double {
            return calculateTemperatureForAcceptanceProbability(
                solution,
                explorer,
                targetAcceptanceProbability = initialAcceptanceRatio,
                sampleSize = sampleSize
            )
        }

        fun calculateFreezingTemperature(
            solution: QAPSolution,
            explorer: INeighborhoodExplorer,
            epsilon: Double = 1e-3,
            sampleSize: Int = 100
        ): Double {
            return calculateTemperatureForAcceptanceProbability(
                solution,
                explorer,
                targetAcceptanceProbability = epsilon,
                sampleSize = sampleSize
            )
        }

        private fun calculateTemperatureForAcceptanceProbability(
            solution: QAPSolution,
            explorer: INeighborhoodExplorer,
            targetAcceptanceProbability: Double,
            sampleSize: Int
        ): Double {
            val worseningDeltas = mutableListOf<Int>()

            repeat(sampleSize) {
                val move = explorer.generateLazyRandomMove(solution)

                if (move.delta > 0) {
                    worseningDeltas.add(move.delta)
                }
            }

            if (worseningDeltas.isEmpty()) {
                return if (targetAcceptanceProbability >= 0.5) 100.0 else 1.0
            }

            val avgDelta = worseningDeltas.average()
            return -avgDelta / Math.log(targetAcceptanceProbability)
        }
    }

    override fun reset() {
        println("Resetting temperature to initial value: $initialTemperature")
        currentTemperature = initialTemperatureMemory
    }

    override fun clone(): Stateful {
        return TemperatureWrapper(initialTemperature)
    }
}
