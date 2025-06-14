package TSP

import LocalSearch.AbstrSolutionGenerator
import LocalSearch.IProblemInstance
import LocalSearch.ISolution
import Util.Randomizer

class TSPTestSolutionGenerator<ProblemT : IProblemInstance, SolutionT : ISolution>(
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