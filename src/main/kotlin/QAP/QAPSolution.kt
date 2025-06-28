package QAP

import LocalSearch.Representations.AbstrPermutationSolution
import LocalSearch.Representations.PermutationRepresentation

class QAPSolution (
    override val instance: QAPInstance,
    override val representation: PermutationRepresentation,
    providedCost: Int? = null
) : AbstrPermutationSolution() {
    override val solutionCost: Int = providedCost ?: QAPSolutionManager.calculateSolutionCost(instance, representation)

    override fun describe() {
        representation.describe()
        println("Solution cost: $solutionCost \n")
    }

    override fun copyWith(newRepresentation: PermutationRepresentation, delta: Int): QAPSolution {
        return QAPSolution(instance, newRepresentation, solutionCost + delta)
    }

    fun swapElements(i: Int, j: Int) : QAPSolution{
        val newSolution = representation.retrieveData()
        val temp = newSolution[i]
        newSolution[i] = newSolution[j]
        newSolution[j] = temp
        val newRepresentation = PermutationRepresentation(newSolution)

        return QAPSolution(instance, newRepresentation)
    }
}