package QAP

import Results.OptimizationResult
import Enums.AlgorithmType
import LocalSearch.EvaluationsCounter
import QAP.Test.SwapNeighborhoodExplorer
import Util.Randomizer
import java.util.*
import java.util.concurrent.Executors

object QAPOptimizer {
    fun performOptimization(config: OptimizationConfig, aggregateMultiStarts: Boolean): List<OptimizationResult> {
        config.executions.add(Date())

        return when (config.algorithmType) {
            AlgorithmType.RANDOM_WALK -> List(config.algorithmRuns) {
                performRandomWalk(config.instance, config.time)
            }

            AlgorithmType.RANDOM_SEARCH -> List(config.algorithmRuns) {
                performRandomSearch(config.instance, config.time)
            }

            AlgorithmType.RANDOM_GREEDY_LOCAL_SEARCH -> List(config.algorithmRuns) {
                performRandomLocalSearchGreedy(config.instance, config.time)
            }

            AlgorithmType.RANDOM_STEEPEST_LOCAL_SEARCH -> List(config.algorithmRuns) {
                performRandomLocalSearchSteepest(config.instance, config.time)
            }

            AlgorithmType.HEURISTIC_GREEDY_LOCAL_SEARCH -> List(config.algorithmRuns) {
                performHeuristicLocalSearchGreedy(config.instance, config.time)
            }

            AlgorithmType.HEURISTIC_STEEPEST_LOCAL_SEARCH -> List(config.algorithmRuns) {
                performHeuristicLocalSearchSteepest(config.instance, config.time)
            }

            AlgorithmType.RANDOM_GREEDY_MS_LOCAL_SEARCH -> List(config.algorithmRuns) {
                val results = performRandomMultiLSGreedy(config.instance, config.multiStarts!!, config.time)
                if (aggregateMultiStarts) results.minByOrNull { it.bestSolution!!.solutionCost }!! else results
            }.flattenIfNeeded(aggregateMultiStarts)

            AlgorithmType.RANDOM_STEEPEST_MS_LOCAL_SEARCH -> List(config.algorithmRuns) {
                val results = performRandomMultiLSSteepest(config.instance, config.multiStarts!!, config.time)
                if (aggregateMultiStarts) results.minByOrNull { it.bestSolution!!.solutionCost }!! else results
            }.flattenIfNeeded(aggregateMultiStarts)

            AlgorithmType.HEURISTIC_GREEDY_MS_LOCAL_SEARCH -> List(config.algorithmRuns) {
                val results = performHeuristicMultiLSGreedy(config.instance, config.multiStarts!!, config.time)
                if (aggregateMultiStarts) results.minByOrNull { it.bestSolution!!.solutionCost }!! else results
            }.flattenIfNeeded(aggregateMultiStarts)

            AlgorithmType.HEURISTIC_STEEPEST_MS_LOCAL_SEARCH -> List(config.algorithmRuns) {
                val results = performHeuristicMultiLSSteepest(config.instance, config.multiStarts!!, config.time)
                if (aggregateMultiStarts) results.minByOrNull { it.bestSolution!!.solutionCost }!! else results
            }.flattenIfNeeded(aggregateMultiStarts)

            else -> throw IllegalArgumentException("Unknown algorithm: ${config.algorithmType}")
        }
    }

    fun generateRandomSolution(qap: QAPInstance): QAPSolution {
        val locations = Array(qap.instanceSize) { it }
        val randomSolution = Randomizer.randomShuffle(locations)

        return QAPSolution(qap, randomSolution.toIntArray())
    }

    fun generateHeuristicSolution(instance: QAPInstance): QAPSolution {
        var solution = mutableListOf<Int>()
        var locations = MutableList(instance.instanceSize) { it }
        val intialFacility = Randomizer.getRandomIndex(instance.instanceSize)

        solution.add(intialFacility)
        locations.remove(intialFacility)

        while (locations.isNotEmpty()) {
            val nextFacility = locations.minByOrNull { QAPSolutionManager.calculateAdditionCost(solution, it, instance) }!!

            solution.add(nextFacility)
            locations.remove(nextFacility)
        }

        return QAPSolution(instance, solution.toIntArray())
    }

