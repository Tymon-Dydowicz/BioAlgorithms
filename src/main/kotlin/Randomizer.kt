object Randomizer {
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
}