package QAP.SA

import LocalSearch.*
import QAP.QAPSolution
import QAP.Test.SwapMove

class TemperatureWrapper(val initialTemperature: Double) : Stateful, Resettable {
    // TODO Move this to a better place so it can account for the objective
    var currentTemperature: Double = initialTemperature
    private var initialTemperatureMemory: Double = initialTemperature

    companion object {
        fun calculateInitialTemperature(
            solution: ISolution,
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
            solution: ISolution,
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
            solution: ISolution,
            explorer: INeighborhoodExplorer,
            targetAcceptanceProbability: Double,
            sampleSize: Int
        ): Double {
            val worseningDeltas = mutableListOf<Int>()

            repeat(sampleSize) {
                val move = explorer.generateLazyRandomMove(solution, IObjective.Minimization())

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
