package LocalSearch

import LocalSearch.BaseInterfaces.ICostEvaluatorBase
import LocalSearch.BaseInterfaces.ISolutionBase

abstract class AbstrNeighborhoodExplorer(
    val costEvaluator: ICostEvaluator<*, *>
): INeighborhoodExplorer {
    //TODO FIX ERROR CATCHING

    final override fun generateLazyMoves(
        solution: ISolutionBase,
        objective: IObjective,
        counter: EvaluationsCounter
    ): List<LazyEvaluatedMove> {
        return when (val result = generateLazy(solution, counter, objective) { generateMoves(solution) }) {
            is LazyMoveResult.Multiple -> result.moves
            is LazyMoveResult.Single -> listOf(result.move)
        }
    }

    final override fun generateLazyRandomMove(
        solution: ISolutionBase,
        objective: IObjective,
        counter: EvaluationsCounter
    ): LazyEvaluatedMove {
        return when (val result = generateLazy(solution, counter, objective) { generateRandomMove(solution) }) {
            is LazyMoveResult.Single -> result.move
            is LazyMoveResult.Multiple -> result.moves.firstOrNull()
                ?: throw IllegalStateException("Expected at least one move from generateRandomMove")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun generateLazy(
        solution: ISolutionBase,
        counter: EvaluationsCounter,
        objective: IObjective?,
        moveGenerator: () -> Any
    ): LazyMoveResult {
        return try {
            val moves = moveGenerator()
            val typedEvaluator = costEvaluator as ICostEvaluator<ISolutionBase, IMove>

            when (moves) {
                is IMove -> LazyMoveResult.Single(
                    createObjectiveAwareLazyMove(moves, solution, typedEvaluator, counter, objective)
                )

                is List<*> -> {
                    val lazyEvaluatedMoves = moves.map {
                        val move = it as IMove
                        createObjectiveAwareLazyMove(move, solution, typedEvaluator, counter, objective)
                    }
                    LazyMoveResult.Multiple(lazyEvaluatedMoves)
                }

                else -> throw IllegalArgumentException(
                    "Unsupported move type: ${moves::class.simpleName}"
                )
            }
        } catch (e: ClassCastException) {
            throw IllegalStateException(
                "Cost evaluator type mismatch in ${this::class.simpleName}.",
                e
            )
        }
    }

    private fun createObjectiveAwareLazyMove(
        move: IMove,
        solution: ISolutionBase,
        evaluator: ICostEvaluator<ISolutionBase, IMove>,
        counter: EvaluationsCounter,
        objective: IObjective?
    ): LazyEvaluatedMove {
        return if (objective != null) {
            LazyEvaluatedMove.createObjectiveAware(
                move = move,
                actualDeltaProvider = { evaluator.evaluateDelta(solution, move) },
                objectiveDeltaProvider = {
                    val actualDelta = evaluator.evaluateDelta(solution, move)
                    val transformed = objective.optimizationCriterion.transform(
                        solution.solutionCost.toDouble(),
                        actualDelta.toDouble()
                    )
                    (objective.direction * transformed).toInt()
                },
                counter = counter
            )
        } else {
            LazyEvaluatedMove.create(move, {
                evaluator.evaluateDelta(solution, move)
            }, counter)
        }
    }
}