package LocalSearch

import QAP.QAPInstance
import Results.OptimizationResult
import org.slf4j.LoggerFactory


abstract class AbstrLocalSearchMetaheuristic(
    protected val solutionGenerator: ISolutionGenerator,
    protected val neighborhoodExplorer: INeighborhoodExplorer,
    protected val IAcceptanceCriterion: IAcceptanceCriterion,
    protected val stoppingCriterion: IStoppingCriterion,
    protected val perturbation: IPerturbation,
) {
    private val logger = LoggerFactory.getLogger(AbstrLocalSearchMetaheuristic::class.java)

    fun solve(instance: QAPInstance): OptimizationResult {

        val result = OptimizationResult(getAlgorithmDescription())
        result.optimum = instance.optimalSolution!!.solutionCost

        var currentSolution = solutionGenerator.generate(instance)
        result.initialSolution = currentSolution

        val algorithmState = LocalSearchState(instance, currentSolution, currentSolution, currentSolution.solutionCost)

        result.addStep(currentSolution.solutionCost)
        result.increaseEvaluatedSolutions(1)
        algorithmState.evaluatedSolutions++

        while (!stoppingCriterion.shouldStop(
                algorithmState,
                System.currentTimeMillis(),
            )) {

            algorithmState.iteration++
            logger.info(algorithmState.toString())

            val moves = neighborhoodExplorer.generateMoves(algorithmState.currentSolution)

            val (evaluations, selectedMove) = IAcceptanceCriterion.selectNextMove(
                algorithmState,
                moves,
                neighborhoodExplorer,
            )

            result.increaseEvaluatedSolutions(evaluations)
            algorithmState.evaluatedSolutions += evaluations

            if (selectedMove != null) {
                currentSolution = neighborhoodExplorer.applyMove(algorithmState.currentSolution, selectedMove)
                result.addStep(currentSolution.solutionCost)

                algorithmState.currentSolution = currentSolution

                if (currentSolution.solutionCost < algorithmState.bestSolutionCost) {
                    // TODO the last improvement should be a little bit elsewhere
                    algorithmState.bestSolution = currentSolution
                    algorithmState.bestSolutionCost = currentSolution.solutionCost
                    algorithmState.lastImprovement = System.currentTimeMillis()
                }
            } else if (perturbation is IPerturbation.NoPerturbation) {
                break
            } else {
                val destroyedSolution = perturbation.destroy(algorithmState.currentSolution)
                val perturbatedSolution = perturbation.repair(destroyedSolution)

                algorithmState.currentSolution = perturbatedSolution
                algorithmState.perturbations++
            }
        }

        result.setRuntimeIn(System.currentTimeMillis() - algorithmState.startTime)
        result.setLastImprovementIn(System.currentTimeMillis() - algorithmState.lastImprovement)
        result.addStep(algorithmState.bestSolutionCost)
        result.setBestSolutionIn(algorithmState.bestSolution)

        return result
    }

    fun getAlgorithmDescription(): String {
        return "${solutionGenerator.getName()}${neighborhoodExplorer.getName()}${IAcceptanceCriterion.getName()}"
    }
}