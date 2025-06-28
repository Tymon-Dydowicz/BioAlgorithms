package QAP.Test

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.IMove
import LocalSearch.RepresentationHelper
import LocalSearch.Representations.ISolution

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

    override fun applyToWithDelta(solution: ISolutionBase, delta: Int): ISolutionBase {
        val currentArray = RepresentationHelper.getIntArray(solution)
        val newArray = currentArray.copyOf()

        val temp = newArray[i]
        newArray[i] = newArray[j]
        newArray[j] = temp

        return solution.copyWithRaw(newArray, delta)
    }

    override fun toString(): String {
        return "Swap($i,$j)"
    }
}