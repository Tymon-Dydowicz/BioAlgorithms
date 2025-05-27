package Util

object Timer {
    class TimeResult(val totalTime: Long, val averageTime: Long, val executions: Int){
        override fun toString(): String {
            return "Total time: $totalTime ms\nAverage time: $averageTime ms\nExecutions: $executions"
        }
    }

    fun calculateExectuionTime(block: () -> Unit, timePrecision: Double, executionsPrecision: Int): TimeResult {
        val startTime = System.nanoTime()
        var counter = 0
        while (System.nanoTime() - startTime < timePrecision * 1_000_000_000 && counter <= executionsPrecision) {
            block()
            counter++
        }
        val totalTime = System.nanoTime() - startTime

        return TimeResult(totalTime, totalTime/counter, counter)
    }

    fun testTimer() {
        val length = 100

        fun testFunction() {
            val array = Array(length) { it }
            Thread.sleep(100)
            Randomizer.randomShuffle(array)
        }

        val timeResult = Timer.calculateExectuionTime(::testFunction, 2.8, 100)
        println(timeResult)
    }
}