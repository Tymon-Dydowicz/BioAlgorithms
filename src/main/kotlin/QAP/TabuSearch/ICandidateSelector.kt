package QAP.TabuSearch

import LocalSearch.LazyEvaluatedMove
import LocalSearch.LocalSearchState

interface ICandidateSelector {
    fun selectCandidates(lazyEvaluatedMoves: List<LazyEvaluatedMove>, state: LocalSearchState): List<LazyEvaluatedMove>
    fun getName(): String {
        return this::class.simpleName ?: "UnknownSelector"
    }

    class All : ICandidateSelector {
        override fun selectCandidates(lazyEvaluatedMoves: List<LazyEvaluatedMove>, state: LocalSearchState): List<LazyEvaluatedMove> {
            return lazyEvaluatedMoves
        }

        override fun getName(): String {
            return "DefaultSelector"
        }
    }

    class Elite(private val fraction: Double = 0.1) : ICandidateSelector {
        override fun selectCandidates(lazyEvaluatedMoves: List<LazyEvaluatedMove>, state: LocalSearchState): List<LazyEvaluatedMove> {

            val topK = (state.instance.instanceSize * fraction).toInt().coerceAtLeast(1)
            return lazyEvaluatedMoves.sortedBy { it.delta }.take(topK)
        }

        override fun getName(): String {
            return "EliteSelector(fraction=$fraction)"
        }
    }

    class SampledElite(private val fraction: Double = 0.2) : ICandidateSelector {
        override fun selectCandidates(lazyEvaluatedMoves: List<LazyEvaluatedMove>, state: LocalSearchState): List<LazyEvaluatedMove> {
            val sampleSize = (lazyEvaluatedMoves.size * fraction).toInt().coerceAtLeast(1)
            val eliteSize = (sampleSize * fraction).toInt().coerceAtLeast(1)

            return lazyEvaluatedMoves
                .shuffled()
                .take(sampleSize)
                .sortedBy { it.delta }
                .take(eliteSize)
        }

        override fun getName(): String {
            return "SampledEliteSelector(fraction=$fraction)"
        }
    }
}