package nl.ns.martin.kanters.domain

import nl.ns.martin.kanters.domain.GameState.NOT_STARTED
import nl.ns.martin.kanters.domain.GameState.PLAYING

class Game {

    private val players = mutableListOf<Player>()

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
    }

    fun playTurn(): Turn {
        TODO()
    }
}