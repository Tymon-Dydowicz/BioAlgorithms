package QAP.TabuSearch

import LocalSearch.IMove
import LocalSearch.LocalSearchState

interface IAspirationCriterion {
    fun satisfies(move: IMove, delta: Int, state: LocalSearchState): Boolean
    fun getName(): String

    class BestMove : IAspirationCriterion {
        override fun satisfies(move: IMove, delta: Int, state: LocalSearchState): Boolean {
            return state.currentSolution.solutionCost + delta < state.bestSolution.solutionCost
        }

        override fun getName(): String {
            return "BestMove"
        }
    }
}