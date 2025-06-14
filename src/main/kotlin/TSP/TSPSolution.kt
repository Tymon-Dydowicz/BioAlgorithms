package TSP

import LocalSearch.IProblemInstance
import LocalSearch.ISolution

class TSPSolution(
    override val instance: TSPInstance,
    override val solution: IntArray,
    providedCost: Int? = null
) : ISolution {
    override val solutionCost: Int = providedCost ?: calculateTotalCost()

    override fun describe() {
        println("Solution: " + solution.joinToString(" "))
        println("Solution cost: $solutionCost \n")
    }

    override fun copyWith(newRepresentation: IntArray, delta: Int): ISolution {
        return TSPSolution(instance, newRepresentation, solutionCost + delta)
    }

    private fun calculateTotalCost(): Int {
        var totalCost = 0
        for (i in solution.indices) {
            val nextIndex = (i + 1) % solution.size
            totalCost += instance.getDistance(solution[i], solution[nextIndex])
        }
        return totalCost
    }
}