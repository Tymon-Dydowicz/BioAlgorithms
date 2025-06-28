package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase


class LazyEvaluatedMove private constructor(
    val move: IMove,
    private val actualDeltaProvider: () -> Int,
    private val objectiveDeltaProvider: (() -> Int)?,
    val evaluationsCounter: EvaluationsCounter
) {
    private var _actualDelta: Int? = null
    private var _objectiveDelta: Int? = null
    private var _evaluated: Boolean = false

    val actualDelta: Int
        get() {
            if (!_evaluated) evaluate()
            return _actualDelta!!
        }

    val objectiveDelta: Int?
        get() {
            if (!_evaluated) evaluate()
            return _objectiveDelta
        }

    val delta: Int
        get() = objectiveDelta ?: actualDelta

    private fun evaluate() {
        if (!_evaluated) {
            evaluationsCounter.evaluations++
            _actualDelta = actualDeltaProvider()
            _objectiveDelta = objectiveDeltaProvider?.invoke()
            _evaluated = true
        }
    }

    fun applyTo(solution: ISolutionBase): ISolutionBase {
        return move.applyToWithDelta(solution, actualDelta)
    }

    companion object {
        fun create(
            move: IMove,
            deltaProvider: () -> Int,
            counter: EvaluationsCounter
        ): LazyEvaluatedMove = LazyEvaluatedMove(
            move = move,
            actualDeltaProvider = deltaProvider,
            objectiveDeltaProvider = null,
            evaluationsCounter = counter
        )

        fun createObjectiveAware(
            move: IMove,
            actualDeltaProvider: () -> Int,
            objectiveDeltaProvider: () -> Int,
            counter: EvaluationsCounter
        ): LazyEvaluatedMove = LazyEvaluatedMove(
            move = move,
            actualDeltaProvider = actualDeltaProvider,
            objectiveDeltaProvider = objectiveDeltaProvider,
            evaluationsCounter = counter
        )
    }
}