package LocalSearch

import LocalSearch.BaseInterfaces.ISolutionBase
import LocalSearch.Representations.ISolution

interface IPerturbation {
    fun destroy(solution: ISolutionBase): ISolutionBase
    fun repair(solution: ISolutionBase): ISolutionBase
    fun getName(): String

    class NoPerturbation: IPerturbation {
        override fun destroy(solution: ISolutionBase): ISolutionBase {
            return solution
        }

        override fun repair(solution: ISolutionBase): ISolutionBase {
            return solution
        }

        override fun getName(): String {
            return "NoPerturbation"
        }
    }
}