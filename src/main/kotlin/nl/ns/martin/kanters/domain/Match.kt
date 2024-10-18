package nl.ns.martin.kanters.domain

class Match {

    private val positions: List<List<Position>> = List(3) { List(3) { Position() } }

    fun getPositions(): List<List<Position>> = positions

    fun claim(x: Int, y: Int, player: Player) {
        val position = positions[x][y]
        check(position.claimedBy == null) { "Position is already claimed" }

        position.claimedBy = player
    }

    fun getStreak(): CompletedStreak? =
        possibleStreaks.flatMap { streak ->
            streak.map { (x, y) -> positions[x][y] }
                .mapNotNull { it.claimedBy }
                .groupingBy { it }
                .eachCount()
                .filterValues { it == streak.size }
                .mapValues { streak }
                .map { (player, streak) -> CompletedStreak(player, streak) }
        }
            .firstOrNull()

    fun isFinished() =
        getStreak() != null || positions.flatten().all { it.claimedBy != null }

    companion object {
        private val possibleStreaks = listOf(
            listOf(0 to 0, 0 to 1, 0 to 2),
            listOf(1 to 0, 1 to 1, 1 to 2),
            listOf(2 to 0, 2 to 1, 2 to 2),
            listOf(0 to 0, 1 to 0, 2 to 0),
            listOf(0 to 1, 1 to 1, 2 to 1),
            listOf(0 to 2, 1 to 2, 2 to 2),
            listOf(0 to 0, 1 to 1, 2 to 2),
            listOf(2 to 0, 1 to 1, 0 to 2),
        )
    }
}