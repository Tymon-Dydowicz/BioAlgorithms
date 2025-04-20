package LocalSearch

import QAP.QAPSolution

interface IPerturbation {
    fun destroy(solution: QAPSolution): QAPSolution
    fun repair(solution: QAPSolution): QAPSolution
    fun getName(): String

    class NoPerturbation: IPerturbation {
        override fun destroy(solution: QAPSolution): QAPSolution {
            return solution
        }

        override fun repair(solution: QAPSolution): QAPSolution {
            return solution
        }

        override fun getName(): String {
            return "NoPerturbation"
        }
    }
}