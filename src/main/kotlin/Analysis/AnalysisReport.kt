package Analysis

interface AnalysisReport {
    val instanceName: String

    fun exportToCSV(filePath: String)

    fun getReportName(): String
}