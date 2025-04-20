package QAP.TabuSearch

import LocalSearch.LocalSearchState
import java.lang.Math.ceil

interface ITabuTenureSchedule {
    fun calculateTenure(state: LocalSearchState): Int
    fun notifyMoveSelected(delta: Int, isImproving: Boolean)
    fun getName(): String

    class StaticTenure(private val tenure: Int) : ITabuTenureSchedule {
        override fun calculateTenure(state: LocalSearchState): Int {
            return tenure
        }

        override fun notifyMoveSelected(delta: Int, isImproving: Boolean) {
            // No action needed for static tenure
        }

        override fun getName(): String {
            return "StaticTenure"
        }
    }

    class SizeBasedTenure(neighborhoodSize: Int, factor: Double = 0.08) : ITabuTenureSchedule {
        private var tenure = (neighborhoodSize * factor).toInt()

        override fun calculateTenure(state: LocalSearchState): Int {
            return tenure
        }

        override fun notifyMoveSelected(delta: Int, isImproving: Boolean) {
            // No action needed for size-based tenure
        }

        override fun getName(): String {
            return "SizeBasedTenure"
        }
    }
}