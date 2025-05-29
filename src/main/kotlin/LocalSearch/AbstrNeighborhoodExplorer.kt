package LocalSearch

import QAP.QAPSolution

abstract class AbstrNeighborhoodExplorer: INeighborhoodExplorer {
    override fun generateLazyMoves(solution: QAPSolution, counter: EvaluationsCounter): List<LazyEvaluatedMove> {
        return generateMoves(solution).map { move ->
            LazyEvaluatedMove(move, { calculateDelta(solution, move) }, counter)
        }
    }
}