package ProblemImplementations.Knapsack

import LocalSearch.AbstrNeighborhoodExplorer
import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.ICostEvaluator
import LocalSearch.IMove
import QAP.Test.FlipMove


class FlipNeighborhoodExplorer(
    costEvaluator: ICostEvaluator<*, *>,
): AbstrNeighborhoodExplorer(costEvaluator) {
    // TODO make this depend on representation size
    override fun generateMoves(solution: ISolutionBase): List<IMove> {
        val moves = mutableListOf<FlipMove>()
        for (i in 0 until solution.instance.instanceSize) {
            moves.add(FlipMove(i))
        }

        return moves.shuffled()
    }

    // TODO make this depend on representation size
    override fun generateRandomMove(solution: ISolutionBase): IMove {
        val i = (0 until solution.instance.instanceSize).random()
        return FlipMove(i)
    }

    override fun getName(): String {
        return "FlipNH"
    }
}