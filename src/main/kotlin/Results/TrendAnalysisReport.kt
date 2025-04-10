package Results

import QAP.QAPSolution

class TrendAnalysisReport(val name: String) {
    var initialSolutions: MutableList<QAPSolution> = mutableListOf()
    var bestSolutions: MutableList<QAPSolution> = mutableListOf()

    fun addStep(initialSolution: QAPSolution, bestSolution: QAPSolution) {
        initialSolutions.add(initialSolution)
        bestSolutions.add(bestSolution)
    }

    fun exportToCSV(filename: String = "trend.csv") {
        val file = java.io.File(filename)
        file.printWriter().use { out ->
            out.println("InitialSolution,BestSolution")
            for (i in initialSolutions.indices) {
                out.println("${initialSolutions[i].solutionCost},${bestSolutions[i].solutionCost}")
            }
        }
    }
}