package QAP.Test

import LocalSearch.IMove
import QAP.QAPSolution

class SwapMove(val i: Int, val j: Int) : IMove {
    override fun extractFeature(featureType: String): String {
        return when (featureType) {
            "default" -> "$i-$j"
            "first" -> "$i-*"
            "second" -> "*-$j"
            "ordered" -> "${minOf(i, j)}-${maxOf(i, j)}"
            else -> throw IllegalArgumentException("Unknown feature type: $featureType")
        }
    }

    override fun getAvailableFeatureTypes(): Set<String> {
        return setOf("default", "first", "second", "ordered")
    }

    override fun applyToWithDelta(solution: QAPSolution, delta: Int): QAPSolution {
        val newSolution = solution.solution.copyOf()
        newSolution[i] = solution.solution[j]
        newSolution[j] = solution.solution[i]
        return QAPSolution(solution.instance, newSolution, solution.solutionCost + delta)
    }

    override fun toString(): String {
        return "Swap($i,$j)"
    }
}