package QAP.Test

import LocalSearch.IMove

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

    override fun toString(): String {
        return "Swap($i,$j)"
    }
}