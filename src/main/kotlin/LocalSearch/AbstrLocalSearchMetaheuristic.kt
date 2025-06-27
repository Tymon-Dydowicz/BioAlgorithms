package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.Representations.AbstrPermutationSolution
import LocalSearch.Representations.ISolution
import QAP.TabuSearch.ICandidateSelector
import Results.OptimizationResult
import org.slf4j.LoggerFactory


abstract class AbstrLocalSearchMetaheuristic(
    protected val solutionGenerator: ISolutionGenerator<*, *>, //TODO Think about builder pattern for safety
    protected val neighborhoodExplorer: INeighborhoodExplorer,
    protected val acceptanceCriterion: IAcceptanceCriterion,
    protected val stoppingCriterion: IStoppingCriterion,
    protected val candidateSelector: ICandidateSelector,
    protected val perturbation: IPerturbation,
    protected val objective: IObjective
) {
    private val logger = LoggerFactory.getLogger(AbstrLocalSearchMetaheuristic::class.java)

    fun solve(instance: IProblemInstance): OptimizationResult {
        // TODO Extend it to facilitate all LS variants
        // TODO Add RestartsStrategy, IntensificationStrategy, DiversificationStrategy, CandidateSelection and SearchMemory
        // TODO Fix the steps calculation
        val evaluationsCounter = EvaluationsCounter()

        val result = OptimizationResult(getAlgorithmDescription(), instance.instanceSize)
        result.optimum = instance.optimalSolution!!.solutionCost

        @Suppress("UNCHECKED_CAST")
        val typedGenerator = solutionGenerator as ISolutionGenerator<IProblemInstance, ISolution>
        var currentSolution = typedGenerator.generateSolution(instance)
        result.initialSolution = currentSolution

        val algorithmState = LocalSearchState(instance, currentSolution, currentSolution, currentSolution.solutionCost)

        result.addStep(currentSolution.solutionCost, System.nanoTime() - algorithmState.startTime)

        while (!stoppingCriterion.shouldStop(algorithmState, System.nanoTime())) {
            algorithmState.iteration++
            logger.trace(algorithmState.toString())

            val lazyMoves = neighborhoodExplorer.generateLazyMoves(algorithmState.currentSolution, objective, evaluationsCounter)
            val candidateMoves = candidateSelector.selectCandidates(lazyMoves, algorithmState)

            val selectedMove = acceptanceCriterion.selectNextMove(algorithmState, candidateMoves)

//            result.increaseEvaluatedSolutions(evaluations)
            result.algorithmLoops++
//            algorithmState.evaluatedSolutions += evaluations

            if (selectedMove != null) {
//                currentSolution = neighborhoodExplorer.applyMove(algorithmState.currentSolution, selectedMove)
                currentSolution = selectedMove.applyTo(algorithmState.currentSolution)
                algorithmState.currentSolution = currentSolution
                // TODO Think about pos/neg steps here?
                result.totalSteps++

                if (objective.isBetter(algorithmState.bestSolution, currentSolution)) {
                    // TODO Rethink if this is a correct spot
                    result.addStep(currentSolution.solutionCost, System.nanoTime() - algorithmState.startTime)
                    algorithmState.bestSolution = currentSolution
                    algorithmState.bestSolutionCost = currentSolution.solutionCost
                    algorithmState.lastImprovement = System.nanoTime()
                    algorithmState.iterationsWithoutImprovement = 0
                } else {
                    algorithmState.iterationsWithoutImprovement++
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

        result.setRuntimeIn(System.nanoTime() - algorithmState.startTime)
        result.setLastImprovementIn(System.nanoTime() - algorithmState.lastImprovement)
        result.addStep(algorithmState.bestSolutionCost, System.nanoTime() - algorithmState.startTime)
        result.setBestSolutionIn(algorithmState.bestSolution)
        println("Best solution: ${algorithmState.bestSolution}, cost: ${algorithmState.bestSolutionCost}")
        algorithmState.bestSolution.describe()
        result.evaluatedSolutions = evaluationsCounter.evaluations

        return result
    }

    fun getAlgorithmDescription(): String {
        return "${solutionGenerator.getName()}${neighborhoodExplorer.getName()}${acceptanceCriterion.getName()}"
    }
}