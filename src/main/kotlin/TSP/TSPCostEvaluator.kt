package TSP

import LocalSearch.ICostEvaluator
import QAP.Test.SwapMove

class TSPCostEvaluator : ICostEvaluator<TSPSolution, SwapMove> {
    override fun evaluateDelta(solution: TSPSolution, move: SwapMove): Int {
        val tour = solution.solution
        val i = move.i
        val j = move.j
        val instance = solution.instance

        if (i == j) return 0

        val size = tour.size

        // Helper function to get tour city at position (with wraparound)
        fun cityAt(pos: Int): Int = tour[(pos + size) % size]

        // Get the actual cities being swapped
        val cityI = cityAt(i)
        val cityJ = cityAt(j)

        // Get neighbors (previous and next positions)
        val iPrev = (i - 1 + size) % size
        val iNext = (i + 1) % size
        val jPrev = (j - 1 + size) % size
        val jNext = (j + 1) % size

        var deltaCost = 0

        // Handle different cases based on position relationships
        when {
            // Case 1: Adjacent positions (i+1 == j)
            iNext == j -> {
                // Before: ...prev_i - city_i - city_j - next_j...
                // After:  ...prev_i - city_j - city_i - next_j...

                val oldCost = instance.getDistance(cityAt(iPrev), cityI) +
                        instance.getDistance(cityI, cityJ) +
                        instance.getDistance(cityJ, cityAt(jNext))

                val newCost = instance.getDistance(cityAt(iPrev), cityJ) +
                        instance.getDistance(cityJ, cityI) +
                        instance.getDistance(cityI, cityAt(jNext))

                deltaCost = newCost - oldCost
            }

            // Case 2: Adjacent positions (j+1 == i) - wraparound case
            jNext == i -> {
                // Before: ...prev_j - city_j - city_i - next_i...
                // After:  ...prev_j - city_i - city_j - next_i...

                val oldCost = instance.getDistance(cityAt(jPrev), cityJ) +
                        instance.getDistance(cityJ, cityI) +
                        instance.getDistance(cityI, cityAt(iNext))

                val newCost = instance.getDistance(cityAt(jPrev), cityI) +
                        instance.getDistance(cityI, cityJ) +
                        instance.getDistance(cityJ, cityAt(iNext))

                deltaCost = newCost - oldCost
            }

            // Case 3: Non-adjacent positions
            else -> {
                // Remove old edges
                val oldCost = instance.getDistance(cityAt(iPrev), cityI) +
                        instance.getDistance(cityI, cityAt(iNext)) +
                        instance.getDistance(cityAt(jPrev), cityJ) +
                        instance.getDistance(cityJ, cityAt(jNext))

                // Add new edges (after swap)
                val newCost = instance.getDistance(cityAt(iPrev), cityJ) +
                        instance.getDistance(cityJ, cityAt(iNext)) +
                        instance.getDistance(cityAt(jPrev), cityI) +
                        instance.getDistance(cityI, cityAt(jNext))

                deltaCost = newCost - oldCost
            }
        }

        return deltaCost
    }

    override fun evaluateSolution(solution: TSPSolution): Int {
        var totalCost = 0
        val representation = solution.solution
        for (i in representation.indices) {
            val nextIndex = (i + 1) % representation.size
            totalCost += solution.instance.getDistance(representation[i], representation[nextIndex])
        }
        return totalCost
    }
}