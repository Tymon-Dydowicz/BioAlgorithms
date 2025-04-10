package QAP

class QAPSolution(val instance: QAPInstance, val solution: IntArray) {
    var solutionCost: Int

    init {
        this.solutionCost = QAPSolutionManager.calculateSolutionCost(instance, solution)
    }

    fun describe() {
        println("Solution: " + solution.joinToString(" "))
        println("Solution cost: $solutionCost \n")
    }

    fun overrideCost(newCost: Int) {
        this.solutionCost = newCost
    }

    fun swapElements(i: Int, j: Int) : QAPSolution{
        val newSolution = solution.copyOf()
        newSolution[i] = solution[j]
        newSolution[j] = solution[i]

        return QAPSolution(instance, newSolution)
    }
}