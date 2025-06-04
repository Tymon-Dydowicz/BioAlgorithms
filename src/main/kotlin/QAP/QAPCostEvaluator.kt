package QAP

import LocalSearch.ICostEvaluator
import LocalSearch.IMove
import LocalSearch.ISolution
import QAP.Test.SwapMove

class QAPCostEvaluator : ICostEvaluator<QAPSolution, SwapMove> {
    override fun evaluateDelta(solution: QAPSolution, move: SwapMove): Int {
        val i = move.i
        val j = move.j

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

    override fun evaluateSolution(solution: QAPSolution): Int {
        TODO("Not yet implemented")
    }
}