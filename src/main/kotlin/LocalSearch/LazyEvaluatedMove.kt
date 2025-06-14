package LocalSearch

import QAP.QAPSolution


class LazyEvaluatedMove(
    val move: IMove,
    private val deltaProvider: () -> Int,
    private val evaluationsCounter: EvaluationsCounter,
) {
    val delta: Int by lazy {
        evaluationsCounter.evaluations++
        deltaProvider() // .also { move.delta = it } TODO Rethink if the IMove should stay clean
    }

    fun applyTo(solution: ISolution): ISolution {
        return move.applyToWithDelta(solution, delta)
    }
}