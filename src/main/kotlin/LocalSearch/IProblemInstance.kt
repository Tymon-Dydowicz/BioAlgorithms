package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase

interface IProblemInstance {
    val instanceName: String
    val instanceSize: Int
    var optimalSolution: ISolutionBase? // Think about how to change this to immutable

    fun verifyInstance()
    fun describe()
}