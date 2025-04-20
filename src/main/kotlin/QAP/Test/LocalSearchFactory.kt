package QAP.Test

import LocalSearch.LocalSearchConfig

object LocalSearchFactory {
    fun createGreedyLocalSearch(maxTime: Long, heuristicInitial: Boolean): LocalSearchConfig {
        return LocalSearchConfig(
            if (heuristicInitial) HeuristicSolutionGenerator() else RandomSolutionGenerator(),
            SwapNeighborhoodExplorer(),
            GreedyAcceptance(),
            TimeBasedStopping(maxTime),
        )
    }


    fun createSteepestLocalSearch(maxTime: Long, heuristicInitial: Boolean): LocalSearchConfig {
        return LocalSearchConfig(
            if (heuristicInitial) HeuristicSolutionGenerator() else RandomSolutionGenerator(),
            SwapNeighborhoodExplorer(),
            SteepestAcceptance(),
            TimeBasedStopping(maxTime),
        )
    }
}