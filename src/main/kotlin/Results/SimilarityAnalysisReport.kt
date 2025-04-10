package Results

import QAP.QAPSolution
import QAP.QAPSolutionManager

class SimilarityAnalysisReport(val name: String, val optimalSolution: QAPSolution) {
    val hammingDistance: MutableList<Int> = mutableListOf()
    val hammingSimilarity: MutableList<Double> = mutableListOf()
    val cosineSimilarity: MutableList<Double> = mutableListOf()
    val solutionCosts: MutableList<Int> = mutableListOf()

    constructor(name: String, optimalSolution: QAPSolution, results: List<OptimizationResult>) : this(name, optimalSolution) {
        processResults(results)
    }

    private fun processResults(results: List<OptimizationResult>) {
        for (result in results) {
            hammingDistance.add(QAPSolutionManager.calculatHammingDistance(result.bestSolution!!, optimalSolution))
            hammingSimilarity.add(QAPSolutionManager.calculateHammingSimilarity(result.bestSolution!!, optimalSolution))
            cosineSimilarity.add(QAPSolutionManager.calculateCosineSimilarity(result.bestSolution!!, optimalSolution))
            solutionCosts.add(result.bestSolution!!.solutionCost)
        }
    }

    class SimilarityAnalysisStep(
        val hammingDistance: Int,
        val hammingSimilarity: Double,
        val cosineSimilarity: Double,
        val solutionCost: Int
    )

    fun addStep(
        hammingDistance: Int,
        hammingSimilarity: Double,
        cosineSimilarity: Double,
        solutionCost: Int
    ) {
        this.hammingDistance.add(hammingDistance)
        this.hammingSimilarity.add(hammingSimilarity)
        this.cosineSimilarity.add(cosineSimilarity)
        this.solutionCosts.add(solutionCost)
    }

    fun exportToCSV(filename: String = "similarity.csv") {
        val file = java.io.File(filename)
        file.printWriter().use { out ->
            out.println("HammingDistance,HammingSimilarity,CosineSimilarity,Cost,OptimalCost")
            for (i in hammingDistance.indices) {
                out.println("${hammingDistance[i]},${hammingSimilarity[i]},${cosineSimilarity[i]},${solutionCosts[i]},${optimalSolution.solutionCost}")
            }
        }
    }

    companion object {
        fun createSteps(result: OptimizationResult) {
            val localOptimum = result.bestSolution!!
            val optimum = result.optimum
        }
    }

}