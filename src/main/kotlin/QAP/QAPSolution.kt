package QAP

import LocalSearch.ISolution

class QAPSolution (
    override val instance: QAPInstance,
    val solution: IntArray,
    providedCost: Int? = null
) : ISolution {
    // TODO Abstract the QAPSolution into a generic solution class
    val solutionCost: Int = providedCost ?: QAPSolutionManager.calculateSolutionCost(instance, solution)

    fun describe() {
        println("Solution: " + solution.joinToString(" "))
        println("Solution cost: $solutionCost \n")
    }


    fun swapElements(i: Int, j: Int) : QAPSolution{
        val newSolution = solution.copyOf()
        newSolution[i] = solution[j]
        newSolution[j] = solution[i]

        return QAPSolution(instance, newSolution)
    }
}