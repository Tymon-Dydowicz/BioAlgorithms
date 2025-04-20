package QAP.SA

import LocalSearch.INeighborhoodExplorer
import QAP.QAPSolution

class TemperatureWrapper(val initialTemperature: Double) {
    var currentTemperature: Double = initialTemperature

    companion object {
        fun calculateInitialTemperature(
            solution: QAPSolution,
            explorer: INeighborhoodExplorer,
            initialAcceptanceRatio: Double = 0.8,
            sampleSize: Int = 100
        ): Double {
            val worseningDeltas = mutableListOf<Int>()

            repeat(sampleSize) {
                val randomMove = explorer.generateRandomMove(solution)
                val delta = explorer.calculateDelta(solution, randomMove)

                if (delta > 0) {
                    worseningDeltas.add(delta)
                }
            }

            if (worseningDeltas.isEmpty()) {
                return 100.0
            }

            val avgDelta = worseningDeltas.average()

            return -avgDelta / Math.log(initialAcceptanceRatio)
        }
    }
}
