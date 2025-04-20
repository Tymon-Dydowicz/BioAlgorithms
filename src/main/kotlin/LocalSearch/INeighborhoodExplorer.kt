package LocalSearch

import QAP.QAPSolution

interface INeighborhoodExplorer {
//    fun generateNeighbors(solution: QAPSolution): List<QAPSolution>
    fun generateMoves(solution: QAPSolution): List<IMove>
    fun generateRandomMove(solution: QAPSolution): IMove
    fun applyMove(solution: QAPSolution, move: IMove): QAPSolution
    fun calculateDelta(solution: QAPSolution, move: IMove): Int
    fun getName(): String
}