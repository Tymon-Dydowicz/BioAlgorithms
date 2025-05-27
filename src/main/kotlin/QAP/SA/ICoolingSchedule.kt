package QAP.SA

import kotlin.math.ln

interface ICoolingSchedule {
    fun shouldCool(iteration: Int, temperature: Double): Boolean
    fun cool(currentTemperature: Double): Double

    class Exponential(private val coolingIterations: Int, private val alpha: Double = 0.95) : ICoolingSchedule {
        override fun shouldCool(iteration: Int, temperature: Double): Boolean {
            return iteration % coolingIterations == 0 && iteration > 0
        }

        override fun cool(currentTemperature: Double): Double =
            currentTemperature * alpha
    }

    class Linear(private val coolingIterations: Int, private val delta: Double = 1.0) : ICoolingSchedule {
        override fun shouldCool(iteration: Int, temperature: Double): Boolean {
            return iteration % coolingIterations == 0 && iteration > 0
        }
        override fun cool(currentTemperature: Double): Double =
            (currentTemperature - delta).coerceAtLeast(0.0)
    }

    class Logarithmic(private val coolingIterations: Int, private val gamma: Double = 1.0) : ICoolingSchedule {
        override fun shouldCool(iteration: Int, temperature: Double): Boolean {
            return iteration % coolingIterations == 0 && iteration > 0
        }
        override fun cool(currentTemperature: Double): Double =
            currentTemperature / (1 + gamma * ln(1 + currentTemperature))
    }
}
