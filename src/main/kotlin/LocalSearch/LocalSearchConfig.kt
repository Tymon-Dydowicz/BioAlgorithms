package LocalSearch

import Enums.AlgorithmType
import QAP.TabuSearch.ICandidateSelector
import QAP.Test.*

class LocalSearchConfig private constructor(
    val solutionGenerator: ISolutionGenerator<*, *>, //TODO Think about builder pattern for safety
    val neighborhoodExplorer: AbstrNeighborhoodExplorer,
    val acceptanceCriterionProvider: CloneCriterionProvider,
    val stoppingCriterion: IStoppingCriterion,
    val candidateSelector: ICandidateSelector = ICandidateSelector.All(),
    val perturbation: IPerturbation = IPerturbation.NoPerturbation(),
    private val acceptanceCriterion: IAcceptanceCriterion,
) {
    constructor(
        solutionGenerator: ISolutionGenerator<*, *>, //TODO Think about builder pattern for safety
        neighborhoodExplorer: AbstrNeighborhoodExplorer,
        acceptanceCriterion: IAcceptanceCriterion,
        stoppingCriterion: IStoppingCriterion,
        candidateSelector: ICandidateSelector = ICandidateSelector.All(),
        perturbation: IPerturbation = IPerturbation.NoPerturbation(),
    ) : this(
        solutionGenerator,
        neighborhoodExplorer,
        CloneCriterionProvider(acceptanceCriterion),
        stoppingCriterion,
        candidateSelector,
        perturbation,
        acceptanceCriterion,
    )

    fun createAlgorithm(): AbstrLocalSearchMetaheuristic {
        return object : AbstrLocalSearchMetaheuristic(
            solutionGenerator,
            neighborhoodExplorer,
            acceptanceCriterionProvider.create(),
            stoppingCriterion,
            candidateSelector,
            perturbation,
        ) {}
    }

    val algorithmType: AlgorithmType
        get() {
            // Map the combination of components to legacy algorithm types
            // This is a simplified example - you'd need to map all combinations
            return when {
//            solutionGenerator is RandomSolutionGenerator &&
//                    acceptanceCriterion is GreedyAcceptance -> AlgorithmType.RANDOM_GREEDY_LOCAL_SEARCH
//
//            solutionGenerator is RandomSolutionGenerator &&
//                    acceptanceCriterion is SteepestAcceptance -> AlgorithmType.RANDOM_STEEPEST_LOCAL_SEARCH
//
//            solutionGenerator is HeuristicSolutionGenerator &&
//                    acceptanceCriterion is GreedyAcceptance -> AlgorithmType.HEURISTIC_GREEDY_LOCAL_SEARCH
//
//            solutionGenerator is HeuristicSolutionGenerator &&
//                    acceptanceCriterion is SteepestAcceptance -> AlgorithmType.HEURISTIC_STEEPEST_LOCAL_SEARCH

                acceptanceCriterion is SimulatedAnnealingAcceptance -> AlgorithmType.SIMULATED_ANNEALING
                acceptanceCriterion is TabuSearchAcceptance -> AlgorithmType.TABU_SEARCH

                // Uncomment and extend as needed:
                // acceptanceCriterion is SimulatedAnnealingAcceptance -> AlgorithmType.SIMULATED_ANNEALING
                // acceptanceCriterion is TabuAcceptance -> AlgorithmType.TABU_SEARCH

                else -> AlgorithmType.CUSTOM
            }
        }
}