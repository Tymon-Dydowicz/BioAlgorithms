package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.Representations.IRepresentation
import LocalSearch.Representations.ISolution

abstract class AbstrSolutionGenerator<ProblemT : IProblemInstance, SolutionT : ISolutionBase, RepT : IRepresentation<*>>(
    private val constructor: (ProblemT, RepT) -> SolutionT
) : ISolutionGenerator<ProblemT, SolutionT> {

    @Suppress("UNCHECKED_CAST")
    final override fun generateSolution(instance: IProblemInstance): SolutionT {
        val typedInstance = instance as? ProblemT
            ?: error("${this::class.simpleName} cannot handle instance of type ${instance::class.simpleName}")
        val representation = generateTest(typedInstance) as RepT
        return constructor(typedInstance, representation)
    }

    abstract override fun generateTest(instance: ProblemT): IRepresentation<*>

    override fun getName(): String {
        return this::class.simpleName ?: "UnknownSolutionGenerator"
    }
}