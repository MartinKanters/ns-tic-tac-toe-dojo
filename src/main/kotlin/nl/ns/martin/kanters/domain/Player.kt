package nl.ns.martin.kanters.domain

import kotlin.random.Random

data class Player(val symbol: Char) {
    fun playTurn(positions: List<List<Position>>): Pair<Int, Int> {
        val openPositions = positions.flatMapIndexed { x, row ->
            row.mapIndexedNotNull { y, position ->
                    if (position.claimedBy == null) x to y else null
                }
        }

        val choice = Random.nextInt(0, openPositions.size - 1)

        return openPositions[choice]
    }
}