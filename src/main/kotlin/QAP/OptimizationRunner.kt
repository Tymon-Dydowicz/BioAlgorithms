package QAP

import LocalSearch.AbstrLocalSearchMetaheuristic
import Results.OptimizationResult
import java.util.*
import java.util.concurrent.Executors

data class OptimizationRunner(
    val config: OptimizationConfig,
    val startTime: Date = Date(),
    val results: MutableList<OptimizationResult> = mutableListOf()
) {
    var endTime: Date? = null
    var totalExecutionTimeMs: Long = 0

    fun getBestResult(): OptimizationResult? {
        return results.minByOrNull { it.bestSolution?.solutionCost ?: Int.MAX_VALUE }
    }

    fun getAverageSolutionCost(): Double {
        if (results.isEmpty()) return 0.0
        return results.mapNotNull { it.bestSolution?.solutionCost?.toDouble() }.average()
    }

    fun getAverageExecutionTime(): Double {
        if (results.isEmpty()) return 0.0
        return results.map { it.runtime.toDouble() }.average()
    }

    fun complete() {
        endTime = Date()
        totalExecutionTimeMs = endTime!!.time - startTime.time
    }
}

fun runOptimization(config: OptimizationConfig, aggregateMultiStarts: Boolean = true): OptimizationRunner {
    val runner = OptimizationRunner(config)

    config.executions.add(runner.startTime)

    val algorithm = config.localSearchConfig.createAlgorithm()
    config.instance.describe()

    val results = if (config.multiStarts == null) {
        println("Running single optimization with ${config.algorithmRuns} runs...")

        List(config.algorithmRuns) {
            algorithm.solve(config.instance)
        }
    } else {
        List(config.algorithmRuns) {
            val multiStartResults = runMultiStartOptimization(
                algorithm,
                config.instance,
                config.multiStarts,
            )

            if (aggregateMultiStarts) {
                listOf(multiStartResults.minByOrNull { it.bestSolution?.solutionCost ?: Int.MAX_VALUE }!!)
            } else {
                multiStartResults
            }
        }.flatten()
    }

    runner.results.addAll(results)

    runner.complete()
    return runner
}

fun runMultiStartOptimization(
    algorithm: AbstrLocalSearchMetaheuristic,
    instance: QAPInstance,
    starts: Int,
): List<OptimizationResult> {
    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    try {
        val futures = (1..starts).map {
            executor.submit<OptimizationResult> {
                algorithm.solve(instance)
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