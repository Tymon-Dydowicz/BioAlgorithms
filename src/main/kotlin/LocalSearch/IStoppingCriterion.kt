package LocalSearch

fun interface IStoppingCriterion {
    fun shouldStop(state: LocalSearchState, currentTime: Long): Boolean

    infix fun or(other: IStoppingCriterion): IStoppingCriterion {
        return IStoppingCriterion { state, currentTime ->
            this.shouldStop(state, currentTime) || other.shouldStop(state, currentTime)
        }
    }

    infix fun and(other: IStoppingCriterion): IStoppingCriterion {
        return IStoppingCriterion { state, currentTime ->
            this.shouldStop(state, currentTime) && other.shouldStop(state, currentTime)
        }
    }

    companion object {
        fun maxIterations(maxIterations: Int): IStoppingCriterion {
            return IStoppingCriterion { state, _ -> state.iteration >= maxIterations }
        }

        fun maxRuntime(maxRuntimeMs: Long): IStoppingCriterion {
            return IStoppingCriterion { state, currentTime -> currentTime - state.startTime >= maxRuntimeMs }
        }

        fun maxStagnationTime(maxStagnationMs: Long): IStoppingCriterion {
            return IStoppingCriterion { state, currentTime -> currentTime - state.lastImprovement >= maxStagnationMs }
        }

        fun targetCost(targetCost: Int): IStoppingCriterion {
            return IStoppingCriterion { state, _ -> state.bestSolutionCost <= targetCost }
        }

        fun temperatureThreshold(minTemperature: Double): IStoppingCriterion {
            return IStoppingCriterion { state, _ -> state.temperature <= minTemperature }
        }

        fun maxPertubations(maxPerturbations: Int): IStoppingCriterion {
            return IStoppingCriterion { state, _ -> state.perturbations >= maxPerturbations }
        }

        fun maxEvaluations(maxEvals: Int): IStoppingCriterion {
            return IStoppingCriterion { state, _ -> state.evaluatedSolutions >= maxEvals }
        }
    }
}