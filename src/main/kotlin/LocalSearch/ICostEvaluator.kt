package LocalSearch

interface ICostEvaluator<Solution, Move> {
    fun evaluateDelta(solution: Solution, move: Move): Int
    fun evaluateSolution(solution: Solution): Int
}