package LocalSearch

interface IOptimizationCriterion {
    fun transform(solutionCost: Double, delta: Double): Double
    fun getName(): String

    class NoTransformCriterion : IOptimizationCriterion {
        override fun transform(solutionCost: Double, delta: Double): Double = delta
        override fun getName(): String = ""
    }

    class TargetOptimizationCriterion(private val targetValue: Double) : IOptimizationCriterion {
        override fun transform(solutionCost: Double, delta: Double): Double {
            val currentDistance = kotlin.math.abs(solutionCost - targetValue)
            val newDistance = kotlin.math.abs(solutionCost + delta - targetValue)
            return newDistance - currentDistance
        }

        override fun getName(): String = "Target($targetValue)"
    }
}