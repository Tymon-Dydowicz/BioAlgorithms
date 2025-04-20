package Results

import java.io.File
import kotlin.math.sqrt

class AvgOptimizationResult(val name: String, private val results: List<OptimizationResult>) {
    var avgRuntime: Double = 0.0
    var stdDevRuntime: Double = 0.0

    var avgPosSteps: Double = 0.0
    var stdDevPosSteps: Double = 0.0

    var avgNegSteps: Double = 0.0
    var stdDevNegSteps: Double = 0.0

    var avgBestCost: Double = 0.0
    var stdDevBestCost: Double = 0.0
    var bestCost: Int = 0

    var avgTimeSinceLastImprovement: Double = 0.0
    var stdDevTimeSinceLastImprovement: Double = 0.0

    var optimum: Int = 0

    var avgEvaluatedSolutions: Double = 0.0
    var stdDevEvaluatedSolutions: Double = 0.0

    var avgSolutionSteps: MutableList<Double> = mutableListOf()
    var stdDevSolutionSteps: MutableList<Double> = mutableListOf()
    var bestSolutionSteps: MutableList<Int> = mutableListOf()

    init {
        if (results.isNotEmpty()) {
            calculateAveragesAndStdDevs()
        }
    }

    private fun calculateAveragesAndStdDevs() {
        avgRuntime = results.map { it.runtime }.averageLong()
        avgPosSteps = results.map { it.posSteps }.averageInt()
        avgNegSteps = results.map { it.negSteps }.averageInt()
        avgBestCost = results.mapNotNull { it.bestSolution?.solutionCost }.averageInt()
        avgTimeSinceLastImprovement = results.map { it.timeSinceLastImprovement }.averageLong()
        optimum = results.firstOrNull()?.optimum ?: 0
        avgEvaluatedSolutions = results.map { it.evaluatedSolutions }.averageInt()

        stdDevRuntime = calculateStdDev(results.map { it.runtime.toDouble() })
        stdDevPosSteps = calculateStdDev(results.map { it.posSteps.toDouble() })
        stdDevNegSteps = calculateStdDev(results.map { it.negSteps.toDouble() })
        stdDevBestCost = calculateStdDev(results.mapNotNull { it.bestSolution?.solutionCost?.toDouble() })
        stdDevTimeSinceLastImprovement = calculateStdDev(results.map { it.timeSinceLastImprovement.toDouble() })
        stdDevEvaluatedSolutions = calculateStdDev(results.map { it.evaluatedSolutions.toDouble() })

//        val minStepsLength = results.minOf { it.solutionSteps.size }
        val bestResult = results.maxByOrNull { it.bestSolution?.solutionCost ?: Int.MAX_VALUE }
        val maxStepsLength = results.maxOfOrNull { it.solutionSteps.size } ?: 0

        for (i in 0 until maxStepsLength) {
            val stepsAtIteration = results
                .mapNotNull { it.solutionSteps.getOrNull(i)?.toDouble() }

            if (stepsAtIteration.isNotEmpty()) {
                avgSolutionSteps.add(stepsAtIteration.average())
                stdDevSolutionSteps.add(calculateStdDev(stepsAtIteration))
                bestSolutionSteps.add(bestResult?.solutionSteps?.getOrNull(i) ?: -1)
            }
        }
    }

    private fun List<Double>.averageDouble(): Double {
        return if (this.isEmpty()) 0.0 else this.sum() / this.size
    }

    private fun List<Long>.averageLong(): Double {
        return if (this.isEmpty()) 0.0 else this.sum().toDouble() / this.size
    }

    private fun List<Int>.averageInt(): Double {
        return if (this.isEmpty()) 0.0 else this.sum().toDouble() / this.size
    }

    private fun calculateStdDev(values: List<Double>): Double {
        if (values.isEmpty() || values.size == 1) return 0.0

        val avg = values.averageDouble()
        val variance = values.map { (it - avg) * (it - avg) }.sum() / (values.size - 1)
        return sqrt(variance)
    }

    fun describe() {
        println("Method: $name (Averaged over ${results.size} runs)")
        println("Runtime: $avgRuntime ± $stdDevRuntime ms")
        println("Positive Steps: $avgPosSteps ± $stdDevPosSteps | Negative Steps: $avgNegSteps ± $stdDevNegSteps")
        println("Best Solution Cost: $avgBestCost ± $stdDevBestCost")
        println("Time Since Last Improvement: $avgTimeSinceLastImprovement ± $stdDevTimeSinceLastImprovement ms")
        println("Evaluated Solutions: $avgEvaluatedSolutions ± $stdDevEvaluatedSolutions")
        println("Gap to Optimum: ${((avgBestCost / optimum) - 1) * 100}%")
    }

    fun exportToCSV(filename: String = "${name}_avg_results.csv") {
        val file = File(filename)

        val csvContent = StringBuilder()
        csvContent.appendLine("type,key,value,std_dev,best")
        csvContent.appendLine("metadata,runtime,$avgRuntime,$stdDevRuntime")
        csvContent.appendLine("metadata,posSteps,$avgPosSteps,$stdDevPosSteps")
        csvContent.appendLine("metadata,negSteps,$avgNegSteps,$stdDevNegSteps")
        csvContent.appendLine("metadata,bestCost,$avgBestCost,$stdDevBestCost")
        csvContent.appendLine("metadata,timeSinceLastImprovement,$avgTimeSinceLastImprovement,$stdDevTimeSinceLastImprovement")
        csvContent.appendLine("metadata,optimum,$optimum,0")
        csvContent.appendLine("metadata,evaluatedSolutions,$avgEvaluatedSolutions,$stdDevEvaluatedSolutions")
        csvContent.appendLine("metadata,gapToOptimum,${((avgBestCost / optimum) - 1) * 100},0")

        avgSolutionSteps.forEachIndexed { index, avgCost ->
            csvContent.appendLine("step,$index,$avgCost,${stdDevSolutionSteps[index]},${bestSolutionSteps[index]}")
        }

        file.writeText(csvContent.toString())
    }

    companion object {
        fun fromResults(name: String, results: List<OptimizationResult>): AvgOptimizationResult {
            return AvgOptimizationResult(name, results)
        }
    }
}