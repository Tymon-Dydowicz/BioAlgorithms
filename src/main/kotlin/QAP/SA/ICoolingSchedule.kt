package QAP.SA

import kotlin.math.ln

interface ICoolingSchedule {
    fun cool(currentTemperature: Double): Double

    class Exponential(private val alpha: Double = 0.95) : ICoolingSchedule {
        override fun cool(currentTemperature: Double): Double =
            currentTemperature * alpha
    }

    class Linear(private val delta: Double = 1.0) : ICoolingSchedule {
        override fun cool(currentTemperature: Double): Double =
            (currentTemperature - delta).coerceAtLeast(0.0)
    }

    class Logarithmic(private val gamma: Double = 1.0) : ICoolingSchedule {
        override fun cool(currentTemperature: Double): Double =
            currentTemperature / (1 + gamma * ln(1 + currentTemperature))
    }
}
