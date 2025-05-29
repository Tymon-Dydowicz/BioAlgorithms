package LocalSearch

import QAP.QAPInstance
import QAP.TabuSearch.ICandidateSelector
import QAP.Test.SimulatedAnnealingAcceptance
import QAP.Test.TabuSearchAcceptance
import Results.OptimizationResult
import org.slf4j.LoggerFactory


abstract class AbstrLocalSearchMetaheuristic(
    protected val solutionGenerator: ISolutionGenerator,
    protected val neighborhoodExplorer: AbstrNeighborhoodExplorer,
    protected val acceptanceCriterion: IAcceptanceCriterion,
    protected val stoppingCriterion: IStoppingCriterion,
    protected val candidateSelector: ICandidateSelector,
    protected val perturbation: IPerturbation,
) {
    private val logger = LoggerFactory.getLogger(AbstrLocalSearchMetaheuristic::class.java)

    fun solve(instance: QAPInstance): OptimizationResult {
        // TODO Extend it to faciliate all LS variants
        // TODO Add RestartsStrategy, IntensificationStatrategy, DiversificationStrategy, CandidateSelection and SearchMemory
        // TODO Fix the steps calculation
        val evaluationsCounter = EvaluationsCounter()

        val result = OptimizationResult(getAlgorithmDescription(), instance.instanceSize)
        result.optimum = instance.optimalSolution!!.solutionCost

        var currentSolution = solutionGenerator.generate(instance)
        result.initialSolution = currentSolution

        val algorithmState = LocalSearchState(instance, currentSolution, currentSolution, currentSolution.solutionCost)

        result.addStep(currentSolution.solutionCost, System.nanoTime() - algorithmState.startTime)

        while (!stoppingCriterion.shouldStop(algorithmState, System.nanoTime())) {
            algorithmState.iteration++
            logger.trace(algorithmState.toString())

            val lazyMoves = neighborhoodExplorer.generateLazyMoves(algorithmState.currentSolution, evaluationsCounter)
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

                if (currentSolution.solutionCost < algorithmState.bestSolutionCost) {
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
        result.evaluatedSolutions = evaluationsCounter.evaluations

        return result
    }

    fun getAlgorithmDescription(): String {
        return "${solutionGenerator.getName()}${neighborhoodExplorer.getName()}${acceptanceCriterion.getName()}"
    }
}