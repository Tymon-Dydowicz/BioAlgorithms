package QAP.Test

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.IMove
import LocalSearch.RepresentationHelper


class FlipMove(val index: Int) : IMove {
    override fun extractFeature(featureType: String): String {
        return when (featureType) {
            "default" -> "$index"
            "position" -> "$index"
            "bit" -> "*"  // Generic bit flip feature
            "parity" -> if (index % 2 == 0) "even" else "odd"  // Even/odd position feature
            else -> throw IllegalArgumentException("Unknown feature type: $featureType")
        }
    }

    override fun getAvailableFeatureTypes(): Set<String> {
        return setOf("default", "position", "bit", "parity")
    }

    override fun applyToWithDelta(solution: ISolutionBase, delta: Int): ISolutionBase {
        // Use helper to safely extract the representation
        val currentArray = RepresentationHelper.getBooleanArray(solution)
        val newArray = currentArray.copyOf()

        // Perform the flip
        newArray[index] = !newArray[index]

        return solution.copyWithRaw(newArray, delta)
    }

    override fun toString(): String {
        return "Flip($index)"
    }
}