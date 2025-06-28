package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase

abstract class AbstrProblemInstance(
    override val instanceSize: Int,
    instanceName: String?,
    override var optimalSolution: ISolutionBase? = null // Think about how to change this to immutable
) : IProblemInstance {
    override val instanceName: String = instanceName ?: "Unknown Instance"

    override fun verifyInstance() {
        // Default implementation does nothing
    }

    override fun describe() {
        println("Problem instance: $instanceName, size: $instanceSize")
        optimalSolution?.let {
            println("Optimal solution cost: ${it.solutionCost}")
        } ?: println("No optimal solution defined.")
    }
}