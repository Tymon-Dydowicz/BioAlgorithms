package ProblemImplementations.Knapsack

import LocalSearch.ICostEvaluator
import LocalSearch.Representations.BinaryVectorRepresentation
import QAP.Test.FlipMove

class KnapsackCostEvaluator : ICostEvaluator<KnapsackSolution, FlipMove> {
    override fun evaluateDelta(solution: KnapsackSolution, move: FlipMove): Int {
        val selection = solution.representation.retrieveData()
        val index = move.index
        val instance = solution.instance

        val newRepresentation = selection.toMutableList()
        newRepresentation[index] = !newRepresentation[index]

        var newWeight = 0
        for (i in newRepresentation.indices) {
            if (newRepresentation[i]) {
                newWeight += instance.weights[i]
            }
        }

        if (newWeight > instance.capacity) {
            return Int.MIN_VALUE
        }

        val newSolution = KnapsackSolution(instance, BinaryVectorRepresentation(newRepresentation.toBooleanArray()))
        return newSolution.solutionCost - solution.solutionCost
    }

    override fun evaluateSolution(solution: KnapsackSolution): Int {
        val selection = solution.representation.retrieveData()
        val totalValue = selection.indices.sumOf { i ->
            if (selection[i]) solution.instance.getValue(i) else 0
        }
        return -totalValue
    }
}