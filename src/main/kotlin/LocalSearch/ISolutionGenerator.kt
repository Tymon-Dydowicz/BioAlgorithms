package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.BaseInterfaces.ISolutionGeneratorBase
import LocalSearch.Representations.IRepresentation
import LocalSearch.Representations.ISolution

interface ISolutionGenerator<ProblemT : IProblemInstance, SolutionT : ISolutionBase> : ISolutionGeneratorBase {
    override fun generateSolution(instance: IProblemInstance): SolutionT
    fun generateTest(instance: ProblemT): IRepresentation<*>
}