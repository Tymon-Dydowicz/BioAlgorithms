package Enums

enum class AlgorithmType(private val displayName: String) {
    RANDOM_WALK("RW"),
    RANDOM_SEARCH("RS"),
    HEURSITC("heur"),
//    RANDOM_GREEDY_LOCAL_SEARCH("randGLS"),
//    RANDOM_STEEPEST_LOCAL_SEARCH("randSLS"),
    RANDOM_GREEDY_LOCAL_SEARCH("GreeedyLocalSearch"),
    RANDOM_STEEPEST_LOCAL_SEARCH("SteepestLocalSearch"),
    HEURISTIC_GREEDY_LOCAL_SEARCH("heurGLS"),
    HEURISTIC_STEEPEST_LOCAL_SEARCH("heurSLS"),
    RANDOM_GREEDY_MS_LOCAL_SEARCH("randGMSLS"),
    RANDOM_STEEPEST_MS_LOCAL_SEARCH("randSMSLS"),
//    RANDOM_GREEDY_MS_LOCAL_SEARCH("GreeedyLocalSearch"), //TODO watch for collision
//    RANDOM_STEEPEST_MS_LOCAL_SEARCH("SteepestLocalSearch"),//TODO watch for collision
    HEURISTIC_GREEDY_MS_LOCAL_SEARCH("heurGMSLS"),
    HEURISTIC_STEEPEST_MS_LOCAL_SEARCH("heurSMSLS"),
    SIMULATED_ANNEALING("SimulatedAnnealing"),
    TABU_SEARCH("TabuSearch"),
    CUSTOM("custom");

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