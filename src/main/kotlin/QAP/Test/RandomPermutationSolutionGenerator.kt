package QAP.Test

import LocalSearch.AbstrPermutationSolutionGenerator
import LocalSearch.AbstrSolutionGenerator
import LocalSearch.IProblemInstance
import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.Representations.AbstrPermutationSolution
import LocalSearch.Representations.PermutationRepresentation

class RandomPermutationSolutionGenerator<ProblemT : IProblemInstance, SolutionT: AbstrPermutationSolution>(
    constructor: (ProblemT, PermutationRepresentation) -> SolutionT
) : AbstrPermutationSolutionGenerator<ProblemT, SolutionT>(constructor){
    override fun generateTest(instance: ProblemT): PermutationRepresentation {
        val locations = Array(instance.instanceSize) { it }
        val randomSolution = locations.shuffle().let { locations }

        return PermutationRepresentation(randomSolution.toIntArray())
    }

    override fun getName(): String {
        return "Random"
    }
}