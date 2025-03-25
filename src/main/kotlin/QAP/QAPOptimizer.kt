package QAP

import Util.Randomizer

object QAPOptimizer {
    fun generateRandomSolution(qap: QAPInstance): QAPSolution {
        val locations = Array(qap.instanceSize) { it }
        val randomSolution = Randomizer.randomShuffle(locations)

        return QAPSolution(qap, randomSolution.toIntArray())
    }

    fun generateHeuristicSolution(instance: QAPInstance): QAPSolution {
        var solution = mutableListOf<Int>()
        var locations = MutableList(instance.instanceSize) { it }
        val intialFacility = Randomizer.getRandomIndex(instance.instanceSize)

        solution.add(intialFacility)
        locations.remove(intialFacility)

        while (locations.isNotEmpty()) {
            val nextFacility = locations.minByOrNull { calculateAdditionCost(solution, it, instance) }!!

            solution.add(nextFacility)
            locations.remove(nextFacility)
        }

        return QAPSolution(instance, solution.toIntArray())
    }

    fun calculateAdditionCost(solution: List<Int>, newFacility: Int, instance: QAPInstance): Int {
        var cost = 0
        val newLocation = solution.size

        for (i in solution.indices) {
            cost += instance.getFlow(i, newLocation) * instance.getDistance(solution[i], newFacility)
        }

        return cost
    }
}