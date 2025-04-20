package QAP.Test

import LocalSearch.IStoppingCriterion
import LocalSearch.LocalSearchConfig

object LocalSearchFactory {
    fun createGreedyLocalSearch(maxTime: Long, heuristicInitial: Boolean): LocalSearchConfig {
        return LocalSearchConfig(
            if (heuristicInitial) HeuristicSolutionGenerator() else RandomSolutionGenerator(),
            SwapNeighborhoodExplorer(),
            GreedyAcceptance(),
            IStoppingCriterion.maxRuntime(maxTime),
        )
    }


    fun createSteepestLocalSearch(maxTime: Long, heuristicInitial: Boolean): LocalSearchConfig {
        return LocalSearchConfig(
            if (heuristicInitial) HeuristicSolutionGenerator() else RandomSolutionGenerator(),
            SwapNeighborhoodExplorer(),
            SteepestAcceptance(),
            IStoppingCriterion.maxRuntime(maxTime),
        )
    }
}