    fun performRandomWalk(instance: QAPInstance, time: Long): OptimizationResult {
        // TODO rework this whole part
        val counter = EvaluationsCounter()

        val result = OptimizationResult("RandomWalk", instance.instanceSize)
        result.optimum = instance.optimalSolution!!.solutionCost

        var currentSolution = generateRandomSolution(instance)
        result.initialSolution = currentSolution

        var bestSolution = currentSolution
        var bestCost = currentSolution.solutionCost
        val endTime = System.nanoTime() + time
        var improvementTimer = System.nanoTime()

        result.addStep(bestSolution.solutionCost, System.nanoTime() - endTime + time)
        result.increaseEvaluatedSolutions(1)
        result.algorithmLoops++
        val neighborhoodExplorer = SwapNeighborhoodExplorer()
        val lazyMoves = neighborhoodExplorer.generateLazyMoves(currentSolution, counter)

        while (System.nanoTime() < endTime) {
//            val neighoorhood = QAPNeighboorManager.generateNeighboorhood(currentSolution)
            val selectedMove = lazyMoves.random()
//            selectedMove.delta = neighborhoodExplorer.calculateDelta(currentSolution, selectedMove)
            val newSolution = neighborhoodExplorer.applyMove(currentSolution, selectedMove.move)

            result.increaseEvaluatedSolutions(1)
            result.algorithmLoops++

            if (newSolution.solutionCost < bestCost) {
                result.addStep(newSolution.solutionCost, System.nanoTime() - endTime + time)
                bestSolution = newSolution
                bestCost = newSolution.solutionCost
                improvementTimer = System.nanoTime()
            }
        }

        result.setRuntimeIn(System.nanoTime() - endTime + time)
        result.setLastImprovementIn(System.nanoTime() - improvementTimer)
        result.setBestSolutionIn(bestSolution)

        return result
    }

    fun performRandomSearch(instance: QAPInstance, time: Long): OptimizationResult {
        // TODO rework this whole part
        val result = OptimizationResult("RandomSearch", instance.instanceSize)
        result.optimum = instance.optimalSolution!!.solutionCost

        var bestSolution = generateRandomSolution(instance)
        result.initialSolution = bestSolution

        var bestCost = bestSolution.solutionCost
        val endTime = System.nanoTime() + time
        var improvementTimer = System.nanoTime()

        result.addStep(bestSolution.solutionCost, System.nanoTime() - endTime + time)
        result.increaseEvaluatedSolutions(1)
        result.algorithmLoops++

        while (System.nanoTime() < endTime) {
            val newSolution = generateRandomSolution(instance)
            val newCost = newSolution.solutionCost

            result.increaseEvaluatedSolutions(1)
            result.algorithmLoops++

            if (newCost < bestCost) {
                result.addStep(newSolution.solutionCost,System.nanoTime() - endTime + time)
                bestSolution = newSolution
                bestCost = newCost
                improvementTimer = System.nanoTime()
            }
        }

        result.setRuntimeIn(System.nanoTime() - endTime + time)
        result.setLastImprovementIn(System.nanoTime() - improvementTimer)
        result.setBestSolutionIn(bestSolution)

        return result
    }

    fun performHeurstic(instance: QAPInstance, time: Long): OptimizationResult {
        // TODO rework this whole part
        val result = OptimizationResult("Heuristic", instance.instanceSize)
        result.optimum = instance.optimalSolution!!.solutionCost

        val solution = mutableListOf<Int>()
        val locations = MutableList(instance.instanceSize) { it }
        val intialFacility = Randomizer.getRandomIndex(instance.instanceSize)
        val endTime = System.nanoTime() + time

        solution.add(intialFacility)
        locations.remove(intialFacility)

        while (locations.isNotEmpty()) {
            val nextFacility = locations.minByOrNull { QAPSolutionManager.calculateAdditionCost(solution, it, instance) }!!

            solution.add(nextFacility)
            locations.remove(nextFacility)
        }

        result.setRuntimeIn(System.nanoTime() - endTime + time)
        val heurSolution = QAPSolution(instance, solution.toIntArray())
        result.initialSolution = heurSolution
        result.setBestSolutionIn(heurSolution)

        return result
    }

