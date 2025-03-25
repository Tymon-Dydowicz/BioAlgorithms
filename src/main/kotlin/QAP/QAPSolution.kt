package QAP

class QAPSolution(val instance: QAPInstance, val solution: IntArray) {
    val solutionCost: Int

    init {
        this.solutionCost = calculateSolutionCost(solution)
    }

    fun calculateSolutionCost(solution: IntArray): Int {
        var cost = 0
        for (i in 0 until instance.instanceSize) {
            for (j in 0 until instance.instanceSize) {
                cost += instance.getFlow(i, j) * instance.getDistance(solution[i], solution[j])
            }
        }
        return cost
    }

    fun describe() {
        println("Solution: " + solution.joinToString(" "))
        println("Solution cost: $solutionCost")
    }
}