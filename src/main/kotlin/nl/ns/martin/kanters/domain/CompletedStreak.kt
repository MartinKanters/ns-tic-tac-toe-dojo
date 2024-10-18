package nl.ns.martin.kanters.domain

data class CompletedStreak(
    val player: Player,
    val streak: List<Pair<Int, Int>>,
)
