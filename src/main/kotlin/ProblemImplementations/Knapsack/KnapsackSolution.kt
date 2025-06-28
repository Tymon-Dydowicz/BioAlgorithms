package ProblemImplementations.Knapsack

import LocalSearch.Representations.AbstrBinaryVectorSolution
import LocalSearch.Representations.BinaryVectorRepresentation

class KnapsackSolution(
    override val instance: KnapsackInstance,
    override val representation: BinaryVectorRepresentation,
    providedCost: Int? = null
) : AbstrBinaryVectorSolution() {
    override val solutionCost: Int = providedCost ?: calculateTotalValue()

    override fun describe() {
        representation.describe()
        println("Solution value: $solutionCost")
        println("Solution weight: ${calculateTotalWeight()}")
        println("Capacity utilization: ${calculateTotalWeight()}/${instance.capacity}")
        println("Valid: ${isValid()}\n")
    }

    override fun copyWith(newRepresentation: BinaryVectorRepresentation, delta: Int): KnapsackSolution {
        return KnapsackSolution(instance, newRepresentation, solutionCost + delta)
    }

    private fun calculateTotalValue(): Int {
        val selection = representation.retrieveData()
        return selection.indices.sumOf { i ->
            if (selection[i]) instance.getValue(i) else 0
        }
    }

    private fun calculateTotalWeight(): Int {
        val selection = representation.retrieveData()
        return selection.indices.sumOf { i ->
            if (selection[i]) instance.getWeight(i) else 0
        }
    }

    fun isValid(): Boolean {
        return calculateTotalWeight() <= instance.capacity
    }
}