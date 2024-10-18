package nl.ns.martin.kanters.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class MatchTest {

    @Test
    fun `for a new match, 9 positions should be open`() {
        val match = Match()

        val positions = match.getPositions()

        assertThat(positions).isEqualTo(
            List(3) { List(3) { Position() } }
        )
    }

    @Test
    fun `a position can be claimed by a player`() {
        val player = Player('X')
        val match = Match()

        match.claim(0, 1, player)

        val positions = match.getPositions()
        assertThat(positions[0][1]).isEqualTo(Position(claimedBy = player))
    }

    @Test
    fun `a position which is chosen cannot be claimed again`() {
        val player1 = Player('X')
        val player2 = Player('O')
        val match = Match()

        match.claim(0, 1, player1)
        assertThatThrownBy { match.claim(0, 1, player2) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Position is already claimed")
    }

    @ParameterizedTest
    @ValueSource(strings = [
        """
            x|x|x
            o|.|o
            .|o|.""",
        """
            x|o|x
            o|o|o
            .|x|.""",
        """
            x|o|.
            o|x|o
            .|o|x""",
        """
            o|o|x
            o|.|x
            .|o|x"""
    ])
    fun `completed streaks can be found`(boardStringRepresentation: String) {
        val match = Match()
        val board = parseBoardStringRepresentation(boardStringRepresentation)
        playStepsOnMatch(board, match)

        val streak = match.getStreak()

        assertThat(streak).isNotNull()
    }

    @ParameterizedTest
    @ValueSource(strings = [
        """
            x|.|x
            o|.|o
            x|o|.""",
        """
            x|o|x
            o|.|o
            .|x|.""",
        """
            .|o|.
            o|x|o
            x|o|x""",
        """
            .|o|x
            o|o|.
            .|.|x"""
    ])
    fun `uncompleted streaks will not be reported as completed`(boardStringRepresentation: String) {
        val match = Match()
        val board = parseBoardStringRepresentation(boardStringRepresentation)
        playStepsOnMatch(board, match)

        val streak = match.getStreak()

        assertThat(streak).isNull()
    }

    @Test
    fun `a completed streak with the winner can be found`() {
        val boardStringRepresentation = """
            x|o|.
            o|x|o
            .|o|x"""
        val match = Match()
        val board = parseBoardStringRepresentation(boardStringRepresentation)
        playStepsOnMatch(board, match)

        val streak = match.getStreak()

        assertThat(streak).isNotNull()
        assertThat(streak!!.player.symbol).isEqualTo('x')
        assertThat(streak.streak).isEqualTo(listOf(
            0 to 0, 1 to 1, 2 to 2
        ))
    }

    private fun parseBoardStringRepresentation(matchStringRepresentation: String) =
        matchStringRepresentation.split(System.lineSeparator())
            .filterNot { it.isEmpty() }
            .map { line ->
                line.split('|').map { position ->
                    position.trim()[0].takeIf { it != '.' }?.let { Player(it) }
                }
            }

    private fun playStepsOnMatch(board: List<List<Player?>>, match: Match) =
        board.forEachIndexed { x, horizontalPositions ->
            horizontalPositions.forEachIndexed { y, player ->
                player?.run {
                    match.claim(x, y, player)
                }
            }
        }
}