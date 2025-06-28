package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.Representations.ISolution

interface INeighborhoodExplorer {
//    fun generateNeighbors(solution: QAPSolution): List<QAPSolution>
    fun generateMoves(solution: ISolutionBase): List<IMove>
    fun generateLazyMoves(solution: ISolutionBase, objective: IObjective, counter: EvaluationsCounter): List<LazyEvaluatedMove>
    fun generateRandomMove(solution: ISolutionBase): IMove
    fun generateLazyRandomMove(solution: ISolutionBase, objective: IObjective, counter: EvaluationsCounter = EvaluationsCounter()): LazyEvaluatedMove
//    fun calculateDelta(solution: ISolution, move: IMove): Int
    fun getName(): String
}