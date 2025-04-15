package Enums

enum class AlgorithmType(val displayName: String) {
    RANDOM_WALK("RW"),
    RANDOM_SEARCH("RS"),
    HEURSITC("heur"),
    RANDOM_GREEDY_LOCAL_SEARCH("randGLS"),
    RANDOM_STEEPEST_LOCAL_SEARCH("randSLS"),
    HEURISTIC_GREEDY_LOCAL_SEARCH("heurGLS"),
    HEURISTIC_STEEPEST_LOCAL_SEARCH("heurSLS"),
    RANDOM_GREEDY_MS_LOCAL_SEARCH("randGMSLS"),
    RANDOM_STEEPEST_MS_LOCAL_SEARCH("randSMSLS"),
    HEURISTIC_GREEDY_MS_LOCAL_SEARCH("heurGMSLS"),
    HEURISTIC_STEEPEST_MS_LOCAL_SEARCH("heurSMSLS");

    override fun toString(): String {
        return displayName
    }

    fun isMultiStart(): Boolean {
        return this == RANDOM_GREEDY_MS_LOCAL_SEARCH
                || this == RANDOM_STEEPEST_MS_LOCAL_SEARCH
                || this == HEURISTIC_GREEDY_MS_LOCAL_SEARCH
                || this == HEURISTIC_STEEPEST_MS_LOCAL_SEARCH
    }
}