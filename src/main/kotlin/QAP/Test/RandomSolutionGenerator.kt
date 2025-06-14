package QAP.Test

import LocalSearch.AbstrSolutionGenerator
import LocalSearch.IProblemInstance
import LocalSearch.ISolution
import LocalSearch.ISolutionGenerator
import QAP.QAPInstance
import QAP.QAPSolution
import Util.Randomizer

class RandomSolutionGenerator<ProblemT : IProblemInstance, SolutionT: ISolution>(
    constructor: (ProblemT, IntArray) -> SolutionT
) : AbstrSolutionGenerator<ProblemT, SolutionT>(constructor) {
    override fun generateTest(instance: ProblemT): IntArray {
        val locations = Array(instance.instanceSize) { it }
        val randomSolution = Randomizer.randomShuffle(locations)

        return randomSolution.toIntArray()
    }

    override fun getName(): String {
        return "Random"
    }
}