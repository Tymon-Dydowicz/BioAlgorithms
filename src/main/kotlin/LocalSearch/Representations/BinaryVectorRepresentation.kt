package LocalSearch.Representations

class BinaryVectorRepresentation(override val data: BooleanArray): IRepresentation<BooleanArray> {
    override fun retrieveData(): BooleanArray {
        return data.copyOf()
    }

    override fun describe() {
        println("Permutation Representation: ${data.joinToString(", ")}")
    }

    override fun copyWith(newRepresentation: BooleanArray): IRepresentation<BooleanArray> {
        return BinaryVectorRepresentation(newRepresentation)
    }

}