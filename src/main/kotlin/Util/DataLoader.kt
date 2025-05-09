package Util

import QAP.QAPInstance

object DataLoader {
    fun loadArrayFromFile(dataPath: String, instanceName: String): QAPInstance {
        val path = "$dataPath/$instanceName"
        val instanceNameClean = instanceName.split(".")[0]
        val file = java.io.File(path)
        val lines = file.readLines().filter { it.isNotBlank() }

        val instanceSize = lines[0].trim().toInt()

        val flowMatrix = Array(instanceSize) { IntArray(instanceSize) }
        val distanceMatrix = Array(instanceSize) { IntArray(instanceSize) }

        for (i in 0 until instanceSize) {
            val rowValues = lines[i + 1].trim().split(Regex("\\s+")).map { it.toInt() }
            for (j in 0 until instanceSize) {
                flowMatrix[i][j] = rowValues[j]
            }
        }

        for (i in 0 until instanceSize) {
            val rowValues = lines[i + instanceSize + 1].trim().split(Regex("\\s+")).map { it.toInt() }
            for (j in 0 until instanceSize) {
                distanceMatrix[i][j] = rowValues[j]
            }
        }

        return QAPInstance(instanceNameClean, instanceSize, flowMatrix, distanceMatrix)
    }

    fun loadUnformattedInstance(dataPath: String, instanceName: String): QAPInstance {
        val path = "$dataPath/$instanceName"
        val instanceNameClean = instanceName.split(".")[0]
        val file = java.io.File(path)
        val values = file.readText().replace("\n", " ").split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        val instanceSize = values[0]

        val flowMatrix = Array(instanceSize) { IntArray(instanceSize) }
        val distanceMatrix = Array(instanceSize) { IntArray(instanceSize) }

        for (i in 0 until instanceSize) {
            for (j in 0 until instanceSize) {
                flowMatrix[i][j] = values[i * instanceSize + j + 1]
            }
        }

        for (i in 0 until instanceSize) {
            for (j in 0 until instanceSize) {
                distanceMatrix[i][j] = values[i * instanceSize + j + 1 + instanceSize * instanceSize]
            }
        }

        return QAPInstance(instanceNameClean, instanceSize, flowMatrix, distanceMatrix)
    }
}