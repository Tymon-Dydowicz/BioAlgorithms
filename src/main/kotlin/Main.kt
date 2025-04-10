import QAP.QAPInstance
import QAP.QAPOptimizer
import QAP.QAPSolution
import Results.*
import Util.*

typealias OptimizationFunction = (QAPInstance, Int, Int) -> OptimizationResult
typealias MultiStartOpimizationFunction = (QAPInstance, Int, Int) -> List<OptimizationResult>

fun main(args: Array<String>) {
    val dataLocation = "data/qapdata/"

    val instanceName = "nug28.dat"
    val instanceNameClean = instanceName.split(".")[0]
    val instance = DataLoader.loadUnformattedInstance(dataLocation + instanceName)
    instance.describe()
    OptimalSolutions.setOptimumForInstance(instance, instanceName)

    val repeats = 20

    val instances = listOf(
        "tai12a.dat",
//        "els19.dat",
//        "rou20.dat",
//        "bur26a.dat",
//        "nug28.dat",
//        "kra32.dat",
//        "esc64a.dat",
//        "lipa90b.dat",
    )


//    for (instanceName in instances) {
//        val instanceNameClean = instanceName.split(".")[0]
//
//        val instance = DataLoader.loadUnformattedInstance(dataLocation + instanceName)
//        instance.describe()
//        OptimalSolutions.setOptimumForInstance(instance, instanceName)
//
//        evaluateAlgorithms(instance, instanceNameClean, repeats)
//    }


    val repeatsUntil = 500
    val algorithmRestarts = "heurSMSLS"
//    analyzeRestarts(instance, instanceNameClean, algorithmRestarts, repeatsUntil)

    val startsTrend = 300
    val algorithmTrend = "heurGMSLS"
//    analyzeTrend(instance, instanceNameClean, algorithmTrend, startsTrend)

    val startsSimilarity = 300
    val algorithmSimilarity = "heurGMSLS"
    analyzeSimilarity(instance, instanceNameClean, algorithmSimilarity, startsSimilarity)

}

