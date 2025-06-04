package QAP.Test

import LocalSearch.*
import QAP.QAPSolution

class SwapNeighborhoodExplorer(
    costEvaluator: ICostEvaluator<*, *>,
): AbstrNeighborhoodExplorer(costEvaluator) {
    override fun generateMoves(solution: ISolution): List<IMove> {
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

    override fun generateRandomMove(solution: ISolution): IMove {
        val i = (0 until solution.instance.instanceSize).random()
        val j = (0 until solution.instance.instanceSize).random()
        return SwapMove(i, j)
    }

    override fun getName(): String {
        return "SwapNH"
    }
}