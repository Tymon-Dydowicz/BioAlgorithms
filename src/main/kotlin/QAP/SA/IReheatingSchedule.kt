package QAP.SA

interface IReheatingSchedule {
    fun shouldReheat(iteration: Int, temperature: Double, currentEnergy: Double): Boolean
    fun reheat(initialTemperature: Double, currentTemperature: Double): Double

    class PeriodicReheat(private val period: Int, private val factor: Double = 0.5) : IReheatingSchedule {
        override fun shouldReheat(iteration: Int, temperature: Double, currentEnergy: Double): Boolean {
            return iteration % period == 0 && iteration > 0
        }

        override fun reheat(initialTemperature: Double, currentTemperature: Double): Double {
            return initialTemperature * factor
        }
    }

    class OscilatingReheat(private val threshold: Double = 0.1, private val factor: Double = 0.5) : IReheatingSchedule {
        override fun shouldReheat(iteration: Int, temperature: Double, currentEnergy: Double): Boolean {
            return temperature < threshold
        }

        override fun reheat(initialTemperature: Double, currentTemperature: Double): Double {
           return initialTemperature * factor
        }
    }

    class NoReheat : IReheatingSchedule {
        override fun shouldReheat(iteration: Int, temperature: Double, currentEnergy: Double): Boolean {
            return false
        }

        override fun reheat(initialTemperature: Double, currentTemperature: Double): Double {
            return currentTemperature
        }
    }

}