fun QAPExperiment(dataLocation: String, instanceName: String, repeats: Int) {
    val instanceNameClean = instanceName.split(".")[0]

    val instance = DataLoader.loadUnformattedInstance(dataLocation + instanceName)
    instance.describe()

    println("Optimal Solution:")
    val optimalSolution: IntArray
    val optimalQAPSolution: QAPSolution
    if (instanceName == "lipa90b.dat") {
        optimalQAPSolution = QAPOptimizer.generateRandomSolution(instance)
        optimalQAPSolution.overrideCost(12490441)
    } else {
        optimalSolution = OptimalSolutions.getPermutation(instanceName)
        optimalQAPSolution = QAPSolution(instance, optimalSolution)
    }
    optimalQAPSolution.describe()

    println("Random Solution:")
    val randomSolution = QAPOptimizer.generateRandomSolution(instance)
    randomSolution.describe()

    println("Greedy Solution:")
    val greedySolution = QAPOptimizer.generateHeuristicSolution(instance)
    greedySolution.describe()



    println("Random Walk:")
    val RWResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val RWResult = QAPOptimizer.performRandomWalk(instance, 150)
        RWResult.setOptimumIn(optimalQAPSolution.solutionCost)
        RWResults.add(RWResult)
    }
    val avgRWResult = AvgOptimizationResult.fromResults("RandomWalk", RWResults)
    avgRWResult.describe()
    avgRWResult.exportToCSV("results/" + instanceNameClean + "_RW_avg_results.csv")

    println("Random Search:")
    val RSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val RSResult = QAPOptimizer.performRandomSearch(instance, 150)
        RSResult.setOptimumIn(optimalQAPSolution.solutionCost)
        RSResults.add(RSResult)
    }
    val avgRSResult = AvgOptimizationResult.fromResults("RandomSearch", RSResults)
    avgRSResult.describe()
    avgRSResult.exportToCSV("results/" + instanceNameClean + "_RS_avg_results.csv")

    println("Heuristic Greedy Local Search:")
    val greedyHeurLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val gLSResult = QAPOptimizer.performHeuristicLocalSearchGreedy(instance, 5000)
        gLSResult.setOptimumIn(optimalQAPSolution.solutionCost)
        greedyHeurLSResults.add(gLSResult)
    }
    val avgHeurGLSResult = AvgOptimizationResult.fromResults("GreedyLocalSearch", greedyHeurLSResults)
    avgHeurGLSResult.describe()
    avgHeurGLSResult.exportToCSV("results/" + instanceNameClean + "_heurGLS_avg_results.csv")

    println("Random Greedy Local Search:")
    val greedyRandLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val gLSResult = QAPOptimizer.performHeuristicLocalSearchGreedy(instance, 5000)
        gLSResult.setOptimumIn(optimalQAPSolution.solutionCost)
        greedyRandLSResults.add(gLSResult)
    }
    val avgRandGLSResult = AvgOptimizationResult.fromResults("GreedyLocalSearch", greedyRandLSResults)
    avgRandGLSResult.describe()
    avgRandGLSResult.exportToCSV("results/" + instanceNameClean + "_randGLS_avg_results.csv")

    println("Heuristic Steepest Local Search:")
    val sHeurLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val sLSResult = QAPOptimizer.performHeuristicLocalSearchSteepest(instance, 5000)
        sLSResult.setOptimumIn(optimalQAPSolution.solutionCost)
        sHeurLSResults.add(sLSResult)
    }
    val avgHeurSLSResult = AvgOptimizationResult.fromResults("SteepestLocalSearch", sHeurLSResults)
    avgHeurSLSResult.describe()
    avgHeurSLSResult.exportToCSV("results/" + instanceNameClean + "_heurSLS_avg_results.csv")

    println("Heuristic Steepest Local Search:")
    val sRandLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val sLSResult = QAPOptimizer.performHeuristicLocalSearchSteepest(instance, 5000)
        sLSResult.setOptimumIn(optimalQAPSolution.solutionCost)
        sRandLSResults.add(sLSResult)
    }
    val avgRandSLSResult = AvgOptimizationResult.fromResults("SteepestLocalSearch", sRandLSResults)
    avgRandSLSResult.describe()
    avgRandSLSResult.exportToCSV("results/" + instanceNameClean + "_randSLS_avg_results.csv")

    println("Heuristic Steepest Multi Start Local Search:")
    val sHeurMSLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val sMSLSResult = QAPOptimizer.performHeuristicMultiLSSteepest(instance, 100, 5000)
        sMSLSResult.forEach { it.setOptimumIn(optimalQAPSolution.solutionCost) }
        val BestMSLSSolution = sMSLSResult.maxByOrNull { it.solutionSteps.size }!!
        sHeurMSLSResults.add(BestMSLSSolution)
    }
    val avgHeurSMSLSResult = AvgOptimizationResult.fromResults("SteepestMultiStartLocalSearch", sHeurMSLSResults)
    avgHeurSMSLSResult.describe()
    avgHeurSMSLSResult.exportToCSV("results/" + instanceNameClean + "_heurSMSLS_avg_results.csv")

    println("Random Steepest Multi Start Local Search:")
    val sRandMSLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val sMSLSResult = QAPOptimizer.performHeuristicMultiLSSteepest(instance, 100, 5000)
        sMSLSResult.forEach { it.setOptimumIn(optimalQAPSolution.solutionCost) }
        val BestMSLSSolution = sMSLSResult.maxByOrNull { it.solutionSteps.size }!!
        sRandMSLSResults.add(BestMSLSSolution)
    }
    val avgRandSMSLSResult = AvgOptimizationResult.fromResults("SteepestMultiStartLocalSearch", sRandMSLSResults)
    avgRandSMSLSResult.describe()
    avgRandSMSLSResult.exportToCSV("results/" + instanceNameClean + "_randSMSLS_avg_results.csv")

    println("Greedy Multi Start Local Search:")
    val gHeurMSLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val gMSLSResult = QAPOptimizer.performHeuristicMultiLSGreedy(instance, 100, 5000)
        gMSLSResult.forEach { it.setOptimumIn(optimalQAPSolution.solutionCost) }
        val BestMSLSGreedySolution = gMSLSResult.maxByOrNull { it.solutionSteps.size }!!
        gHeurMSLSResults.add(BestMSLSGreedySolution)
    }
    val avgHeurGMSLSResult = AvgOptimizationResult.fromResults("GreedyMultiStartLocalSearch", gHeurMSLSResults)
    avgHeurGMSLSResult.describe()
    avgHeurGMSLSResult.exportToCSV("results/" + instanceNameClean + "_heurGMSLS_avg_results.csv")

    println("Random Multi Start Local Search:")
    val gRandMSLSResults = mutableListOf<OptimizationResult>()
    for (i in 0 until repeats) {
        val gMSLSResult = QAPOptimizer.performHeuristicMultiLSGreedy(instance, 100, 5000)
        gMSLSResult.forEach { it.setOptimumIn(optimalQAPSolution.solutionCost) }
        val BestMSLSGreedySolution = gMSLSResult.maxByOrNull { it.solutionSteps.size }!!
        gRandMSLSResults.add(BestMSLSGreedySolution)
    }
    val avgRandGMSLSResult = AvgOptimizationResult.fromResults("GreedyMultiStartLocalSearch", gRandMSLSResults)
    avgRandGMSLSResult.describe()
    avgRandGMSLSResult.exportToCSV("results/" + instanceNameClean + "_randGMSLS_avg_results.csv")
}

