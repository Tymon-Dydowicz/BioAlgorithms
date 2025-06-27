package LocalSearch

sealed interface IObjective {
    val direction: Int
    val optimizationCriterion: IOptimizationCriterion

    fun getName(): String

    class Minimization(override val optimizationCriterion: IOptimizationCriterion = IOptimizationCriterion.NoTransformCriterion()) : IObjective {
        override val direction: Int = 1

        override fun getName(): String = "MinimizationObjective"
    }

    class Maximization(override val optimizationCriterion: IOptimizationCriterion = IOptimizationCriterion.NoTransformCriterion()) :
        IObjective {
        override val direction: Int = -1

        override fun getName(): String = "MaximizationObjective"
    }
}