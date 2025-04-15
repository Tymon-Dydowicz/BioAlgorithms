package Util

import Enums.AlgorithmType
import QAP.QAPInstance
import java.util.Date

data class OptimizationConfig(
    val instance: QAPInstance,
    val algorithmType: AlgorithmType,
    val time: Long,
    val algorithmRuns: Int = 1,
    val multiStarts: Int? = null,
    var aggregateMultiStarts: Boolean = true,
    val timeStamp: Date = Date(),
    val executions: MutableList<Date> = mutableListOf(),
) {
}
