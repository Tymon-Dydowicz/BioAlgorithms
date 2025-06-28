package LocalSearch.BaseInterfaces

import LocalSearch.BaseInterfaces.IRepresentationBase
import LocalSearch.IProblemInstance

interface ISolutionBase {
    val instance: IProblemInstance
    val solutionCost: Int
    val representationBase: IRepresentationBase

    fun describe()
    fun copyWithRaw(newRepresentation: Any, delta: Int): ISolutionBase
}