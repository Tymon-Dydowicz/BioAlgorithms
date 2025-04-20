package QAP.TabuSearch

import LocalSearch.IMove

interface IMoveFeatureExtractor {
    fun extractFeature(move: IMove): String
    fun getName(): String

    class Default : IMoveFeatureExtractor {
        override fun extractFeature(move: IMove): String {
            return move.extractFeature("default")
        }

        override fun getName(): String {
            return "DefaultAttribute"
        }
    }
}