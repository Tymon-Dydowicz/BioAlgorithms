object Timer {
    class TimeResult(val totalTime: Long, val averageTime: Long, val executions: Int){
        override fun toString(): String {
            return "Total time: $totalTime ms\nAverage time: $averageTime ms\nExecutions: $executions"
        }
    }

    fun calculateExectuionTime(block: () -> Unit, timePrecision: Double, executionsPrecision: Int): TimeResult {
        val startTime = System.currentTimeMillis()
        var counter = 0
        while (System.currentTimeMillis() - startTime < timePrecision * 1000 && counter <= executionsPrecision) {
            block()
            counter++
        }
        val totalTime = System.currentTimeMillis() - startTime

        return TimeResult(totalTime, totalTime/counter, counter)
    }
}