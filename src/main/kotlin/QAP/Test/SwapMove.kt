package QAP.Test

import LocalSearch.IMove

class SwapMove(val i: Int, val j: Int) : IMove {
    override fun hash(): Int {
        return i * 31 + j
    }

    override fun toString(): String {
        return "SwapMove(i=$i, j=$j)"
    }
}