    fun performRandomLocalSearchSteepest(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(
            instance,
            time,
            QAPOptimizer::generateRandomSolution,
            QAPNeighboorManager::steepestNeighboorSelection
        )
    }

    fun performHeuristicLocalSearchSteepest(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(
            instance,
            time,
            QAPOptimizer::generateHeuristicSolution,
            QAPNeighboorManager::steepestNeighboorSelection
        )
    }

    fun performRandomLocalSearchGreedy(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(
            instance,
            time,
            QAPOptimizer::generateRandomSolution,
            QAPNeighboorManager::greedyNeighboorSelection
        )
    }

    fun performHeuristicLocalSearchGreedy(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(
            instance,
            time,
            QAPOptimizer::generateHeuristicSolution,
            QAPNeighboorManager::greedyNeighboorSelection
        )
    }

    private fun performLocalSearchImpl(instance: QAPInstance, time: Long, startMethod: (QAPInstance) -> QAPSolution, selectionMethod: (QAPSolution) -> Pair<Int, QAPSolution>): OptimizationResult {
        val result = OptimizationResult("RandomSearch", instance.instanceSize)

        var solution = startMethod(instance)
        result.initialSolution = solution
        val endTime = System.nanoTime() + time

        result.addStep(solution.solutionCost, System.nanoTime() - endTime + time)
        result.increaseEvaluatedSolutions(1)

        while (System.nanoTime() < endTime) {
            val (evauluations, bestNeighboor) = selectionMethod(solution)



            if (bestNeighboor.solutionCost >= solution.solutionCost) {
//                println("Local minimum found!")
                break
            } else {
                solution = bestNeighboor
                result.addStep(bestNeighboor.solutionCost, System.nanoTime() - endTime + time)
                result.increaseEvaluatedSolutions(evauluations)
            }
        }

        result.setRuntimeIn(System.nanoTime() - endTime + time)
        result.setBestSolutionIn(solution)

        return result
    }

    fun performHeuristicMultiLSGreedy(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(
            instance,
            starts,
            time,
            QAPOptimizer::generateHeuristicSolution,
            QAPNeighboorManager::greedyNeighboorSelection
        )
    }

    fun performRandomMultiLSGreedy(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(
            instance,
            starts,
            time,
            QAPOptimizer::generateRandomSolution,
            QAPNeighboorManager::greedyNeighboorSelection
        )
    }

    fun performHeuristicMultiLSSteepest(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(
            instance,
            starts,
            time,
            QAPOptimizer::generateHeuristicSolution,
            QAPNeighboorManager::steepestNeighboorSelection
        )
    }

    fun performRandomMultiLSSteepest(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(
            instance,
            starts,
            time,
            QAPOptimizer::generateRandomSolution,
            QAPNeighboorManager::steepestNeighboorSelection
        )
    }

    private fun performMultiLSImpl(instance: QAPInstance, starts: Int, time: Long, startMethod: (QAPInstance) -> QAPSolution, selectionMethod: (QAPSolution) -> Pair<Int, QAPSolution>): List<OptimizationResult> {
        val timePerRun = time
        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        try {
            val futures = (1..starts).map {
                executor.submit<OptimizationResult> {
                    performLocalSearchImpl(instance, timePerRun, startMethod, selectionMethod)
                }
            }

            return futures.map { it.get() }
        } finally {
            executor.shutdown()
        }
    }

    private fun List<Any>.flattenIfNeeded(flatten: Boolean): List<OptimizationResult> {
        return if (flatten) {
            this as List<OptimizationResult>
        } else {
            this.flatMap { it as List<OptimizationResult> }
        }
    }
}