package LocalSearch

import QAP.QAPSolution

interface IMove {
    fun extractFeature(featureType: String = "default"): String
    fun getAvailableFeatureTypes(): Set<String>
    fun applyToWithDelta(solution: QAPSolution, delta: Int): QAPSolution
}