import LocalSearch.LocalSearchConfig
import LocalSearch.IStoppingCriterion
import QAP.*
import QAP.SA.ICoolingSchedule
import QAP.SA.IReheatingSchedule
import QAP.SA.TemperatureWrapper
import QAP.TabuSearch.IAspirationCriterion
import QAP.TabuSearch.IMoveFeatureExtractor
import QAP.TabuSearch.ITabuList
import QAP.TabuSearch.ITabuTenureSchedule
import QAP.Test.*
import Results.*
import Util.*

typealias OptimizationFunction = (QAPInstance, Int, Int) -> OptimizationResult
typealias MultiStartOpimizationFunction = (QAPInstance, Int, Int) -> List<OptimizationResult>

fun main(args: Array<String>) {
    val dataLocation = "data/qapdata/"
    val instanceName = "tai12a.dat"
    val repeats = 20
    val instanceNameClean = instanceName.split(".")[0]

//    QAPBenchmark(dataLocation, instanceName, repeats)

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

    val instance = DataLoader.loadUnformattedInstance(dataLocation, instanceName)
    instance.describe()
    OptimalSolutions.setOptimumForInstance(instance, instanceName)

    val repeatsUntil = 500
    val algorithmRestarts = "heurSMSLS"

    val startsTrend = 300
    val algorithmTrend = "heurGMSLS"

    val startsSimilarity = 1
    val algorithmSimilarity = "heurGMSLS"

//    val config = OptimizationConfig(
//        instance = instance,
//        localSearchConfig = LocalSearchFactory.createSteepestLocalSearch(5000, false),
//        time = 5000,
//        multiStarts = 300,
//        algorithmRuns = startsSimilarity,
//    )
//    runAnalysis(
//        config = config,
//        analysisType = AnalysisType.RESTARTS,
//    )

    runTabuSearch(instance)

}

fun runTabuSearch(instance: QAPInstance) {
    val tabuConfig = LocalSearchConfig(
        RandomSolutionGenerator(),
        SwapNeighborhoodExplorer(),
        TabuSearchAcceptance(
            ITabuList.AttributeBasedTabuList(IMoveFeatureExtractor.Default()),
            IAspirationCriterion.BestMove(),
            ITabuTenureSchedule.SizeBasedTenure(132)
//            ITabuTenureSchedule.StaticTenure(50), // Static 50 finds the optimum very often
        ),
        IStoppingCriterion.temperatureThreshold(100.0) or
                IStoppingCriterion.maxRuntime(100) or
                IStoppingCriterion.maxIterations(1000000),
    )
    val LSTabuConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = tabuConfig,
        time = 5000,
        algorithmRuns = 50,
    )

    evaluateAlgorithms(listOf(LSTabuConfig))
}

fun runSimulateAnnealing(instance: QAPInstance) {
    val initialTemp = TemperatureWrapper.calculateInitialTemperature(RandomSolutionGenerator().generate(instance), SwapNeighborhoodExplorer())
    println("Initial temperature: $initialTemp")
    val temperatureWrapper = TemperatureWrapper(initialTemp)

    val SAConfig = LocalSearchConfig(
        RandomSolutionGenerator(),
        SwapNeighborhoodExplorer(),
        SimulatedAnnealingAcceptance(temperatureWrapper, IReheatingSchedule.PeriodicReheat(20, 0.3), ICoolingSchedule.Exponential(0.95)),
        IStoppingCriterion.temperatureThreshold(100.0) or
                IStoppingCriterion.maxRuntime(5000) or
                IStoppingCriterion.maxIterations(1000000),
    )
    val LSSAConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = SAConfig,
        time = 5000,
        algorithmRuns = 1,
    )

    evaluateAlgorithms(listOf(LSSAConfig))
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
    OptimalSolutions.setOptimumForInstance(instance, instanceName)
    instance.describe()

//    val heurConfig = OptimizationConfig(
//        instance = instance,
//        algorithmType = AlgorithmType.HEURSITC,
//        time = 5000,
//        algorithmRuns = repeats,
//    )

    val randGLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createGreedyLocalSearch(5000, false),
        time = 5000,
        algorithmRuns = repeats,
    )

    val randSLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createSteepestLocalSearch(5000, false),
        time = 5000,
        algorithmRuns = repeats,
    )

    val heurGLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createGreedyLocalSearch(5000, true),
        time = 5000,
        algorithmRuns = repeats,
    )

    val heurSLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createSteepestLocalSearch(5000, true),
        time = 5000,
        algorithmRuns = repeats,
    )

    val randGMSLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createGreedyLocalSearch(5000, false),
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

    val heurGMSLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createGreedyLocalSearch(5000, true),
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

    val randSMSLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createSteepestLocalSearch(5000, false),
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

    val heurSMSLSConfig = OptimizationConfig(
        instance = instance,
        localSearchConfig = LocalSearchFactory.createSteepestLocalSearch(5000, true),
        time = 5000,
        algorithmRuns = repeats,
        multiStarts = 200,
    )

//    val longestExecutionTime = checkLongestExecutionTime(heurSMSLSConfig)

//    val RWConfig = OptimizationConfig(
//        instance = instance,
//        algorithmType = AlgorithmType.RANDOM_WALK,
//        time = longestExecutionTime,
//        algorithmRuns = repeats,
//    )
//
//    val RSConfig = OptimizationConfig(
//        instance = instance,
//        algorithmType = AlgorithmType.RANDOM_SEARCH,
//        time = longestExecutionTime,
//        algorithmRuns = repeats,
//    )

    val configs = listOf(
//        heurConfig,
//        randGLSConfig,
//        randSLSConfig,
//        heurGLSConfig,
//        heurSLSConfig,
        randGMSLSConfig,
        heurGMSLSConfig,
        randSMSLSConfig,
        heurSMSLSConfig,
//        RWConfig,
//        RSConfig
    )

    evaluateAlgorithms(configs)
}