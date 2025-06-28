package ProblemImplementations.TSP

import LocalSearch.AbstrSolutionGenerator
import LocalSearch.IProblemInstance
import LocalSearch.Representations.AbstrPermutationSolution
import LocalSearch.Representations.PermutationRepresentation
import Util.Randomizer

class TSPTestSolutionGenerator<ProblemT : IProblemInstance, SolutionT : AbstrPermutationSolution>(
    constructor: (ProblemT, PermutationRepresentation) -> SolutionT
) : AbstrSolutionGenerator<ProblemT, SolutionT, PermutationRepresentation>(constructor) {
    override fun generateTest(instance: ProblemT): PermutationRepresentation {
        val locations = Array(instance.instanceSize) { it }
        val randomSolution = Randomizer.randomShuffle(locations)

        return PermutationRepresentation(randomSolution.toIntArray())
    }

    override fun getName(): String {
        return "Random"
    }
}