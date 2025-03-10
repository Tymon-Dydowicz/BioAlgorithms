import java.lang.Thread.sleep

fun main(args: Array<String>) {
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