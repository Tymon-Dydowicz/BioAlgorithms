package Util

import QAP.QAPInstance
import java.util.Date

data class OptimizationConfig(
    val instance: QAPInstance,
    val algorithm: Algorithm,
    val time: Long,
    val algorithmRestarts: Int,
    val multiStarts: Int,
    val timeStamp: Date = Date(),
    val executions: MutableList<Date> = mutableListOf(),
) {
}
