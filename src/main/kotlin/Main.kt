import QAP.QAPOptimizer
import QAP.QAPSolution
import Util.DataLoader
import Util.Randomizer
import Util.Timer
import java.lang.Thread.sleep

fun main(args: Array<String>) {
    bioExperiment()

}

fun bioExperiment() {
    val dataLocation = "data/qapdata/"

//    val instanceName = "bur26g.dat"
    val instanceName = "chr12a.dat"
    val instance = DataLoader.loadUnformattedInstance(dataLocation + instanceName)
    instance.describe()

    val randomSolution = QAPOptimizer.generateRandomSolution(instance)
    randomSolution.describe()

    val greedySolution = QAPOptimizer.generateHeuristicSolution(instance)
    greedySolution.describe()

//    chr12a.dat
//    (7,5,12,2,1,3,9,11,10,6,8,4)
//    (6,4,11,1,0,2,8,10,9,5,7,3)

//    els19.dat
//    (9,10,7,18,14,19,13,17,6,11,4,5,12,8,15,16,1,2,3)
//    (8,9,6,17,13,18,12,16,5,10,3,4,11,7,14,15,0,1,2)

//    had12.dat
//    (3,10,11,2,12,5,6,7,8,1,4,9)
//    (2,9,10,1,11,4,5,6,7,0,3,8)
    val optimalSolution = intArrayOf(6,4,11,1,0,2,8,10,9,5,7,3)
    val optimalQAPSolution = QAPSolution(instance, optimalSolution)
    optimalQAPSolution.describe()
}
fun test() {
    val length = 100
    val (first, second) = Randomizer.getRandomPair(length)
    println("$first $second")

    val array = Array(length) { it }
    val shuffledArray = Randomizer.randomShuffle(array)
    println(array.joinToString(" "))
    println(shuffledArray.joinToString(" "))

    fun testFunction() {
        val array = Array(length) { it }
        sleep(100)
        Randomizer.randomShuffle(array)
    }

    val timeResult = Timer.calculateExectuionTime(::testFunction, 2.8, 100)
    println(timeResult)
}