package Results

import java.io.File
import kotlin.math.sqrt

class AvgOptimizationResult(val name: String, private val results: List<OptimizationResult>) {
    var instanceSize: Int = results.firstOrNull()?.instanceSize ?: 0
    var avgRuntime: Double = 0.0
    var stdDevRuntime: Double = 0.0
    var bestRuntime: Long = Long.MAX_VALUE

    var avgPosSteps: Double = 0.0
    var stdDevPosSteps: Double = 0.0
    var bestPosSteps: Int = Int.MAX_VALUE

    var avgNegSteps: Double = 0.0
    var stdDevNegSteps: Double = 0.0
    var bestNegSteps: Int = Int.MAX_VALUE

    var avgTotalSteps: Double = 0.0
    var stdDevTotalSteps: Double = 0.0
    var bestTotalSteps: Int = Int.MAX_VALUE

    var avgAlgorithmLoops: Double = 0.0
    var stdDevAlgorithmLoops: Double = 0.0
    var bestAlgorithmLoops: Int = Int.MAX_VALUE

    var avgBestCost: Double = 0.0
    var stdDevBestCost: Double = 0.0
    var bestCost: Int = Int.MAX_VALUE

    var avgTimeSinceLastImprovement: Double = 0.0
    var stdDevTimeSinceLastImprovement: Double = 0.0
    var bestTimeSinceLastImprovement: Long = Long.MAX_VALUE

    var optimum: Int = 0

    var avgEvaluatedSolutions: Double = 0.0
    var stdDevEvaluatedSolutions: Double = 0.0
    var bestEvaluatedSolutions: Long = Long.MAX_VALUE

    var avgSolutionSteps: MutableList<Double> = mutableListOf()
    var avgSolutionTimestamps: MutableList<Long> = mutableListOf()
    var stdDevSolutionSteps: MutableList<Double> = mutableListOf()
    var bestSolutionSteps: MutableList<Int> = mutableListOf()
//    var bestSolutionTimestamps: MutableList<Long> = mutableListOf()

    init {
        if (results.isNotEmpty()) {
            calculateAveragesAndStdDevs()
        }
    }

