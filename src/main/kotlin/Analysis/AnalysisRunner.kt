package Analysis

import Enums.AnalysisType
import QAP.runOptimization
import Util.OptimizationConfig


fun runAnalysis(
    config: OptimizationConfig,
    analysisType: AnalysisType
) {
    require(analysisType != AnalysisType.RESTARTS || config.algorithmType.isMultiStart()) {
        "Analysis type RESTARTS is only applicable to multi-start algorithms."
    }
    require(analysisType != AnalysisType.RESTARTS || config.algorithmRuns == 1) {
        "RESTARTS analysis must be run with exactly one algorithm execution (algorithmRuns == 1)."
    }

    val aggregateMultiStarts = (analysisType != AnalysisType.RESTARTS)
    config.aggregateMultiStarts = aggregateMultiStarts

    val runner = runOptimization(config, aggregateMultiStarts)
    val results = runner.results

    val report = AnalysisReportFactory.createReport(
        type = analysisType,
        instanceName = config.instance.instanceName,
        results = results,
        optimalSolution = config.instance.optimalSolution
    )

    val reportName = report.getReportName()
    val fileName = "results/${config.instance.instanceName}_${config.algorithmType}_${reportName}.csv"
    println("Exporting $reportName report to $fileName")

    report.exportToCSV(fileName)
}