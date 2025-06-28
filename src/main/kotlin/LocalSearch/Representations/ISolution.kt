package LocalSearch.Representations

import LocalSearch.BaseInterfaces.ISolutionBase

interface ISolution<T : IRepresentation<*>> : ISolutionBase {
    val representation: T
    override val representationBase: T get() = representation

    // Unfortunately forces the user to implement this redundant method, Reflection is too expensive at this place
    fun copyWith(newRepresentation: T, delta: Int) : ISolution<T>

    override fun copyWithRaw(newRepresentation: Any, delta: Int): ISolutionBase {
        @Suppress("UNCHECKED_CAST")
        val newRep = representationBase.copyWithRaw(newRepresentation) as T
        return copyWith(newRep, delta)
    }
}