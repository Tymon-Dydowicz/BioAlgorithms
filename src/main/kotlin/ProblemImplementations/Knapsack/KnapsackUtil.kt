package ProblemImplementations.Knapsack

import LocalSearch.Representations.BinaryVectorRepresentation

class KnapsackUtil {
    companion object {
        fun generateInstanceWithKnownOptimum(
            itemCount: Int = 20,
            optimalItemCount: Int = itemCount / 2,
            baseWeight: Int = 10,
            baseValue: Int = 15,
            name: String? = null
        ): KnapsackInstance {
            require(itemCount > 0) { "Item count must be positive" }
            require(optimalItemCount > 0 && optimalItemCount <= itemCount) {
                "Optimal item count must be between 1 and total item count"
            }

            val weights = IntArray(itemCount)
            val values = IntArray(itemCount)

            val optimalSelection = BooleanArray(itemCount) { i -> i < optimalItemCount }

            for (i in 0 until optimalItemCount) {
                weights[i] = baseWeight + (i * 2)
                values[i] = baseValue + (i * 5)
            }

            for (i in optimalItemCount until itemCount) {
                weights[i] = baseWeight + ((i - optimalItemCount) * 3) + 5
                values[i] = baseValue + ((i - optimalItemCount) * 2)
            }

            val optimalWeight = (0 until optimalItemCount).sumOf { weights[it] }
            val capacity = optimalWeight + (baseWeight / 2)

            val knapsackInstance = KnapsackInstance(itemCount, weights, values, capacity, name)

            val optimalSolution = KnapsackSolution(
                knapsackInstance,
                BinaryVectorRepresentation(optimalSelection)
            )
            knapsackInstance.optimalSolution = optimalSolution

            return knapsackInstance
        }

        fun generateSimpleInstanceWithKnownOptimum(
            itemCount: Int = 10,
            name: String? = null
        ): KnapsackInstance {
            require(itemCount > 0) { "Item count must be positive" }

            val weights = IntArray(itemCount)
            val values = IntArray(itemCount)

            val halfCount = itemCount / 2

            for (i in 0 until halfCount) {
                weights[i] = 10 + i
                values[i] = (10 + i) * 5
            }

            for (i in halfCount until itemCount) {
                weights[i] = 10 + (i - halfCount)
                values[i] = 10 + (i - halfCount)
            }

            val capacity = (0 until halfCount).sumOf { weights[it] }

            val knapsackInstance = KnapsackInstance(itemCount, weights, values, capacity, name)

            val optimalSelection = BooleanArray(itemCount) { i -> i < halfCount }
            val optimalSolution = KnapsackSolution(
                knapsackInstance,
                BinaryVectorRepresentation(optimalSelection)
            )
            knapsackInstance.optimalSolution = optimalSolution

            return knapsackInstance
        }

        fun generateRandomInstance(
            itemCount: Int = 20,
            capacityRatio: Double = 0.5,
            maxWeight: Int = 50,
            maxValue: Int = 100,
            name: String? = null
        ): KnapsackInstance {
            require(itemCount > 0) { "Item count must be positive" }
            require(capacityRatio > 0.0 && capacityRatio <= 1.0) { "Capacity ratio must be between 0 and 1" }
            require(maxWeight > 0) { "Max weight must be positive" }
            require(maxValue > 0) { "Max value must be positive" }

            val weights = IntArray(itemCount) { (1..maxWeight).random() }
            val values = IntArray(itemCount) { (1..maxValue).random() }

            val totalWeight = weights.sum()
            val capacity = (totalWeight * capacityRatio).toInt().coerceAtLeast(weights.minOrNull() ?: 1)

            return KnapsackInstance(itemCount, weights, values, capacity, name)
        }

        fun solveOptimally(instance: KnapsackInstance): KnapsackSolution? {
            val n = instance.instanceSize
            val W = instance.capacity

            // For very large instances, return null to avoid memory issues
            if (n > 30 || W > 10000) return null

            val dp = Array(n + 1) { IntArray(W + 1) { 0 } }

            for (i in 1..n) {
                for (w in 0..W) {
                    val weight = instance.getWeight(i - 1)
                    val value = instance.getValue(i - 1)

                    dp[i][w] = dp[i - 1][w]

                    if (weight <= w) {
                        dp[i][w] = maxOf(dp[i][w], dp[i - 1][w - weight] + value)
                    }
                }
            }

            val selection = BooleanArray(n) { false }
            var w = W
            for (i in n downTo 1) {
                if (dp[i][w] != dp[i - 1][w]) {
                    selection[i - 1] = true
                    w -= instance.getWeight(i - 1)
                }
            }

            return KnapsackSolution(instance, BinaryVectorRepresentation(selection))
        }
    }
}