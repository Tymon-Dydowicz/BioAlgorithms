package QAP

import LocalSearch.AbstrProblemInstance

class QAPInstance(
    instanceSize: Int,
    val flows: Array<IntArray>,
    val distances: Array<IntArray>,
    instanceName: String? = null
) : AbstrProblemInstance(instanceSize, instanceName) {

    init {
        verifyInstance()
    }

    fun getDistance(i: Int, j: Int): Int {
        return distances[i][j]
    }

    fun getFlow(i: Int, j: Int): Int {
        return flows[i][j]
    }

    override fun describe() {
        println("Instance size: $instanceSize")
        println("Flows:")
        for (i in 0 until instanceSize) {
            println(flows[i].joinToString(" "))
        }
        println("Distances:")
        for (i in 0 until instanceSize) {
            println(distances[i].joinToString(" "))
        }
        optimalSolution?.describe()
        println("")
    }

    override fun verifyInstance() {
        if (flows.size != instanceSize || distances.size != instanceSize) {
            throw IllegalArgumentException("Instance size does not match the size of the matrices")
        }
        for (i in 0 until instanceSize) {
            if (flows[i].size != instanceSize || distances[i].size != instanceSize) {
                throw IllegalArgumentException("Instance size does not match the size of the matrices")
            }
        }
    }
}