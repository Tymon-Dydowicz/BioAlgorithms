package QAP

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

        // TODO Test if this works

        return cost
    }
}