package ProblemImplementations.TSP

import LocalSearch.Representations.PermutationRepresentation
import kotlin.math.*

class TSPUtil {
    companion object {
        fun generateCircularInstance(cityCount: Int = 10, radius: Double = 100.0, name: String? = null): TSPInstance {
            require(cityCount >= 3) { "TSP requires at least 3 cities." }

            val angleStep = 2 * Math.PI / cityCount
            val coords = List(cityCount) { i ->
                val angle = i * angleStep
                Pair(radius * cos(angle), radius * sin(angle))
            }

            val distances = Array(cityCount) { i ->
                IntArray(cityCount) { j ->
                    if (i == j) 0
                    else euclideanDistanceInt(coords[i], coords[j])
                }
            }

            val tspInstance = TSPInstance(cityCount, distances, name)

            val optimalTour = (0 until cityCount).toList()
            val optimalSolution = TSPSolution(tspInstance, PermutationRepresentation(optimalTour.toIntArray()))

            tspInstance.optimalSolution = optimalSolution

            return tspInstance
        }

        private fun euclideanDistanceInt(a: Pair<Double, Double>, b: Pair<Double, Double>): Int {
            val dx = a.first - b.first
            val dy = a.second - b.second
            return sqrt(dx * dx + dy * dy).roundToInt()
        }
    }
}