package Results

import java.io.File

class RestartsAnalysisReport(val name: String) {
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

    fun exportToCsv(filename: String = "restarts.csv") {
        val file = File(filename)
        file.printWriter().use { out ->
            out.println("Restarts,AvgBestCost,BestCost")
            for (i in restarts.indices) {
                out.println("${restarts[i]},${avgBestCost[i]},${bestCost[i]}")
            }
        }
    }

    fun addStep(restarts: Int, avgBestCost: Double, bestCost: Int) {
        this.restarts.add(restarts)
        this.avgBestCost.add(avgBestCost)
        this.bestCost.add(bestCost)
    }
}