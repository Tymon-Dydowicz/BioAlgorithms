package Analysis

import QAP.QAPSolution
import QAP.QAPSolutionManager
import Results.OptimizationResult

class SimilarityAnalysisReport private constructor(override val instanceName: String): AnalysisReport {
    val hammingDistance: MutableList<Int> = mutableListOf()
    val hammingSimilarity: MutableList<Double> = mutableListOf()
    val cosineSimilarity: MutableList<Double> = mutableListOf()
    val solutionCosts: MutableList<Int> = mutableListOf()
    lateinit var optimalSolution: QAPSolution

    constructor(instanceName: String, results: List<OptimizationResult>, optimalSolution: QAPSolution) : this(instanceName) {
        this.optimalSolution = optimalSolution
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

    override fun exportToCSV(filePath: String) {
        val file = java.io.File(filePath)
        file.printWriter().use { out ->
            out.println("HammingDistance,HammingSimilarity,CosineSimilarity,Cost,OptimalCost")
            for (i in hammingDistance.indices) {
                out.println("${hammingDistance[i]},${hammingSimilarity[i]},${cosineSimilarity[i]},${solutionCosts[i]},${optimalSolution.solutionCost}")
            }
        }
    }

    override fun getReportName(): String = "similarity_analysis"

    companion object {
        fun createSteps(result: OptimizationResult) {
            val localOptimum = result.bestSolution!!
            val optimum = result.optimum
        }
    }

}