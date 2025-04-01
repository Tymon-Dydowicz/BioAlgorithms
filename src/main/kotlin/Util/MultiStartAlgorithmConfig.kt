package Util

import QAP.QAPInstance

data class MultiStartAlgorithmConfig(
    val name: String,
    val displayName: String,
    val executor: (QAPInstance, Int, Long) -> List<OptimizationResult>,
    val startCount: Int,
    val maxTime: Long
)
