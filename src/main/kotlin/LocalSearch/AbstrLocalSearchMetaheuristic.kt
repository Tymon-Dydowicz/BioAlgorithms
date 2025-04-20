package LocalSearch

import QAP.QAPInstance
import Results.OptimizationResult

abstract class AbstrLocalSearchMetaheuristic(
    protected val solutionGenerator: ISolutionGenerator,
    protected val neighborhoodExplorer: INeighborhoodExplorer,
    protected val IAcceptanceCriterion: IAcceptanceCriterion,
    protected val stoppingCriterion: IStoppingCriterion,
    protected val perturbation: IPerturbation,
) {
    fun solve(instance: QAPInstance): OptimizationResult {
        val result = OptimizationResult(getAlgorithmDescription())
        result.optimum = instance.optimalSolution!!.solutionCost

        var currentSolution = solutionGenerator.generate(instance)
        result.initialSolution = currentSolution

        var bestSolution = currentSolution
        var bestSolutionCost = currentSolution.solutionCost
        val startTime = System.currentTimeMillis()
        var lastImprovement = startTime
        var iteration = 0

        val algorithmState = LocalSearchState(instance, currentSolution, bestSolution, bestSolutionCost, iteration, startTime, lastImprovement)

        result.addStep(currentSolution.solutionCost)
        result.increaseEvaluatedSolutions(1)
        algorithmState.evaluatedSolutions++

        while (!stoppingCriterion.shouldStop(
                algorithmState,
                System.currentTimeMillis(),
            )) {

            iteration++
            algorithmState.iteration++
            println(algorithmState.toString())

            // Generate moves for efficiency
            val moves = neighborhoodExplorer.generateMoves(algorithmState.currentSolution)

            // Select next solution based on acceptance criterion
            val (evaluations, selectedMove) = IAcceptanceCriterion.selectNextMove(
                algorithmState,
                moves,
                neighborhoodExplorer,
            )

            result.increaseEvaluatedSolutions(evaluations)
            algorithmState.evaluatedSolutions += evaluations
            // Apply the selected move to get the next solution

            // Update if a move was selected
            if (selectedMove != null) {
                currentSolution = neighborhoodExplorer.applyMove(algorithmState.currentSolution, selectedMove)
                result.addStep(currentSolution.solutionCost)

                algorithmState.currentSolution = currentSolution

                // Update best solution if improved
                if (currentSolution.solutionCost < bestSolutionCost) {
                    bestSolution = currentSolution
                    bestSolutionCost = currentSolution.solutionCost
                    // TODO the last improvement should be a little bit elsewhere
                    lastImprovement = System.currentTimeMillis()

                    algorithmState.bestSolution = bestSolution
                    algorithmState.bestSolutionCost = bestSolutionCost
                    algorithmState.lastImprovement = lastImprovement
                }
            } else if (perturbation is IPerturbation.NoPerturbation) {
                break
            } else {
                // No acceptable move found, could implement perturbation here
                // For now, we'll just break out of the loop
                val destroyedSolution = perturbation.destroy(algorithmState.currentSolution)
                val perturbatedSolution = perturbation.repair(destroyedSolution)

                algorithmState.currentSolution = perturbatedSolution
                algorithmState.perturbations++
            }
        }

        result.setRuntimeIn(System.currentTimeMillis() - startTime)
        result.setLastImprovementIn(System.currentTimeMillis() - lastImprovement)
        result.addStep(bestSolutionCost)
        result.setBestSolutionIn(bestSolution)

        return result
    }

    fun getAlgorithmDescription(): String {
        return "${solutionGenerator.getName()}${neighborhoodExplorer.getName()}${IAcceptanceCriterion.getName()}"
    }
}