package Util

object Randomizer {

    fun getRandomIndex(length: Int): Int {
        return (0 until length).random()
    }

    fun getRandomPair(length: Int): Pair<Int, Int> {
        val first = (0 until length).random()
        val offset = (0 until length).random()
        val second = (first + offset) % length
        return Pair(first, second)
    }

    fun randomShuffle(array: Array<Int>): Array<Int> {
        val arrayCopy = array.copyOf()
        for (i in (0 until arrayCopy.size - 1)) {
            val swappableIndex = arrayCopy.size - 1 - i
            val randomIndex = (0..arrayCopy.size - 2 - i).random()

            val temp = arrayCopy[swappableIndex]
            arrayCopy[swappableIndex] = arrayCopy[randomIndex]
            arrayCopy[randomIndex] = temp
        }

        return arrayCopy
    }

    fun testRandomizer(){
        val length = 100

        val (first, second) = Randomizer.getRandomPair(length)
        println("$first $second")

        val array = Array(length) { it }
        val shuffledArray = Randomizer.randomShuffle(array)
        println(array.joinToString(" "))
        println(shuffledArray.joinToString(" "))
    }
}