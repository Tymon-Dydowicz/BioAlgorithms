package LocalSearch

abstract class AbstrSolutionGenerator<ProblemT : IProblemInstance, SolutionT : ISolution>(
    private val constructor: (ProblemT, IntArray) -> SolutionT
) : ISolutionGenerator<ProblemT, SolutionT> {

    final override fun generateSolution(instance: ProblemT): SolutionT {
        val typedInstance = instance as? ProblemT
            ?: error("${this::class.simpleName} cannot handle instance of type ${instance::class.simpleName}")
        val representation = generateTest(typedInstance)
        return constructor(typedInstance, representation)
    }

    abstract override fun generateTest(instance: ProblemT): IntArray

    override fun getName(): String {
        return this::class.simpleName ?: "UnknownSolutionGenerator"
    }
}