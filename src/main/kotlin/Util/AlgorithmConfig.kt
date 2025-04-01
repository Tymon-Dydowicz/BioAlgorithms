package Util

import QAP.QAPInstance

data class AlgorithmConfig(
    val name: String,
    val displayName: String,
    val executor: (QAPInstance, Long) -> OptimizationResult,
    val maxTime: Long
)
