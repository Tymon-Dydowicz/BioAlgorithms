package LocalSearch

import QAP.QAPInstance
import QAP.QAPSolution

interface ISolutionGenerator {
    fun generate(instance: QAPInstance): QAPSolution
    fun getName(): String
}