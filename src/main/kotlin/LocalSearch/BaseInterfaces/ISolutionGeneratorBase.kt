package LocalSearch.BaseInterfaces

import LocalSearch.IProblemInstance

interface ISolutionGeneratorBase {
    fun generateSolution(instance: IProblemInstance): ISolutionBase
    fun getName(): String
}