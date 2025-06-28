package QAP.Test

import LocalSearch.*
import LocalSearch.BaseInterfaces.ICostEvaluatorBase
import LocalSearch.BaseInterfaces.ISolutionBase

class SwapNeighborhoodExplorer(
    costEvaluator: ICostEvaluator<*, *>,
): AbstrNeighborhoodExplorer(costEvaluator) {
    // TODO make this depend on representation size
    override fun generateMoves(solution: ISolutionBase): List<IMove> {
        val moves = mutableListOf<SwapMove>()
        for (i in 0 until solution.instance.instanceSize) {
            for (j in 0 until solution.instance.instanceSize) {
                if (i != j) {
                    moves.add(SwapMove(i, j))
                }
            }
        }

        return moves.shuffled()
    }

    // TODO make this depend on representation size
    override fun generateRandomMove(solution: ISolutionBase): IMove {
        val i = (0 until solution.instance.instanceSize).random()
        val j = (0 until solution.instance.instanceSize).random()
        return SwapMove(i, j)
    }

    override fun getName(): String {
        return "SwapNH"
    }
}