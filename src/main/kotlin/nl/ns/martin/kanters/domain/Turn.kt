package nl.ns.martin.kanters.domain

data class Turn(
    val player: Player,
    val position: Pair<Int, Int>
)