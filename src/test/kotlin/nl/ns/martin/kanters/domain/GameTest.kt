package nl.ns.martin.kanters.domain

import nl.ns.martin.kanters.domain.GameState.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class GameTest {

    @Test
    fun `game instance can be created`() {
        val game = Game()

        assertThat(game).isNotNull
        assertThat(game.state).isEqualTo(NOT_STARTED)
    }

    @Test
    fun `game can be started with two enrolled players`() {
        val playerX = Player('X')
        val playerO = Player('O')

        val game = Game()
        game.addPlayer(playerX)
        game.addPlayer(playerO)
        game.start()

        assertThat(game.state).isEqualTo(PLAYING)
    }

    @Test
    fun `game which is started cannot be started again`() {

        val game = Game()
        game.addPlayer(Player('X'))
        game.addPlayer(Player('O'))
        game.start()

        assertThatThrownBy { game.start() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Game is already started")
    }

    @Test
    fun `game cannot be started with less than two enrolled players`() {
        val playerX = Player('X')

        val game = Game()
        game.addPlayer(playerX)

        assertThatThrownBy { game.start() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Not enough players to start")
    }

    @Test
    fun `maximally two players can enroll in the game`() {
        val playerX = Player('X')
        val playerO = Player('O')
        val playerZ = Player('Z')

        val game = Game()
        game.addPlayer(playerX)
        game.addPlayer(playerO)

        assertThatThrownBy { game.addPlayer(playerZ) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Game is already full")
    }

    @Test
    fun `two different players with different symbols have to enroll`() {
        val game = Game()
        game.addPlayer(Player('X'))

        assertThatThrownBy { game.addPlayer(Player('X')) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Player symbol 'X' is already taken")
    }
}