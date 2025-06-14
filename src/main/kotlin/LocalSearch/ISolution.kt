package LocalSearch

import QAP.QAPInstance

interface ISolution {
    val instance: IProblemInstance
    val solutionCost: Int
    val solution: IntArray //TEMPORARY

    fun describe()

    // Unfortunately forces the user to implement this redundant method, Reflection is too expensive at this place
    fun copyWith(newRepresentation: IntArray, delta: Int) : ISolution
}