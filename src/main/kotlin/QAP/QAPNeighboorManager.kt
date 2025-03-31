package QAP

object QAPNeighboorManager {
    fun generateNeighboorhood(solution: QAPSolution): List<Pair<Int, QAPSolution>> {
        //TODO Rethink whether to use whole solution representation or just keep moves with deltas
        val neighboorhood = mutableListOf<Pair<Int, QAPSolution>>()
        for (move in generateMoves(solution)) {
            val delta = calculateDelta(solution, move.first, move.second)
            val newSolution = solution.solution.copyOf()
            newSolution[move.first] = solution.solution[move.second]
            newSolution[move.second] = solution.solution[move.first]
            val newSolutionInstance = QAPSolution(solution.instance, newSolution)
            neighboorhood.add(Pair(delta, newSolutionInstance))
        }

        return neighboorhood
    }

    fun generateMoves(solution: QAPSolution): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until solution.instance.instanceSize) {
            for (j in 0 until solution.instance.instanceSize) {
                if (i != j) {
                    moves.add(Pair(i, j))
                }
            }
        }

        return moves
    }

    fun calculateDelta(solution: QAPSolution, i: Int, j: Int): Int {
        val oldCost = solution.solutionCost
        val newSolution = solution.solution.copyOf()
        newSolution[i] = solution.solution[j]
        newSolution[j] = solution.solution[i]
        val newSolutionInstance = QAPSolution(solution.instance, newSolution)
        return newSolutionInstance.solutionCost - oldCost

    }

    fun greedyNeighboorSelection(solution:QAPSolution): Pair<Int, QAPSolution> {
        val neighboorhood = generateNeighboorhood(solution).shuffled()
        var evaluations = 0

        for (neighboor in neighboorhood) {
            evaluations++
            if (neighboor.second.solutionCost < solution.solutionCost) {
                return Pair(evaluations, neighboor.second)
            }
        }

        return Pair(evaluations, solution)
    }

    fun steepestNeighboorSelection(solution:QAPSolution): Pair<Int, QAPSolution> {
        val neighboorhood = generateNeighboorhood(solution)
        var bestNeighboor = neighboorhood.minByOrNull { it.second.solutionCost }!!.second

        return Pair(neighboorhood.size, bestNeighboor)
    }

    fun getNeighboorhoodSize(solution: QAPSolution): Int {
        return generateMoves(solution).size
    }
}