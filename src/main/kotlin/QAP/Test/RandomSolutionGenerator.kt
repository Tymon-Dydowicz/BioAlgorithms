package QAP.Test

import LocalSearch.ISolutionGenerator
import QAP.QAPInstance
import QAP.QAPSolution
import Util.Randomizer

class RandomSolutionGenerator : ISolutionGenerator {
    override fun generate(instance: QAPInstance): QAPSolution {
        val locations = Array(instance.instanceSize) { it }
        val randomSolution = Randomizer.randomShuffle(locations)

        return QAPSolution(instance, randomSolution.toIntArray())
    }

    override fun getName(): String {
        return "Random"
    }
}