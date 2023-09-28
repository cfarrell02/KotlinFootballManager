package org.setu.model

import java.time.LocalDate

data class Player(
    val name: String,
    val dateOfBirth: LocalDate,
    val positions: MutableList<String>,
    val nationality: String,
    val number : Int,
    val uid: String = java.util.UUID.randomUUID().toString()
) {
    constructor(name: String, dateOfBirth: LocalDate, position: String, nationality: String, number : Int) :
            this(name, dateOfBirth, mutableListOf(position), nationality, number)


    init{
        //Validation here
        require(name.isNotBlank()){"Name cannot be blank"}
        require(dateOfBirth.isBefore(LocalDate.now())){"Date of birth cannot be in the future"}
        require(positions.isNotEmpty()){"Player must have at least one position"}
        require(number > 0){"Number must be greater than 0"}
    }
    override fun toString(): String {
        return "$name, $dateOfBirth, ${positions.joinToString()}, Number: $number, $nationality"
    }

    fun addPosition(position: String) {
        positions.add(position)
    }
    fun removePosition(position: String) {
        positions.remove(position)
    }
}
