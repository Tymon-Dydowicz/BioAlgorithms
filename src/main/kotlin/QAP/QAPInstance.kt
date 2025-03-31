package QAP

class QAPInstance {
    val instanceSize: Int
    val distances: Array<IntArray>
    val flows: Array<IntArray>

    constructor(instanceSize: Int, flows: Array<IntArray>, distances: Array<IntArray>) {
        this.instanceSize = instanceSize
        this.distances = distances
        this.flows = flows

        verifyInstance()
    }

    fun getDistance(i: Int, j: Int): Int {
        return distances[i][j]
    }

    fun getFlow(i: Int, j: Int): Int {
        return flows[i][j]
    }

    fun getFlowWeightedDistance(i: Int, j: Int): Int {
        return getFlow(i, j) * getDistance(i, j)
    }

    fun describe() {
        println("Instance size: $instanceSize")
        println("Flows:")
        for (i in 0 until instanceSize) {
            println(flows[i].joinToString(" "))
        }
        println("Distances:")
        for (i in 0 until instanceSize) {
            println(distances[i].joinToString(" "))
        }
        println("")
    }

    fun verifyInstance() {
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