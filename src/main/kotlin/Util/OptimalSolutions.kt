package Util

object OptimalSolutions {
    val optimalSolutions = mapOf(
        "bur26a.dat" to intArrayOf(26, 15, 11, 7, 4, 12, 13, 2, 6, 18, 1, 5, 9, 21, 8, 14, 3, 20, 19, 25, 17, 10, 16, 24, 23, 22),
        "chr12a.dat" to intArrayOf(7,5,12,2,1,3,9,11,10,6,8,4),
        "els19.dat" to intArrayOf(9,10,7,18,14,19,13,17,6,11,4,5,12,8,15,16,1,2,3),
        "had12.dat" to intArrayOf(3,10,11,2,12,5,6,7,8,1,4,9),
        "bur26g.dat" to intArrayOf(22, 11, 2, 23, 13, 25, 24, 8, 1, 21, 20, 4, 7, 18, 12, 15, 9, 19, 5, 26, 16, 6, 14, 3, 17, 10),
        "tai12a.dat" to intArrayOf(8,1,6,2,11,10,3,5,9,7,12,4),
        "esc64a.dat" to intArrayOf(1,2,9,50,3,61,4,62,5,54,64,6,7,52,56,8,55,10,63,18,11,51,12,13,14,15,20,43,16,41,17,47,23,19,24,21,53,22,28,25,26,27,29,60,30,59,31,32,33,34,35,36,37,38,39,40,42,44,45,46,48,49,57,58)
    )

    fun get(instanceName: String): IntArray {
        if (!optimalSolutions.containsKey(instanceName)) {
            throw IllegalArgumentException("No optimal solution for instance $instanceName")
        }
        return optimalSolutions[instanceName]!!.map { it - 1 }.toIntArray()
    }
}