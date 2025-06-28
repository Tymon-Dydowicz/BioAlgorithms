package LocalSearch.BaseInterfaces

interface IRepresentationBase {
    fun describe()
    fun getRawData(): Any
    fun copyWithRaw(newRepresentation: Any): IRepresentationBase
}