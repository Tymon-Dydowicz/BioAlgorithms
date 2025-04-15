package Analysis

import Results.OptimizationResult
import java.io.File

class RestartsAnalysisReport private constructor(override val instanceName: String): AnalysisReport {
    var restarts: MutableList<Int> = mutableListOf()
    var avgBestCost: MutableList<Double> = mutableListOf()
    var bestCost: MutableList<Int> = mutableListOf()

    constructor(name: String, results: List<OptimizationResult>) : this(name) {
        processResults(results)
    }

    private fun processResults(results: List<OptimizationResult>) {
        for (i in results.indices) {
            val windowSize = i + 1
            restarts.add(windowSize)

            val windowResults = results.take(windowSize)
            val avgCost = windowResults.map { it.bestSolution!!.solutionCost }.average()
            avgBestCost.add(avgCost)

            val minCost = windowResults.minOfOrNull { it.bestSolution!!.solutionCost } ?: 0
            bestCost.add(minCost)
        }
    }

    override fun exportToCSV(filePath: String) {
        val file = File(filePath)
        file.printWriter().use { out ->
            out.println("Restarts,AvgBestCost,BestCost")
            for (i in restarts.indices) {
                out.println("${restarts[i]},${avgBestCost[i]},${bestCost[i]}")
            }
        }
    }

    override fun getReportName(): String = "restarts_analysis"

    fun addStep(restarts: Int, avgBestCost: Double, bestCost: Int) {
        this.restarts.add(restarts)
        this.avgBestCost.add(avgBestCost)
        this.bestCost.add(bestCost)
    }
}