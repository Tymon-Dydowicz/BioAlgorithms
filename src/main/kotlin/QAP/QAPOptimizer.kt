package QAP

import Util.OptimizationResult
import Util.Randomizer
import java.util.concurrent.Executors

object QAPOptimizer {
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

    fun performRandomWalk(instance: QAPInstance, steps: Int, time: Long): OptimizationResult {
        val result = OptimizationResult("RandomWalk")

        var currentSolution = generateRandomSolution(instance)
        var bestSolution = currentSolution
        var bestCost = currentSolution.solutionCost
        val endTime = System.currentTimeMillis() + time
        var improvementTimer = System.currentTimeMillis()

        result.addStep(bestCost)
        result.increaseEvaluatedSolutions(1)

        for (i in 0 until steps) {
            val neighoorhood = QAPNeighboorManager.generateNeighboorhood(currentSolution)
            val newSolution = neighoorhood.random().second
            val newCost = newSolution.solutionCost

            result.addStep(newCost)
            result.increaseEvaluatedSolutions(1)

            if (newCost < bestCost) {
                bestSolution = newSolution
                bestCost = newCost
                improvementTimer = System.currentTimeMillis()
            }

            if (System.currentTimeMillis() > endTime) {
                break
            }
        }

        result.setRuntimeIn(System.currentTimeMillis() - endTime + time)
        result.setLastImprovementIn(System.currentTimeMillis() - improvementTimer)
        result.setBestSolutionIn(bestSolution)

        return result
    }

    fun performRandomSearch(instance: QAPInstance, time: Long): OptimizationResult {
        val result = OptimizationResult("RandomSearch")

        var bestSolution = generateRandomSolution(instance)
        var bestCost = bestSolution.solutionCost
        val endTime = System.currentTimeMillis() + time
        var improvementTimer = System.currentTimeMillis()

        result.addStep(bestCost)
        result.increaseEvaluatedSolutions(1)

        while (System.currentTimeMillis() < endTime) {
            val newSolution = generateRandomSolution(instance)
            val newCost = newSolution.solutionCost

            result.addStep(newCost)
            result.increaseEvaluatedSolutions(1)

            if (newCost < bestCost) {
                bestSolution = newSolution
                bestCost = newCost
                improvementTimer = System.currentTimeMillis()
            }
        }

        result.setRuntimeIn(System.currentTimeMillis() - endTime + time)
        result.setLastImprovementIn(System.currentTimeMillis() - improvementTimer)
        result.setBestSolutionIn(bestSolution)

        return result
    }

    fun performRandomLocalSearchSteepest(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(instance, time, QAPOptimizer::generateRandomSolution, QAPNeighboorManager::steepestNeighboorSelection)
    }

    fun performHeuristicLocalSearchSteepest(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(instance, time, QAPOptimizer::generateHeuristicSolution, QAPNeighboorManager::steepestNeighboorSelection)
    }

    fun performRandomLocalSearchGreedy(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(instance, time, QAPOptimizer::generateRandomSolution, QAPNeighboorManager::greedyNeighboorSelection)
    }

    fun performHeuristicLocalSearchGreedy(instance: QAPInstance, time: Long): OptimizationResult {
        return performLocalSearchImpl(instance, time, QAPOptimizer::generateHeuristicSolution, QAPNeighboorManager::greedyNeighboorSelection)
    }

    private fun performLocalSearchImpl(instance: QAPInstance, time: Long, startMethod: (QAPInstance) -> QAPSolution, selectionMethod: (QAPSolution) -> Pair<Int, QAPSolution>): OptimizationResult {
        // TODO add counting of steps and convergence
        val result = OptimizationResult("RandomSearch")

        var solution = startMethod(instance)
        val endTime = System.currentTimeMillis() + time

        result.addStep(solution.solutionCost)
        result.increaseEvaluatedSolutions(1)

        while (System.currentTimeMillis() < endTime) {
            val (evauluations, bestNeighboor) = selectionMethod(solution)



            if (bestNeighboor.solutionCost >= solution.solutionCost) {
                println("Local minimum found!")
                break
            } else {
                solution = bestNeighboor
                result.addStep(bestNeighboor.solutionCost)
                result.increaseEvaluatedSolutions(evauluations)
            }
        }

        result.setRuntimeIn(System.currentTimeMillis() - endTime + time)
        result.setBestSolutionIn(solution)

        return result
    }

    fun performHeuristicMultiLSGreedy(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(instance, starts, time, QAPOptimizer::generateHeuristicSolution, QAPNeighboorManager::greedyNeighboorSelection)
    }

    fun performRandomMultiLSGreedy(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(instance, starts, time, QAPOptimizer::generateRandomSolution, QAPNeighboorManager::greedyNeighboorSelection)
    }

    fun performHeuristicMultiLSSteepest(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(instance, starts, time, QAPOptimizer::generateHeuristicSolution, QAPNeighboorManager::steepestNeighboorSelection)
    }

    fun performRandomMultiLSSteepest(instance: QAPInstance, starts: Int, time: Long): List<OptimizationResult> {
        return performMultiLSImpl(instance, starts, time, QAPOptimizer::generateRandomSolution, QAPNeighboorManager::steepestNeighboorSelection)
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
}