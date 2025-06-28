package ProblemImplementations.Knapsack

import LocalSearch.AbstrProblemInstance
import LocalSearch.Representations.AbstrBinaryVectorSolution

class KnapsackInstance(
    instanceSize: Int,
    val weights: IntArray,
    val values: IntArray,
    val capacity: Int,
    instanceName: String? = null
) : AbstrProblemInstance(instanceSize, instanceName) {

    init {
        require(weights.size == instanceSize) { "Weights array size must match instance size" }
        require(values.size == instanceSize) { "Values array size must match instance size" }
        require(capacity > 0) { "Capacity must be positive" }
    }

    fun getWeight(itemIndex: Int): Int = weights[itemIndex]
    fun getValue(itemIndex: Int): Int = values[itemIndex]

    override fun describe() {
        println("Knapsack Instance: $instanceName")
        println("Number of items: $instanceSize")
        println("Capacity: $capacity")
        println("Items (Index: Weight, Value):")
        for (i in 0 until instanceSize) {
            println("  $i: ${weights[i]}, ${values[i]}")
        }
        if (optimalSolution != null) {
            if (optimalSolution!! is AbstrBinaryVectorSolution) {
                val optimalRepresentation = (optimalSolution as AbstrBinaryVectorSolution).representation.retrieveData()
                println("Optimal solution representation: ${optimalRepresentation.joinToString(", ")}")
            }
            println("Optimal cost (negative value): ${optimalSolution!!.solutionCost}")
            println("Optimal actual value: ${-(optimalSolution!!.solutionCost)}")
        } else {
            println("No optimal solution defined.")
        }
    }
}