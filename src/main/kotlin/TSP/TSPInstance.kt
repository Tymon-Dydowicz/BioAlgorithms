package TSP

import LocalSearch.AbstrProblemInstance

class TSPInstance(
    instanceSize: Int,
    val distances: Array<IntArray>,
    instanceName: String? = null
) : AbstrProblemInstance(instanceSize, instanceName){

    fun getDistance(i: Int, j: Int): Int {
        return distances[i][j]
    }

    override fun describe() {
        println("TSP Instance: $instanceName")
        println("Number of cities: $instanceSize")
        println("Distance matrix:")
        for (i in 0 until instanceSize) {
            println(distances[i].joinToString(", "))
        }
        if (optimalSolution != null) {
            println("Optimal solution: ${optimalSolution!!.solution.joinToString(", ")}")
            println("Optimal cost: ${optimalSolution!!.solutionCost}")
        } else {
            println("No optimal solution defined.")
        }
    }
}