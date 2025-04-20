package QAP.Test

import LocalSearch.ISolutionGenerator
import QAP.QAPInstance
import QAP.QAPSolution
import QAP.QAPSolutionManager
import Util.Randomizer

class HeuristicSolutionGenerator : ISolutionGenerator {
    override fun generate(instance: QAPInstance): QAPSolution {
        var solution = mutableListOf<Int>()
        var locations = MutableList(instance.instanceSize) { it }
        val intialFacility = Randomizer.getRandomIndex(instance.instanceSize)

        solution.add(intialFacility)
        locations.remove(intialFacility)

        while (locations.isNotEmpty()) {
            val nextFacility = locations.minByOrNull { QAPSolutionManager.calculateAdditionCost(solution, it, instance) }!!

            solution.add(nextFacility)
            locations.remove(nextFacility)
        }

        return QAPSolution(instance, solution.toIntArray())
    }

    override fun getName(): String {
        return "Heuristic"
    }
}