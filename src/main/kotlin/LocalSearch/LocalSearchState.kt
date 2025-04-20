package LocalSearch

import QAP.QAPInstance
import QAP.QAPSolution

data class LocalSearchState(
    val instance: QAPInstance,
    var currentSolution: QAPSolution,
    var bestSolution: QAPSolution,
    var bestSolutionCost: Int,
    var iteration: Int = 0,
    val startTime: Long = System.currentTimeMillis(),
    var lastImprovement: Long = System.currentTimeMillis(),
    var evaluatedSolutions: Int = 0,
    var temperature: Double = Double.MAX_VALUE,
    var perturbations: Int = 0,
    //TODO Create getters and Setters that set the current timestamp to clean up the main function
    //TODO other cleanup to the main function
    //TODO Extend this state so that i can apply intesification/diversification strategies?
) {
    override fun toString(): String {
        return "Iteration: $iteration, Current Solution Cost: ${currentSolution.solutionCost}, Best Solution Cost: $bestSolutionCost, Evaluated Solutions: $evaluatedSolutions, Time Elapsed: ${System.currentTimeMillis() - startTime} ms, $temperature"
    }
}
