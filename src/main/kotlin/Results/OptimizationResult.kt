package Results

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.Representations.ISolution
import java.io.File

class OptimizationResult(val name: String, val instanceSize: Int) {
    var runtime: Long = 0
//    var solutions: MutableList<QAPSolution> = mutableListOf()
    var solutionSteps: MutableList<Int> = mutableListOf()
    val solutionTimestamps: MutableList<Long> = mutableListOf()
    var posSteps: Int = 0
    var negSteps: Int = 0
    var totalSteps: Int = 0
    var algorithmLoops: Int = 0
    var initialSolution: ISolutionBase? = null
    var bestSolution: ISolutionBase? = null
    var timeSinceLastImprovement: Long = 0
    var optimum: Int = 0
    var evaluatedSolutions: Long = 0

    fun describe() {
        print(
            "-- Optimization Result | Size ($instanceSize)-- \n" +
                "Method: $name \n" +
                "Runtime: $runtime ns\n" +
                "Positive Steps $posSteps | Negative Steps $negSteps | Total Steps $totalSteps \n" +
                "Initial Solution: ${initialSolution!!.solutionCost} \n" +
                "Optimum: ${optimum} | ${bestSolution!!.solutionCost} :Best Solution cost \n" +
                "Time since last improvement: $timeSinceLastImprovement ns\n" +
                "Evaluated Solutions: $evaluatedSolutions \n" +
                "Gap to optimum: ${((bestSolution!!.solutionCost.toDouble() / optimum) - 1) * 100} \n" +
        "-------------------------------------- \n")
//                "Cost changes $solutionSteps \n")
    }
//    fun addSolution(solution: QAPSolution) {
//        solutions.add(solution)
//    }

    fun addStep(cost: Int, time: Long) {
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
        this.solutionTimestamps.add(time)
    }

    fun setRuntimeIn(runtime: Long) {
        this.runtime = runtime
    }

    fun setBestSolutionIn(bestSolution: ISolutionBase) {
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
        csvContent.appendLine("metadata,gapToOptimum,${((bestSolution!!.solutionCost.toDouble() / optimum) - 1) * 100}")
        csvContent.appendLine("metadata,evaluatedSolutions,$evaluatedSolutions")

        solutionSteps.forEachIndexed { index, cost ->
            csvContent.appendLine("step,$index,$cost")
        }

        file.writeText(csvContent.toString())
    }
}