package Util

import QAP.QAPInstance
import Results.OptimizationResult

data class AlgorithmConfig(
    val name: String,
    val displayName: String,
    val executor: (QAPInstance, Long) -> OptimizationResult,
    val maxTime: Long
)
