package QAP.Test

//class HeuristicSolutionGenerator : AbstrSolutionGenerator<QAPInstance>() {
//    override fun generateSolution(instance: QAPInstance): QAPSolution {
//        var solution = mutableListOf<Int>()
//        var locations = MutableList(instance.instanceSize) { it }
//        val intialFacility = Randomizer.getRandomIndex(instance.instanceSize)
//
//        solution.add(intialFacility)
//        locations.remove(intialFacility)
//
//        while (locations.isNotEmpty()) {
//            val nextFacility = locations.minByOrNull { QAPSolutionManager.calculateAdditionCost(solution, it, instance) }!!
//
//            solution.add(nextFacility)
//            locations.remove(nextFacility)
//        }
//
//        return QAPSolution(instance, solution.toIntArray())
//    }
//
//    override fun getName(): String {
//        return "Heuristic"
//    }
//}