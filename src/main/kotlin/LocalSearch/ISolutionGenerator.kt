package LocalSearch

import QAP.QAPInstance
import QAP.QAPSolution

interface ISolutionGenerator<in ProblemT : IProblemInstance, out SolutionT: ISolution> {
    fun generateSolution(instance: ProblemT): SolutionT
    fun generateTest(instance: ProblemT): IntArray

    fun getName(): String
}