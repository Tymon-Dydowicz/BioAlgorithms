package QAP

class QAPInstance {
    val instanceName: String
    val instanceSize: Int
    val distances: Array<IntArray>
    val flows: Array<IntArray>
    var optimalSolution: QAPSolution? = null

    constructor(instanceName: String, instanceSize: Int, flows: Array<IntArray>, distances: Array<IntArray>) {
        this.instanceName = instanceName
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