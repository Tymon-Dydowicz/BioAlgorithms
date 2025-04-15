import Analysis.*
import Enums.AlgorithmType
import Enums.AnalysisType
import QAP.QAPInstance
import QAP.QAPOptimizer
import QAP.QAPSolution
import QAP.runOptimization
import Results.*
import Util.*

typealias OptimizationFunction = (QAPInstance, Int, Int) -> OptimizationResult
typealias MultiStartOpimizationFunction = (QAPInstance, Int, Int) -> List<OptimizationResult>

fun main(args: Array<String>) {
    val dataLocation = "data/qapdata/"

    val instanceName = "tai12a.dat"
    val instanceNameClean = instanceName.split(".")[0]
    val instance = DataLoader.loadUnformattedInstance(dataLocation, instanceName)
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

    val startsSimilarity = 2
    val algorithmSimilarity = "heurGMSLS"

    val config = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.RANDOM_STEEPEST_MS_LOCAL_SEARCH,
        time = 5000,
        multiStarts = 300,
        algorithmRuns = startsSimilarity,
    )
    runAnalysis(
        config = config,
        analysisType = AnalysisType.RESTARTS,
    )

//    analyzeSimilarity(instance, instanceNameClean, algorithmSimilarity, startsSimilarity)

}

fun QAPExperiment(dataLocation: String, instanceName: String, repeats: Int) {
    val instanceNameClean = instanceName.split(".")[0]

    val instance = DataLoader.loadUnformattedInstance(dataLocation, instanceName)
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

fun evaluateAlgorithms(
    configs: List<OptimizationConfig>
) {
    for (config in configs) {
        val runner = runOptimization(config)
        val results = runner.results

        val avgResult = AvgOptimizationResult.fromResults(config.algorithmType.toString(), results)

        avgResult.describe()
        avgResult.exportToCSV("results/${config.instance.instanceName}_${config.algorithmType}_avg_results.csv")
    }
}

fun checkLongestExecutionTime(config: OptimizationConfig): Long {
    val runner = runOptimization(config)
    val results = runner.results

    return results.maxOf { it.runtime }
}

fun QAPBenchmark(dataLocation: String, instanceName: String, repeats: Int){
    val instance = DataLoader.loadUnformattedInstance(dataLocation, instanceName)
    instance.describe()
    OptimalSolutions.setOptimumForInstance(instance, instanceName)

    val heurConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.HEURSITC,
        time = 5000,
        algorithmRuns = repeats,
    )

    val randGLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.RANDOM_GREEDY_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
    )

    val randSLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.RANDOM_STEEPEST_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
    )

    val heurGLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.HEURISTIC_GREEDY_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
    )

    val heurSLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.HEURISTIC_STEEPEST_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
    )

    val randGMSLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.RANDOM_GREEDY_MS_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

    val heurGMSLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.HEURISTIC_GREEDY_MS_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

    val randSMSLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.RANDOM_STEEPEST_MS_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

    val heurSMSLSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.HEURISTIC_STEEPEST_MS_LOCAL_SEARCH,
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

    val longestExecutionTime = checkLongestExecutionTime(heurSMSLSConfig)

    val RWConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.RANDOM_WALK,
        time = longestExecutionTime,
        algorithmRuns = repeats,
    )

    val RSConfig = OptimizationConfig(
        instance = instance,
        algorithmType = AlgorithmType.RANDOM_SEARCH,
        time = longestExecutionTime,
        algorithmRuns = repeats,
    )

    val configs = listOf(
        heurConfig,
        randGLSConfig,
        randSLSConfig,
        heurGLSConfig,
        heurSLSConfig,
        randGMSLSConfig,
        heurGMSLSConfig,
        randSMSLSConfig,
        heurSMSLSConfig,
        RWConfig,
        RSConfig
    )

    evaluateAlgorithms(configs)
}