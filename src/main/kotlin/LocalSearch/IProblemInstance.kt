package LocalSearch

interface IProblemInstance {
    val instanceName: String
    val instanceSize: Int
    var optimalSolution: ISolution? // Think about how to change this to immutable

    fun verifyInstance()
    fun describe()
}