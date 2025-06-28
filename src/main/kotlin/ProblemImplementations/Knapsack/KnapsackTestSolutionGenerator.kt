package ProblemImplementations.Knapsack

import LocalSearch.AbstrSolutionGenerator
import LocalSearch.IProblemInstance
import LocalSearch.Representations.AbstrBinaryVectorSolution
import LocalSearch.Representations.BinaryVectorRepresentation

class KnapsackTestSolutionGenerator<ProblemT : IProblemInstance, SolutionT : AbstrBinaryVectorSolution>(
    constructor: (ProblemT, BinaryVectorRepresentation) -> SolutionT
) : AbstrSolutionGenerator<ProblemT, SolutionT, BinaryVectorRepresentation>(constructor) {

    override fun generateTest(instance: ProblemT): BinaryVectorRepresentation {
        require(instance is KnapsackInstance) { "Instance must be a KnapsackInstance" }

        val knapsackInstance = instance as KnapsackInstance
        val selection = BooleanArray(knapsackInstance.instanceSize) { false }

        val items = (0 until knapsackInstance.instanceSize).map { i ->
            Triple(i, knapsackInstance.getValue(i), knapsackInstance.getWeight(i))
        }.filter { it.third > 0 }

        val shuffledItems = items.shuffled()

        var currentWeight = 0

        for ((index, value, weight) in shuffledItems) {
            if (currentWeight + weight <= knapsackInstance.capacity) {
                selection[index] = true
                currentWeight += weight
            }
        }
        val selectionAllZeroes = BooleanArray(knapsackInstance.instanceSize) { false }
        return BinaryVectorRepresentation(selectionAllZeroes)
    }

    override fun getName(): String {
        return "Random Feasible"
    }
}