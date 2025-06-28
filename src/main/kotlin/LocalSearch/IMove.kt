package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.Representations.ISolution

interface IMove {
    fun extractFeature(featureType: String = "default"): String
    fun getAvailableFeatureTypes(): Set<String>
    fun applyToWithDelta(solution: ISolutionBase, delta: Int): ISolutionBase
}