package QAP

import LocalSearch.IProblemInstance
import LocalSearch.ISolution

class QAPSolution (
    override val instance: QAPInstance,
    override val solution: IntArray,
    providedCost: Int? = null
) : ISolution {
    override val solutionCost: Int = providedCost ?: QAPSolutionManager.calculateSolutionCost(instance, solution)

    override fun describe() {
        println("Solution: " + solution.joinToString(" "))
        println("Solution cost: $solutionCost \n")
    }

    override fun copyWith(newRepresentation: IntArray, delta: Int): ISolution {
        return QAPSolution(instance, newRepresentation, solutionCost + delta)
    }

    fun swapElements(i: Int, j: Int) : QAPSolution{
        val newSolution = solution.copyOf()
        newSolution[i] = solution[j]
        newSolution[j] = solution[i]

        return QAPSolution(instance, newSolution)
    }
}