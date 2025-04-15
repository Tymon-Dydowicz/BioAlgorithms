package Analysis

import QAP.QAPSolution
import Results.OptimizationResult

class TrendAnalysisReport private constructor(override val instanceName: String): AnalysisReport {
    var initialSolutions: MutableList<QAPSolution> = mutableListOf()
    var bestSolutions: MutableList<QAPSolution> = mutableListOf()

    constructor(instanceName: String, results: List<OptimizationResult>) : this(instanceName) {
        processResults(results)
    }

    private fun processResults(results: List<OptimizationResult>) {
        for (result in results) {
            initialSolutions.add(result.initialSolution!!)
            bestSolutions.add(result.bestSolution!!)
        }
    }

    fun addStep(initialSolution: QAPSolution, bestSolution: QAPSolution) {
        initialSolutions.add(initialSolution)
        bestSolutions.add(bestSolution)
    }

    override fun exportToCSV(filePath: String) {
        val file = java.io.File(filePath)
        file.printWriter().use { out ->
            out.println("InitialSolution,BestSolution")
            for (i in initialSolutions.indices) {
                out.println("${initialSolutions[i].solutionCost},${bestSolutions[i].solutionCost}")
            }
        }
    }

    override fun getReportName(): String = "trend_analysis"
}