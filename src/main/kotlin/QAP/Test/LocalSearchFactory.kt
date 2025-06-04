package QAP.Test

import LocalSearch.IStoppingCriterion
import LocalSearch.LocalSearchConfig
import QAP.QAPCostEvaluator

object LocalSearchFactory {
    fun createGreedyLocalSearch(maxTime: Long, heuristicInitial: Boolean): LocalSearchConfig {
        return LocalSearchConfig(
            if (heuristicInitial) HeuristicSolutionGenerator() else RandomSolutionGenerator(),
            SwapNeighborhoodExplorer(QAPCostEvaluator()),
            GreedyAcceptance(),
            IStoppingCriterion.maxRuntimeMs(maxTime),
        )
    }


    fun createSteepestLocalSearch(maxTime: Long, heuristicInitial: Boolean): LocalSearchConfig {
        return LocalSearchConfig(
            if (heuristicInitial) HeuristicSolutionGenerator() else RandomSolutionGenerator(),
            SwapNeighborhoodExplorer(QAPCostEvaluator()),
            SteepestAcceptance(),
            IStoppingCriterion.maxRuntimeMs(maxTime),
        )
    }
}