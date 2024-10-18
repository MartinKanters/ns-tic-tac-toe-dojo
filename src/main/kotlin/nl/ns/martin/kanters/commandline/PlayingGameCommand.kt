package nl.ns.martin.kanters.commandline

import nl.ns.martin.kanters.domain.*
import nl.ns.martin.kanters.domain.GameState.PLAYING
import picocli.CommandLine.Command

@Command(name = "play", mixinStandardHelpOptions = true)
class PlayingGameCommand : Runnable {

    override fun run() {
        val game = Game()
        val player1 = Player('X')
        val player2 = Player('O')
        game.addPlayer(player1)
        game.addPlayer(player2)
        game.start()

        renderGameStart(player1, player2)

        while (game.state == PLAYING) {
            val turnResult = game.playTurn()
            val turn = turnResult.turn
            renderTurn(turn, turnResult)
            Thread.sleep(1000)
        }

        val finalScore = game.getFinalScore()
        renderGameFinish(finalScore, player1, player2)
    }

    private fun renderGameStart(player1: Player, player2: Player) {
        println("Started a game with player ${player1.symbol} and player ${player2.symbol}")
    }

    private fun renderTurn(turn: Turn, turnResult: TurnResult) {
        println("Player ${turn.player.symbol} played:")
        val overview = turnResult.newState.joinToString(separator = System.lineSeparator()) { row ->
            row.joinToString(separator = "|") { (it.claimedBy?.symbol ?: ' ').toString() }
        }
        println(overview)
    }

    private fun renderGameFinish(
        finalScore: CompletedStreak?,
        player1: Player,
        player2: Player
    ) {
        val finalMessage = finalScore?.let {
            "Player ${it.player.symbol} won!"
        } ?: "Player ${player1.symbol} and player ${player2.symbol} drew!"

        println(finalMessage)
    }

}