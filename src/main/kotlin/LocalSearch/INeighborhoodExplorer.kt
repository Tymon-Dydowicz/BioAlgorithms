package LocalSearch

import QAP.QAPSolution

interface INeighborhoodExplorer {
//    fun generateNeighbors(solution: QAPSolution): List<QAPSolution>
    fun generateMoves(solution: ISolution): List<IMove>
    fun generateLazyMoves(solution: ISolution, counter: EvaluationsCounter): List<LazyEvaluatedMove>
    fun generateRandomMove(solution: ISolution): IMove
    fun generateLazyRandomMove(solution: ISolution, counter: EvaluationsCounter = EvaluationsCounter()): LazyEvaluatedMove
//    fun calculateDelta(solution: ISolution, move: IMove): Int
    fun getName(): String
}