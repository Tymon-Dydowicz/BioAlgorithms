package LocalSearch.Representations

import LocalSearch.BaseInterfaces.IRepresentationBase

interface IRepresentation<T> : IRepresentationBase {
    val data: T
    override fun getRawData(): Any = data as Any
    fun retrieveData(): T
    fun copyWith(newRepresentation: T): IRepresentation<T>

    override fun copyWithRaw(newRepresentation: Any): IRepresentationBase {
        @Suppress("UNCHECKED_CAST")
        return copyWith(newRepresentation as T)
    }
}