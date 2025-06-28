package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase

object RepresentationHelper {
    inline fun <reified T> getSolutionArray(solution: ISolutionBase): T {
        return when (val raw = solution.representationBase.getRawData()) {
            is T -> raw
            else -> throw IllegalArgumentException("Expected ${T::class.simpleName}, got ${raw::class.simpleName}")
        }
    }

    fun getIntArray(solution: ISolutionBase): IntArray {
        return getSolutionArray<IntArray>(solution)
    }

    fun getBooleanArray(solution: ISolutionBase): BooleanArray {
        return getSolutionArray<BooleanArray>(solution)
    }
}