fun analyzeSimilarity(
    instance: QAPInstance,
    instanceNameClean: String,
    algorithm: String,
    starts: Int,
) {
    val results: List<OptimizationResult>

    when (algorithm) {
        "randGMSLS" -> {
            results = QAPOptimizer.performRandomMultiLSGreedy(instance, starts, 5000)
        }
        "randSMSLS" -> {
            results = QAPOptimizer.performRandomMultiLSSteepest(instance, starts, 5000)
        }
        "heurGMSLS" -> {
            results = QAPOptimizer.performHeuristicMultiLSGreedy(instance, starts, 5000)
        }
        "heurSMSLS" -> {
            results = QAPOptimizer.performHeuristicMultiLSSteepest(instance, starts, 5000)
        }
        else -> throw IllegalArgumentException("Unknown algorithm: $algorithm")
    }
    val similarityAnalysisReport = SimilarityAnalysisReport(instanceNameClean, instance.optimalSolution!!, results)

    similarityAnalysisReport.exportToCSV("results/${instanceNameClean}_${algorithm}_similarity_analysis.csv")
}

fun analyzeTrend(
    instance: QAPInstance,
    instanceNameClean: String,
    algorithm: String,
    starts: Int,
) {
    val trendAnalysisReport = TrendAnalysisReport(instanceNameClean)
    val results: List<OptimizationResult>

    when (algorithm) {
        "randGMSLS" -> {
            results = QAPOptimizer.performRandomMultiLSGreedy(instance, starts, 5000)
        }
        "randSMSLS" -> {
            results = QAPOptimizer.performRandomMultiLSSteepest(instance, starts, 5000)
        }
        "heurGMSLS" -> {
            results = QAPOptimizer.performHeuristicMultiLSGreedy(instance, starts, 5000)
        }
        "heurSMSLS" -> {
            results = QAPOptimizer.performHeuristicMultiLSSteepest(instance, starts, 5000)
        }
        else -> throw IllegalArgumentException("Unknown algorithm: $algorithm")
    }
    results.forEach { trendAnalysisReport.addStep(it.initialSolution!!, it.bestSolution!!)}

    trendAnalysisReport.exportToCSV("results/${instanceNameClean}_${algorithm}_trend_analysis.csv")
}

fun analyzeRestarts(
    instance: QAPInstance,
    instanceNameClean: String,
    algorithm: String,
    starts: Int,
) {
    val results: List<OptimizationResult>
    when (algorithm) {
        "randGMSLS" -> {
            results = QAPOptimizer.performRandomMultiLSGreedy(instance, starts, 5000)
        }
        "randSMSLS" -> {
            results = QAPOptimizer.performRandomMultiLSSteepest(instance, starts, 5000)
        }
        "heurGMSLS" -> {
            results = QAPOptimizer.performHeuristicMultiLSGreedy(instance, starts, 5000)
        }
        "heurSMSLS" -> {
            results = QAPOptimizer.performHeuristicMultiLSSteepest(instance, starts, 5000)
        }
        else -> throw IllegalArgumentException("Unknown algorithm: $algorithm")
    }

    val restartsAnalysisReport = RestartsAnalysisReport(instanceNameClean, results)

    restartsAnalysisReport.exportToCsv("results/${instanceNameClean}_${algorithm}_restarts_analysis.csv")
}

