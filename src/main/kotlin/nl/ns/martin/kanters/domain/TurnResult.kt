package nl.ns.martin.kanters.domain

data class TurnResult(
    val turn: Turn,
    val newState: List<List<Position>>
)