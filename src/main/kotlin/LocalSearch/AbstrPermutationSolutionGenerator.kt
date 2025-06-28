package LocalSearch

import LocalSearch.Representations.AbstrPermutationSolution
import LocalSearch.Representations.PermutationRepresentation

abstract class AbstrPermutationSolutionGenerator<ProblemT : IProblemInstance, SolutionT: AbstrPermutationSolution>(
    constructor: (ProblemT, PermutationRepresentation) -> SolutionT
) : AbstrSolutionGenerator<ProblemT, SolutionT, PermutationRepresentation>(constructor)