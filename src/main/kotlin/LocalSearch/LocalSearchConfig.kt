package LocalSearch

import Enums.AlgorithmType
import QAP.Test.GreedyAcceptance
import QAP.Test.HeuristicSolutionGenerator
import QAP.Test.RandomSolutionGenerator
import QAP.Test.SteepestAcceptance

data class LocalSearchConfig(
    val solutionGenerator: ISolutionGenerator,
    val neighborhoodExplorer: INeighborhoodExplorer,
    val IAcceptanceCriterion: IAcceptanceCriterion,
    val stoppingCriterion: IStoppingCriterion,
    val perturbation: IPerturbation = IPerturbation.NoPerturbation(),
) {
    fun createAlgorithm(): AbstrLocalSearchMetaheuristic {
        return object : AbstrLocalSearchMetaheuristic(
            solutionGenerator,
            neighborhoodExplorer,
            IAcceptanceCriterion,
            stoppingCriterion,
            perturbation,
        ) {}
    }

    val algorithmType: AlgorithmType get() {
        // Map the combination of components to legacy algorithm types
        // This is a simplified example - you'd need to map all combinations
        return when {
            solutionGenerator is RandomSolutionGenerator &&
                    IAcceptanceCriterion is GreedyAcceptance -> AlgorithmType.RANDOM_GREEDY_LOCAL_SEARCH

            solutionGenerator is RandomSolutionGenerator &&
                    IAcceptanceCriterion is SteepestAcceptance -> AlgorithmType.RANDOM_STEEPEST_LOCAL_SEARCH

            solutionGenerator is HeuristicSolutionGenerator &&
                    IAcceptanceCriterion is GreedyAcceptance -> AlgorithmType.HEURISTIC_GREEDY_LOCAL_SEARCH

            solutionGenerator is HeuristicSolutionGenerator &&
                    IAcceptanceCriterion is SteepestAcceptance -> AlgorithmType.HEURISTIC_STEEPEST_LOCAL_SEARCH

            // Uncomment and extend as needed:
            // acceptanceCriterion is SimulatedAnnealingAcceptance -> AlgorithmType.SIMULATED_ANNEALING
            // acceptanceCriterion is TabuAcceptance -> AlgorithmType.TABU_SEARCH

            else -> AlgorithmType.CUSTOM
        }
    }
}
