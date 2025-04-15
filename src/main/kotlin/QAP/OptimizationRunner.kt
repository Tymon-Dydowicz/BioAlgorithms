package QAP

import Results.OptimizationResult
import Util.OptimizationConfig
import java.util.*

/**
 * Data class that encapsulates optimization execution details and results
 */
data class OptimizationRunner(
    val config: OptimizationConfig,
    val startTime: Date = Date(),
    val results: MutableList<OptimizationResult> = mutableListOf()
) {
    var endTime: Date? = null
    var totalExecutionTimeMs: Long = 0

    /**
     * Returns the best result found across all executions
     */
    fun getBestResult(): OptimizationResult? {
        return results.minByOrNull { it.bestSolution?.solutionCost ?: Int.MAX_VALUE }
    }

    /**
     * Returns average solution cost across all results
     */
    fun getAverageSolutionCost(): Double {
        if (results.isEmpty()) return 0.0
        return results.mapNotNull { it.bestSolution?.solutionCost?.toDouble() }.average()
    }

    /**
     * Returns average execution time across all results
     */
    fun getAverageExecutionTime(): Double {
        if (results.isEmpty()) return 0.0
        return results.mapNotNull { it.runtime?.toDouble() }.average()
    }

    /**
     * Completes the optimization run by setting end time and calculating total time
     */
    fun complete() {
        endTime = Date()
        totalExecutionTimeMs = endTime!!.time - startTime.time
    }
}

/**
 * Runs optimization based on the given configuration and returns an OptimizationRunner
 * with execution details and results.
 */
fun runOptimization(config: OptimizationConfig, aggregateMultiStarts: Boolean = true): OptimizationRunner {
    val runner = OptimizationRunner(config)

    // Add execution timestamp to config
    config.executions.add(runner.startTime)

    // Execute optimization using the QAPOptimizer
    val results = QAPOptimizer.performOptimization(config, aggregateMultiStarts)
    runner.results.addAll(results)

    // Complete the run and return the runner
    runner.complete()
    return runner
}

/**
 * Extension function to print a summary of optimization results
 */
fun OptimizationRunner.printSummary() {
    println("=== Optimization Summary ===")
    println("Algorithm: ${config.algorithmType}")
    println("Instance: ${config.instance.instanceName}")
    println("Multi-starts: ${config.multiStarts}")
    println("Time limit per run: ${config.time}ms")
    println("Total execution time: ${totalExecutionTimeMs}ms")
    println("Average execution time: ${getAverageExecutionTime()}ms")

    val bestResult = getBestResult()
    if (bestResult != null) {
        println("Best solution cost: ${bestResult.bestSolution?.solutionCost}")
        println("Average solution cost: ${getAverageSolutionCost()}")
        println("Total evaluations: ${results.sumOf { it.evaluatedSolutions }}")
    } else {
        println("No valid results found.")
    }
}