    private fun calculateAveragesAndStdDevs() {
        avgRuntime = results.map { it.runtime }.averageLong()
        avgPosSteps = results.map { it.posSteps }.averageInt()
        avgNegSteps = results.map { it.negSteps }.averageInt()
        avgTotalSteps = results.map { it.totalSteps }.averageInt()
        avgAlgorithmLoops = results.map { it.algorithmLoops }.averageInt()
        avgBestCost = results.mapNotNull { it.bestSolution?.solutionCost }.averageInt()
        avgTimeSinceLastImprovement = results.map { it.timeSinceLastImprovement }.averageLong()
        optimum = results.firstOrNull()?.optimum ?: 0
        avgEvaluatedSolutions = results.map { it.evaluatedSolutions }.averageLong()

        stdDevRuntime = calculateStdDev(results.map { it.runtime.toDouble() })
        stdDevPosSteps = calculateStdDev(results.map { it.posSteps.toDouble() })
        stdDevNegSteps = calculateStdDev(results.map { it.negSteps.toDouble() })
        stdDevTotalSteps = calculateStdDev(results.map { it.totalSteps.toDouble() })
        stdDevAlgorithmLoops = calculateStdDev(results.map { it.algorithmLoops.toDouble() })
        stdDevBestCost = calculateStdDev(results.mapNotNull { it.bestSolution?.solutionCost?.toDouble() })
        stdDevTimeSinceLastImprovement = calculateStdDev(results.map { it.timeSinceLastImprovement.toDouble() })
        stdDevEvaluatedSolutions = calculateStdDev(results.map { it.evaluatedSolutions.toDouble() })

//        val minStepsLength = results.minOf { it.solutionSteps.size }
        val bestResult = results.minByOrNull { it.bestSolution?.solutionCost ?: Int.MAX_VALUE }
        bestRuntime = bestResult?.runtime ?: Long.MAX_VALUE
        bestPosSteps = bestResult?.posSteps ?: Int.MAX_VALUE
        bestNegSteps = bestResult?.negSteps ?: Int.MAX_VALUE
        bestTotalSteps = bestResult?.totalSteps ?: Int.MAX_VALUE
        bestAlgorithmLoops = bestResult?.algorithmLoops ?: Int.MAX_VALUE
        bestCost = bestResult?.bestSolution?.solutionCost ?: Int.MAX_VALUE
        bestTimeSinceLastImprovement = bestResult?.timeSinceLastImprovement ?: Long.MAX_VALUE
        bestEvaluatedSolutions = bestResult?.evaluatedSolutions ?: Long.MAX_VALUE

        val maxStepsLength = results.maxOfOrNull { it.solutionSteps.size } ?: 0

        for (i in 0 until maxStepsLength) {
            val stepsAtIteration = results
                .mapNotNull { it.solutionSteps.getOrNull(i)?.toDouble() }

            val timestampsAtIteration = results
                .mapNotNull { it.solutionTimestamps.getOrNull(i) }

            if (stepsAtIteration.isNotEmpty()) {
                avgSolutionSteps.add(stepsAtIteration.average())
                avgSolutionTimestamps.add(timestampsAtIteration.averageLong().toLong())
                stdDevSolutionSteps.add(calculateStdDev(stepsAtIteration))
                bestSolutionSteps.add(bestResult?.solutionSteps?.getOrNull(i) ?: -1)
//                bestSolutionTimestamps.add(bestResult?.solutionTimestamps?.getOrNull(i) ?: -1L)
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
        println("Runtime: $avgRuntime ± $stdDevRuntime ns")
        println("Positive Steps: $avgPosSteps ± $stdDevPosSteps | Negative Steps: $avgNegSteps ± $stdDevNegSteps | Total Steps: $avgTotalSteps ± $stdDevTotalSteps")
        println("Best Solution Cost: $avgBestCost ± $stdDevBestCost")
        println("Time Since Last Improvement: $avgTimeSinceLastImprovement ± $stdDevTimeSinceLastImprovement ns")
        println("Evaluated Solutions: $avgEvaluatedSolutions ± $stdDevEvaluatedSolutions")
        println("Gap to Optimum: ${((avgBestCost / optimum) - 1) * 100}%")
    }

    fun exportToCSV(filename: String = "${name}_avg_results.csv") {
        val file = File(filename)

        val csvContent = StringBuilder()
        csvContent.appendLine("type,key,value,std_dev,best,timestamp")
        csvContent.appendLine("metadata,instanceSize,${instanceSize},0,${instanceSize}")
        csvContent.appendLine("metadata,runtime,$avgRuntime,$stdDevRuntime,$bestRuntime")
        csvContent.appendLine("metadata,posSteps,$avgPosSteps,$stdDevPosSteps,$bestPosSteps")
        csvContent.appendLine("metadata,negSteps,$avgNegSteps,$stdDevNegSteps,$bestNegSteps")
        csvContent.appendLine("metadata,totalSteps,$avgTotalSteps,$stdDevTotalSteps,$bestTotalSteps")
        csvContent.appendLine("metadata,algorithmLoops,$avgAlgorithmLoops,$stdDevAlgorithmLoops,$bestAlgorithmLoops")
        csvContent.appendLine("metadata,bestCost,$avgBestCost,$stdDevBestCost,$bestCost")
        csvContent.appendLine("metadata,timeSinceLastImprovement,$avgTimeSinceLastImprovement,$stdDevTimeSinceLastImprovement,$bestTimeSinceLastImprovement")
        csvContent.appendLine("metadata,optimum,$optimum,0,$optimum")
        csvContent.appendLine("metadata,evaluatedSolutions,$avgEvaluatedSolutions,$stdDevEvaluatedSolutions,$bestEvaluatedSolutions")
        csvContent.appendLine("metadata,gapToOptimum,${((avgBestCost / optimum) - 1) * 100},0,${((bestCost.toDouble() / optimum) - 1) * 100}")

        avgSolutionSteps.forEachIndexed { index, avgCost ->
            csvContent.appendLine("step,$index,$avgCost,${stdDevSolutionSteps[index]},${bestSolutionSteps[index]},${avgSolutionTimestamps[index]}")
        }

        file.writeText(csvContent.toString())
    }

    companion object {
        fun fromResults(name: String, results: List<OptimizationResult>): AvgOptimizationResult {
            // discard 1st result because of warmup
            val resultsWarmup = results.drop(1)
//            println("individual results:")
//            for (result in results) {
//                println(result.runtime)
//            }
            return AvgOptimizationResult(name, resultsWarmup)
        }
    }
}