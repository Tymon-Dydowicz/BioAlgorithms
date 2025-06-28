package ProblemImplementations.TSP

import LocalSearch.ICostEvaluator
import QAP.Test.SwapMove

class TSPCostEvaluator : ICostEvaluator<TSPSolution, SwapMove> {
    override fun evaluateDelta(solution: TSPSolution, move: SwapMove): Int {
        val tour = solution.representation.retrieveData()
        val i = move.i
        val j = move.j
        val instance = solution.instance

        if (i == j) return 0

        val size = tour.size

        fun cityAt(pos: Int): Int = tour[(pos + size) % size]

        val cityI = cityAt(i)
        val cityJ = cityAt(j)

        val iPrev = (i - 1 + size) % size
        val iNext = (i + 1) % size
        val jPrev = (j - 1 + size) % size
        val jNext = (j + 1) % size

        var deltaCost = 0

        when {
            iNext == j -> {

                val oldCost = instance.getDistance(cityAt(iPrev), cityI) +
                        instance.getDistance(cityI, cityJ) +
                        instance.getDistance(cityJ, cityAt(jNext))

                val newCost = instance.getDistance(cityAt(iPrev), cityJ) +
                        instance.getDistance(cityJ, cityI) +
                        instance.getDistance(cityI, cityAt(jNext))

                deltaCost = newCost - oldCost
            }

            jNext == i -> {

                val oldCost = instance.getDistance(cityAt(jPrev), cityJ) +
                        instance.getDistance(cityJ, cityI) +
                        instance.getDistance(cityI, cityAt(iNext))

                val newCost = instance.getDistance(cityAt(jPrev), cityI) +
                        instance.getDistance(cityI, cityJ) +
                        instance.getDistance(cityJ, cityAt(iNext))

                deltaCost = newCost - oldCost
            }

            else -> {
                val oldCost = instance.getDistance(cityAt(iPrev), cityI) +
                        instance.getDistance(cityI, cityAt(iNext)) +
                        instance.getDistance(cityAt(jPrev), cityJ) +
                        instance.getDistance(cityJ, cityAt(jNext))

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
        val permutation = solution.representation.retrieveData()
        for (i in permutation.indices) {
            val nextIndex = (i + 1) % permutation.size
            totalCost += solution.instance.getDistance(permutation[i], permutation[nextIndex])
        }
        return totalCost
    }
}