package nl.ns.martin.kanters.domain

import nl.ns.martin.kanters.domain.GameState.*

class Game {

    private val players = mutableListOf<Player>()
    private val matches = mutableListOf<Match>()
    private var turnIndex = 0

    var state = NOT_STARTED
        private set

    fun addPlayer(player: Player) {
        check(players.size < 2) { "Game is already full" }
        check(players.none { it.symbol == player.symbol }) { "Player symbol '${player.symbol}' is already taken" }

        players.add(player)
    }

    fun start() {
        check(state != PLAYING) { "Game is already started" }
        check(players.size == 2) { "Not enough players to start" }

        state = PLAYING
        matches += Match()
    }

    fun playTurn(): TurnResult {
        val match = matches.lastOrNull()
        check(state == PLAYING && match != null) { "Game is not ongoing" }
        val player = players[turnIndex++ % players.size]

        val positions = match.getPositions()
        val (x, y) = player.playTurn(positions)
        match.claim(x, y, player)

        if (match.isFinished()) {
            state = FINISHED
        }

        return TurnResult(Turn(player, x to y), positions.toList())
    }

    /**
     * Returns either the winning streak, or null if it's a draw
     */
    fun getFinalScore(): CompletedStreak? {
        check(state == FINISHED) { "Game is not finished" }
        return matches.last().getStreak()
    }
}