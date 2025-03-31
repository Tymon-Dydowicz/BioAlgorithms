package Util

import QAP.QAPSolution
import java.io.File

class OptimizationResult(val name: String) {
    var runtime: Long = 0
    var solutionSteps: MutableList<Int> = mutableListOf()
    var posSteps: Int = 0
    var negSteps: Int = 0
    var bestSolution: QAPSolution? = null
    var timeSinceLastImprovement: Long = 0
    var optimum: Int = 0
    var evaluatedSolutions: Int = 0

    fun describe() {
        print("Method: $name \n" +
                "Runtime: $runtime \n" +
                "Positive Steps $posSteps | Negative Steps $negSteps \n" +
                "Best Solution cost: ${bestSolution!!.solutionCost} \n")
//                "Cost changes $solutionSteps \n")
    }

    fun addStep(cost: Int) {
        var lastCost = Int.MAX_VALUE
        if (solutionSteps.isNotEmpty()) {
            lastCost = solutionSteps.last()
        }

        if (cost >= lastCost) {
            negSteps += 1
        } else {
            posSteps += 1
        }
        solutionSteps.add(cost)
    }

    fun setRuntimeIn(runtime: Long) {
        this.runtime = runtime
    }

    fun setBestSolutionIn(bestSolution: QAPSolution) {
        this.bestSolution = bestSolution
    }

    fun setLastImprovementIn(time: Long) {
        this.timeSinceLastImprovement = time
    }

    fun setOptimumIn(optimum: Int) {
        this.optimum = optimum
    }

    fun increaseEvaluatedSolutions(increment: Int) {
        evaluatedSolutions += increment
    }

    fun exportToCSV(filename: String = "results.csv") {
        val file = File(filename)

        val csvContent = StringBuilder()
        csvContent.appendLine("type,key,value")
        csvContent.appendLine("metadata,runtime,$runtime")
        csvContent.appendLine("metadata,posSteps,$posSteps")
        csvContent.appendLine("metadata,negSteps,$negSteps")
        csvContent.appendLine("metadata,timeSinceLastImprovement,$timeSinceLastImprovement")
        csvContent.appendLine("metadata,optimum,$optimum")
        csvContent.appendLine("metadata,evaluatedSolutions,$evaluatedSolutions")

        solutionSteps.forEachIndexed { index, cost ->
            csvContent.appendLine("step,$index,$cost")
        }

        file.writeText(csvContent.toString())
    }
}