package QAP

class QAPSolution(val instance: QAPInstance, val solution: IntArray) {
    val solutionCost: Int

    init {
        // TODO Consider changing this so that there is a possiblity of using deltas instead of recalculating whole cost
        this.solutionCost = QAPSolutionManager.calculateSolutionCost(instance, solution)
    }

    fun describe() {
        println("Solution: " + solution.joinToString(" "))
        println("Solution cost: $solutionCost \n")
    }
}