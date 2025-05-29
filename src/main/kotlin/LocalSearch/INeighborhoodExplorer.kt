package LocalSearch

import QAP.QAPSolution

interface INeighborhoodExplorer {
//    fun generateNeighbors(solution: QAPSolution): List<QAPSolution>
    fun generateMoves(solution: QAPSolution): List<IMove>
    fun generateLazyMoves(solution: QAPSolution, counter: EvaluationsCounter): List<LazyEvaluatedMove>
    fun generateRandomMove(solution: QAPSolution): IMove
    fun calculateDelta(solution: QAPSolution, move: IMove): Int
    fun getName(): String
}