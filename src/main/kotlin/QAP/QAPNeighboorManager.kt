package QAP

object QAPNeighboorManager {
    enum class SelectionType {
        GREEDY,
        STEEPEST,
    }

    enum class EvaluationType {
        FULL,
        DELTA,
    }

    fun getNextSolution(solution:QAPSolution, selectionType: SelectionType, evaluationType: EvaluationType): Pair<Int, QAPSolution> {
        var evaluations: Int = 0
        when (selectionType) {
            SelectionType.GREEDY -> {
                when (evaluationType) {
                    EvaluationType.FULL -> {
                        val moves = generateMoves(solution)
                        for (move in moves) {
                            val nextSolution = solution.swapElements(move.first, move.second)
                            evaluations++
                            if (nextSolution.solutionCost < solution.solutionCost) {
                                return Pair(evaluations, nextSolution)
                            }
                        }

                        //TODO maybe null
                        return Pair(evaluations, solution)
                    }
                    EvaluationType.DELTA -> {
                        //TODO Implement
                        val moves = generateMoves(solution)
                        for (move in moves) {
                            val delta = calculateDelta(solution, move.first, move.second)
                            evaluations++
                            if (delta < 0) {
                                val nextSolution = solution.swapElements(move.first, move.second)
                                return Pair(evaluations, nextSolution)
                            }
                        }

                        return Pair(evaluations, solution)
                    }
                }
            }
            SelectionType.STEEPEST -> {
                when (evaluationType) {
                    EvaluationType.FULL -> {
                        val moves = generateMoves(solution)
                        var bestSolution = solution
                        for (move in moves) {
                            val nextSolution = solution.swapElements(move.first, move.second)
                            evaluations++
                            if (nextSolution.solutionCost < bestSolution.solutionCost) {
                                bestSolution = nextSolution
                            }
                        }
                        return Pair(evaluations, bestSolution)
                    }
                    EvaluationType.DELTA -> {
                        //TODO Implement
                        val moves = generateMoves(solution)
                        val movesWithDeltas = mutableListOf<Pair<Int, Pair<Int, Int>>>()
                        for (move in moves) {
                            val delta = calculateDelta(solution, move.first, move.second)
                            evaluations++
                            movesWithDeltas.add(Pair(delta, move))
                        }
                        val bestMove = movesWithDeltas.minByOrNull { it.first }!!.second
                        val nextSolution = solution.swapElements(bestMove.first, bestMove.second)

                        return Pair(evaluations, nextSolution)
                    }
                }
            }
        }
    }

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
        val distances = solution.instance.distances
        val flows = solution.instance.flows
        val permutation = solution.solution

        var delta = 0

        for (k in 0 until permutation.size) {
            if (k != i && k != j) {
                // Change in cost for interaction between facilities i and k
                delta += flows[i][k] * (distances[permutation[j]][permutation[k]] - distances[permutation[i]][permutation[k]])

                // Change in cost for interaction between facilities j and k
                delta += flows[j][k] * (distances[permutation[i]][permutation[k]] - distances[permutation[j]][permutation[k]])

                // Change in cost for interaction between facilities k and i
                delta += flows[k][i] * (distances[permutation[k]][permutation[j]] - distances[permutation[k]][permutation[i]])

                // Change in cost for interaction between facilities k and j
                delta += flows[k][j] * (distances[permutation[k]][permutation[i]] - distances[permutation[k]][permutation[j]])
            }
        }

        // Change in cost for interaction between facilities i and j
        delta += flows[i][j] * (distances[permutation[j]][permutation[i]] - distances[permutation[i]][permutation[j]])
        delta += flows[j][i] * (distances[permutation[i]][permutation[j]] - distances[permutation[j]][permutation[i]])

        return delta
    }

    fun greedyNeighboorSelection(solution:QAPSolution): Pair<Int, QAPSolution> {
        return getNextSolution(solution, SelectionType.GREEDY, EvaluationType.DELTA)
    }

    fun steepestNeighboorSelection(solution:QAPSolution): Pair<Int, QAPSolution> {
        return getNextSolution(solution, SelectionType.STEEPEST, EvaluationType.DELTA)
    }

    fun getNeighboorhoodSize(solution: QAPSolution): Int {
        return generateMoves(solution).size
    }
}