fun evaluateAlgorithms(
    instance: QAPInstance,
    instanceNameClean: String,
    repeats: Int
) {
    val algorithms = listOf(
        AlgorithmConfig(
            "RW", "Random Walk",
            { inst, maxTime -> QAPOptimizer.performRandomWalk(inst, maxTime) },
            3
        ),
        AlgorithmConfig(
            "RS", "Random Search",
            { inst, maxTime -> QAPOptimizer.performRandomSearch(inst, maxTime) },
            3
        ),
        AlgorithmConfig(
            "heur", "Heuristic",
            { inst, maxTime -> QAPOptimizer.performHeurstic(inst, maxTime) },
            5000
        ),
        AlgorithmConfig(
            "heurGLS", "Heuristic Greedy Local Search",
            { inst, maxTime -> QAPOptimizer.performHeuristicLocalSearchGreedy(inst, maxTime) },
            5000
        ),
        AlgorithmConfig(
            "randGLS", "Random Greedy Local Search",
            { inst, maxTime -> QAPOptimizer.performRandomLocalSearchGreedy(inst, maxTime) },
            5000
        ),
        AlgorithmConfig(
            "heurSLS", "Heuristic Steepest Local Search",
            { inst, maxTime -> QAPOptimizer.performHeuristicLocalSearchSteepest(inst, maxTime) },
            5000
        ),
        AlgorithmConfig(
            "randSLS", "Random Steepest Local Search",
            { inst, maxTime -> QAPOptimizer.performRandomLocalSearchSteepest(inst, maxTime) },
            5000
        )
    )

    val multiStartAlgorithms = listOf(
        MultiStartAlgorithmConfig(
            "heurSMSLS", "Heuristic Steepest Multi Start Local Search",
            { inst, starts, maxTime -> QAPOptimizer.performHeuristicMultiLSSteepest(inst, starts, maxTime) },
            100, 5000
        ),
        MultiStartAlgorithmConfig(
            "randSMSLS", "Random Steepest Multi Start Local Search",
            { inst, starts, maxTime -> QAPOptimizer.performRandomMultiLSSteepest(inst, starts, maxTime) },
            100, 5000
        ),
        MultiStartAlgorithmConfig(
            "heurGMSLS", "Greedy Multi Start Local Search",
            { inst, starts, maxTime -> QAPOptimizer.performHeuristicMultiLSGreedy(inst, starts, maxTime) },
            100, 5000
        ),
        MultiStartAlgorithmConfig(
            "randGMSLS", "Random Multi Start Local Search",
            { inst, starts, maxTime -> QAPOptimizer.performRandomMultiLSGreedy(inst, starts, maxTime) },
            100, 5000
        )
    )

    algorithms.forEach { config ->
        runAlgorithm(instance, config, repeats, instanceNameClean)
    }

    multiStartAlgorithms.forEach { config ->
        runMultiStartAlgorithm(instance, config, repeats, instanceNameClean)
    }
}

fun runAlgorithm(
    instance: QAPInstance,
    config: AlgorithmConfig,
    repeats: Int,
    instanceNameClean: String,
) {
    println("${config.displayName}:")
    val results = mutableListOf<OptimizationResult>()

    for (i in 0 until repeats) {
        val result = config.executor(instance, config.maxTime)
        result.setOptimumIn(instance.optimalSolution!!.solutionCost)
        results.add(result)
    }

    val avgResult = AvgOptimizationResult.fromResults(config.name, results)
    avgResult.describe()
    avgResult.exportToCSV("results/${instanceNameClean}_${config.name}_avg_results.csv")
}

fun runMultiStartAlgorithm(
    instance: QAPInstance,
    config: MultiStartAlgorithmConfig,
    repeats: Int,
    instanceNameClean: String,
) {
    println("${config.displayName}:")
    val results = mutableListOf<OptimizationResult>()

    for (i in 0 until repeats) {
        val multiResults = config.executor(instance, config.startCount, config.maxTime)
        multiResults.forEach { it.setOptimumIn(instance.optimalSolution!!.solutionCost) }
        val bestResult = multiResults.minByOrNull { it.bestSolution!!.solutionCost }!!
        val longestRuntime = multiResults.maxOf { it.runtime }
        val sumOfEvaluatedSolutions = multiResults.sumOf { it.evaluatedSolutions }

        bestResult.runtime = longestRuntime
        bestResult.evaluatedSolutions = sumOfEvaluatedSolutions
        results.add(bestResult)
    }

    val avgResult = AvgOptimizationResult.fromResults(config.name, results)
    avgResult.describe()
    avgResult.exportToCSV("results/${instanceNameClean}_${config.name}_avg_results.csv")
}