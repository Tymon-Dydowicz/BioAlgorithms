package Util

import QAP.QAPInstance
import QAP.QAPOptimizer
import QAP.QAPSolution

object OptimalSolutions {
    val optimalSolutions = mapOf(
        "chr12a.dat" to intArrayOf(7,5,12,2,1,3,9,11,10,6,8,4),
        "had12.dat" to intArrayOf(3,10,11,2,12,5,6,7,8,1,4,9),
        "tai12a.dat" to intArrayOf(8,1,6,2,11,10,3,5,9,7,12,4),
        "els19.dat" to intArrayOf(9,10,7,18,14,19,13,17,6,11,4,5,12,8,15,16,1,2,3),
        "rou20.dat" to intArrayOf(1,19,2,14,10,16,11,20,9,5,7,4,8,18,15,3,12,17,13,6),
        "bur26a.dat" to intArrayOf(26, 15, 11, 7, 4, 12, 13, 2, 6, 18, 1, 5, 9, 21, 8, 14, 3, 20, 19, 25, 17, 10, 16, 24, 23, 22),
        "bur26g.dat" to intArrayOf(22, 11, 2, 23, 13, 25, 24, 8, 1, 21, 20, 4, 7, 18, 12, 15, 9, 19, 5, 26, 16, 6, 14, 3, 17, 10),
        "nug28.dat" to intArrayOf(18,21,9,1,28,20,11,3,13,12,10,19,14,22,15,2,25,16,4,23,7,17,24,26,5,27,8,6),
        "kra32.dat" to intArrayOf(31,23,18,21,22,19,10,11,15,9,30,29,14,12,17,26,27,28,1,7,6,25,5,3,8,24,32,13,2,20,4,16),
        "ste36c.dat" to intArrayOf(3,19,29,21,30,31,13,20,2,12,32,23,22,24,4,1,10,11,15,14,26,27,25,36,35,34,33,5,6,7,8,16,18,17,28,9),
        "esc64a.dat" to intArrayOf(1,2,9,50,3,61,4,62,5,54,64,6,7,52,56,8,55,10,63,18,11,51,12,13,14,15,20,43,16,41,17,47,23,19,24,21,53,22,28,25,26,27,29,60,30,59,31,32,33,34,35,36,37,38,39,40,42,44,45,46,48,49,57,58),
    )

    fun getPermutation(instanceName: String): IntArray {
        if (!optimalSolutions.containsKey(instanceName)) {
            throw IllegalArgumentException("No optimal solution for instance $instanceName")
        }
        return optimalSolutions[instanceName]!!.map { it - 1 }.toIntArray()
    }

    fun getSolution(instanceName: String, instance: QAPInstance): QAPSolution {
        val optimalSolution: IntArray
        val optimalQAPSolution: QAPSolution
        if (instanceName == "lipa90b.dat") {
            optimalQAPSolution = QAPOptimizer.generateRandomSolution(instance)
            optimalQAPSolution.overrideCost(12490441)
        } else {
            optimalSolution = getPermutation(instanceName)
            optimalQAPSolution = QAPSolution(instance, optimalSolution)
        }

        return optimalQAPSolution
    }

    fun setOptimumForInstance(instance: QAPInstance, instanceName: String) {
        instance.optimalSolution = getSolution(instanceName, instance)
    }
}