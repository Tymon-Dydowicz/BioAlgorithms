package QAP

import Enums.AlgorithmType
import LocalSearch.*
import QAP.QAPInstance
import QAP.Test.GreedyAcceptance
import QAP.Test.HeuristicSolutionGenerator
import QAP.Test.RandomSolutionGenerator
import QAP.Test.SteepestAcceptance
import java.util.Date

data class OptimizationConfig(
    val instance: QAPInstance,
    // Replace algorithmType with components
    val localSearchConfig: LocalSearchConfig,
    val time: Long,
    val algorithmRuns: Int = 1,
    val multiStarts: Int? = null,
    var aggregateMultiStarts: Boolean = true,
    val timeStamp: Date = Date(),
    val executions: MutableList<Date> = mutableListOf(),
) {
    // Helper function to create an algorithm instance

    val algorithmType: AlgorithmType get() {
        // Map the combination of components to legacy algorithm types
        // This is a simplified example - you'd need to map all combinations
        val baseType = localSearchConfig.algorithmType

        return if (multiStarts != null && multiStarts > 1) {
            when (baseType) {
                AlgorithmType.RANDOM_GREEDY_LOCAL_SEARCH -> AlgorithmType.RANDOM_GREEDY_MS_LOCAL_SEARCH
                AlgorithmType.RANDOM_STEEPEST_LOCAL_SEARCH -> AlgorithmType.RANDOM_STEEPEST_MS_LOCAL_SEARCH
                AlgorithmType.HEURISTIC_GREEDY_LOCAL_SEARCH -> AlgorithmType.HEURISTIC_GREEDY_MS_LOCAL_SEARCH
                AlgorithmType.HEURISTIC_STEEPEST_LOCAL_SEARCH -> AlgorithmType.HEURISTIC_STEEPEST_MS_LOCAL_SEARCH
                else -> AlgorithmType.CUSTOM
            }
        } else {
            baseType
        }
    }
}
