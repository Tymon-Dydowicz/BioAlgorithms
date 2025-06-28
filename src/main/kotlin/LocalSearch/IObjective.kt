package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase

sealed interface IObjective {
    val direction: Int
    val optimizationCriterion: IOptimizationCriterion

    fun getName(): String

    fun isBetter(bestSolution: ISolutionBase, newSolution: ISolutionBase): Boolean {
        val delta = optimizationCriterion.transform((bestSolution.solutionCost).toDouble(), (newSolution.solutionCost - bestSolution.solutionCost).toDouble())
        return direction*delta < 0.0
    }

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