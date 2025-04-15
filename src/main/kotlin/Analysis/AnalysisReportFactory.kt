package Analysis

import Enums.AnalysisType
import QAP.QAPSolution
import QAP.runOptimization
import Results.OptimizationResult
import Util.OptimizationConfig

object AnalysisReportFactory {
    fun createReport(type: AnalysisType, instanceName: String, results: List<OptimizationResult>, optimalSolution: QAPSolution? = null): AnalysisReport {
        return when (type) {
            AnalysisType.SIMILARITY -> {
                require(optimalSolution != null) { "Optimal solution required for similarity analysis" }
                SimilarityAnalysisReport(instanceName, results, optimalSolution)
            }
            AnalysisType.TREND -> TrendAnalysisReport(instanceName, results)
            AnalysisType.RESTARTS -> RestartsAnalysisReport(instanceName, results)
        }
    }
}
