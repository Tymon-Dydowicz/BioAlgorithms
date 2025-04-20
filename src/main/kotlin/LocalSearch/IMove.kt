package LocalSearch

interface IMove {
    fun extractFeature(featureType: String = "default"): String
    fun getAvailableFeatureTypes(): Set<String>
}