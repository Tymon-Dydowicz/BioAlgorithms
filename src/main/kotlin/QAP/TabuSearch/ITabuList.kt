package QAP.TabuSearch

import LocalSearch.IMove

interface ITabuList {
    fun isTabu(move: IMove, currentIteration: Int): Boolean
    fun addMove(move: IMove, currentIteration: Int, tenure: Int)
    fun cleanExpired(currentIteration: Int)
    fun clear()
    fun getName(): String
    fun getTabuListSize(): Int
    fun getTabuList(): List<Pair<String, Int>>

    class AttributeBasedTabuList(private val featureExtractor: IMoveFeatureExtractor) : ITabuList {
        private val tabuMoves = HashMap<String, Int>()

        override fun isTabu(move: IMove, currentIteration: Int): Boolean {
            val feature = featureExtractor.extractFeature(move)
            val expirationIteration = tabuMoves[feature] ?: return false
            return currentIteration < expirationIteration
        }

        override fun addMove(move: IMove, currentIteration: Int, tenure: Int) {
            val feature = featureExtractor.extractFeature(move)
            tabuMoves[feature] = currentIteration + tenure
        }

        override fun cleanExpired(currentIteration: Int) {
            tabuMoves.entries.removeIf { (_, expiration) -> expiration <= currentIteration }
        }

        override fun clear() {
            tabuMoves.clear()
        }

        override fun getName(): String {
            return "AttributeTabuList(${featureExtractor.getName()})"
        }

        override fun getTabuListSize(): Int {
            return tabuMoves.size
        }

        override fun getTabuList(): List<Pair<String, Int>> {
            return tabuMoves.map { (feature, expiration) -> Pair(feature, expiration) }
        }
    }
}