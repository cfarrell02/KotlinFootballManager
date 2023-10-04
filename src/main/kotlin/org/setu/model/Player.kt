package org.setu.model

import java.time.LocalDate

class Player(
    name: String,
    dateOfBirth: LocalDate,
    private val _positions: MutableList<String>,
    nationality: String,
    var number: Int
) : Person(name, dateOfBirth, nationality) {

    val positions: List<String>
        get() = _positions

    constructor(name: String, dateOfBirth: LocalDate, position: String, nationality: String, number: Int) :
            this(name, dateOfBirth, mutableListOf(position), nationality, number)

    init {
        // Validation here
        require(_positions.isNotEmpty()) { "Player must have at least one position" }
        require(number > 0) { "Number must be greater than 0" }
    }

    override fun toString(): String {
        return "Player: $name, $dateOfBirth, ${_positions.joinToString()}, Number: $number, $nationality"
    }

    fun addPosition(position: String) {
        _positions.add(position)
    }

    fun removePosition(position: String) {
        _positions.remove(position)
    }

    fun clearPositions() {
        _positions.clear()
    }
}