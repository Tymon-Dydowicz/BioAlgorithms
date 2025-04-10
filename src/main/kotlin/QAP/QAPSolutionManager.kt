package QAP

import kotlin.math.sqrt

object QAPSolutionManager {
    fun calculateAdditionCost(solution: List<Int>, newFacility: Int, instance: QAPInstance): Int {
        var cost = 0
        val newLocation = solution.size

        for (i in solution.indices) {
            cost += instance.getFlow(i, newLocation) * instance.getDistance(solution[i], newFacility)
        }

        return cost
    }

    fun calculateSolutionCost(instance: QAPInstance, solution: IntArray): Int {
        var cost = 0
        for (i in 0 until instance.instanceSize) {
            for (j in 0 until instance.instanceSize) {
                cost += instance.getFlow(i, j) * instance.getDistance(solution[i], solution[j])
            }
        }
        return cost
    }

    fun calculateSwapCost(instance: QAPInstance, solution: IntArray, firstIndex: Int, secondIndex: Int): Int {
        var cost = 0
        for (i in 0 until instance.instanceSize) {
            if (i != firstIndex && i != secondIndex) {
                cost += instance.getFlow(firstIndex, i) * (instance.getDistance(solution[secondIndex], solution[i]) - instance.getDistance(solution[firstIndex], solution[i]))
                cost += instance.getFlow(secondIndex, i) * (instance.getDistance(solution[firstIndex], solution[i]) - instance.getDistance(solution[secondIndex], solution[i]))
                cost += instance.getFlow(i, firstIndex) * (instance.getDistance(solution[i], solution[secondIndex]) - instance.getDistance(solution[i], solution[firstIndex]))
                cost += instance.getFlow(i, secondIndex) * (instance.getDistance(solution[i], solution[firstIndex]) - instance.getDistance(solution[i], solution[secondIndex]))
            }
        }


        return cost
    }

    fun calculatHammingDistance(solution1: QAPSolution, solution2: QAPSolution): Int {
        return calculateHammingDistance(solution1.solution, solution2.solution)
    }

    fun calculateHammingDistance(solution1: IntArray, solution2: IntArray): Int {
        var distance = 0
        for (i in solution1.indices) {
            if (solution1[i] != solution2[i]) {
                distance++
            }
        }
        return distance
    }

    fun calculateHammingSimilarity(solution1: QAPSolution, solution2: QAPSolution): Double {
        return calculateHammingSimilarity(solution1.solution, solution2.solution)
    }

    fun calculateHammingSimilarity(solution1: IntArray, solution2: IntArray): Double {
        val distance = calculateHammingDistance(solution1, solution2)
        return 1.0 - (distance.toDouble() / solution1.size)
    }

    fun calculateCosineSimilarity(solution1: QAPSolution, solution2: QAPSolution): Double {
        return calculateCosineSimilarity(solution1.solution, solution2.solution)
    }

    fun calculateCosineSimilarity(solution1: IntArray, solution2: IntArray): Double {
        var dotProduct = 0.0
        var normA = 0.0
        var normB = 0.0

        for (i in solution1.indices) {
            dotProduct += solution1[i] * solution2[i]
            normA += solution1[i] * solution1[i]
            normB += solution2[i] * solution2[i]
        }

        return dotProduct / (sqrt(normA) * sqrt(normB))
    }
}