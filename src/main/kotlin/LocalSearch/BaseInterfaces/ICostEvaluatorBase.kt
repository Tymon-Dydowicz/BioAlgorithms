package LocalSearch.BaseInterfaces

import LocalSearch.IMove

interface ICostEvaluatorBase {
    fun evaluateSolution(solution: ISolutionBase): Int
    fun evaluateMove(solution: ISolutionBase, move: IMove): Int
    fun getName(): String
}