package ProblemImplementations.TSP

import LocalSearch.Representations.AbstrPermutationSolution
import LocalSearch.Representations.PermutationRepresentation

class TSPSolution(
    override val instance: TSPInstance,
    override val representation: PermutationRepresentation,
    providedCost: Int? = null
) : AbstrPermutationSolution() {
    override val solutionCost: Int = providedCost ?: calculateTotalCost()

    override fun describe() {
        representation.describe()
        println("Solution cost: $solutionCost \n")
    }

    override fun copyWith(newRepresentation: PermutationRepresentation, delta: Int): TSPSolution {
        return TSPSolution(instance, newRepresentation, solutionCost + delta)
    }

    private fun calculateTotalCost(): Int {
        var totalCost = 0
        val permutation = representation.retrieveData()
        for (i in permutation.indices) {
            val nextIndex = (i + 1) % permutation.size
            totalCost += instance.getDistance(permutation[i], permutation[nextIndex])
        }
        return totalCost
    }
}