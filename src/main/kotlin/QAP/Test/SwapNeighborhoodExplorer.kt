package QAP.Test

import LocalSearch.IMove
import LocalSearch.INeighborhoodExplorer
import QAP.QAPSolution

class SwapNeighborhoodExplorer: INeighborhoodExplorer {
    override fun generateMoves(solution: QAPSolution): List<IMove> {
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

    override fun generateRandomMove(solution: QAPSolution): IMove {
        val i = (0 until solution.instance.instanceSize).random()
        val j = (0 until solution.instance.instanceSize).random()
        return SwapMove(i, j)
    }

    override fun applyMove(solution: QAPSolution, move: IMove): QAPSolution {
        val swapMove = move as SwapMove
        val newSolution = solution.solution.copyOf()
        newSolution[swapMove.i] = solution.solution[swapMove.j]
        newSolution[swapMove.j] = solution.solution[swapMove.i]
        return QAPSolution(solution.instance, newSolution)
    }

    override fun calculateDelta(solution: QAPSolution, move: IMove): Int {
        val swapMove = move as SwapMove
        val i = swapMove.i
        val j = swapMove.j

        val distances = solution.instance.distances
        val flows = solution.instance.flows
        val permutation = solution.solution

        var delta = 0

        for (k in permutation.indices) {
            if (k != i && k != j) {
                delta += flows[i][k] * (distances[permutation[j]][permutation[k]] - distances[permutation[i]][permutation[k]])
                delta += flows[j][k] * (distances[permutation[i]][permutation[k]] - distances[permutation[j]][permutation[k]])
                delta += flows[k][i] * (distances[permutation[k]][permutation[j]] - distances[permutation[k]][permutation[i]])
                delta += flows[k][j] * (distances[permutation[k]][permutation[i]] - distances[permutation[k]][permutation[j]])
            }
        }

        delta += flows[i][j] * (distances[permutation[j]][permutation[i]] - distances[permutation[i]][permutation[j]])
        delta += flows[j][i] * (distances[permutation[i]][permutation[j]] - distances[permutation[j]][permutation[i]])

        return delta
    }

    override fun getName(): String {
        return "SwapNH"
    }
}