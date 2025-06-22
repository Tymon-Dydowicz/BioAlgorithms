package LocalSearch

sealed class LazyMoveResult {
    data class Single(val move: LazyEvaluatedMove) : LazyMoveResult()
    data class Multiple(val moves: List<LazyEvaluatedMove>) : LazyMoveResult()
}
