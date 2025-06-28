package LocalSearch.Representations

class PermutationRepresentation(override val data: IntArray): IRepresentation<IntArray> {
    override fun retrieveData(): IntArray {
        return data.copyOf()
    }

    override fun describe() {
        println("Permutation Representation: ${data.joinToString(", ")}")
    }

    override fun copyWith(newRepresentation: IntArray): IRepresentation<IntArray> {
        return PermutationRepresentation(newRepresentation)
    }

}