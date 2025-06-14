package LocalSearch

import QAP.QAPSolution

interface IPerturbation {
    fun destroy(solution: ISolution): ISolution
    fun repair(solution: ISolution): ISolution
    fun getName(): String

    class NoPerturbation: IPerturbation {
        override fun destroy(solution: ISolution): ISolution {
            return solution
        }

        override fun repair(solution: ISolution): ISolution {
            return solution
        }

        override fun getName(): String {
            return "NoPerturbation"
        }
    }
}