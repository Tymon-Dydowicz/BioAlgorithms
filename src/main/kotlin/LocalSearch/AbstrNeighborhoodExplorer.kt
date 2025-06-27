package LocalSearch


abstract class AbstrNeighborhoodExplorer(
    val costEvaluator: Any
): INeighborhoodExplorer {

    final override fun generateLazyMoves(
        solution: ISolution,
        objective: IObjective,
        counter: EvaluationsCounter
    ): List<LazyEvaluatedMove> {
        return when (val result = generateLazy(solution, counter, objective) { generateMoves(solution) }) {
            is LazyMoveResult.Multiple -> result.moves
            is LazyMoveResult.Single -> listOf(result.move)
        }
    }

    final override fun generateLazyRandomMove(
        solution: ISolution,
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
        solution: ISolution,
        counter: EvaluationsCounter,
        objective: IObjective?,
        moveGenerator: () -> Any
    ): LazyMoveResult {
        return try {
            val result = moveGenerator()
            val typedEvaluator = costEvaluator as ICostEvaluator<ISolution, IMove>

            when (result) {
                is IMove -> LazyMoveResult.Single(
                    createObjectiveAwareLazyMove(result, solution, typedEvaluator, counter, objective)
                )

                is List<*> -> {
                    val moves = result.map {
                        val move = it as IMove
                        createObjectiveAwareLazyMove(move, solution, typedEvaluator, counter, objective)
                    }
                    LazyMoveResult.Multiple(moves)
                }

                else -> throw IllegalArgumentException(
                    "Unsupported move type: ${result::class.simpleName}"
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
        solution: ISolution,
        evaluator: ICostEvaluator<ISolution, IMove>,
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