package org.setu

import java.time.LocalDate

data class Player(
    val name: String,
    val dateOfBirth: LocalDate,
    val positions: List<String> = emptyList(),
    val nationality: String
) {
    constructor(name: String, dateOfBirth: LocalDate, position: String, nationality: String) :
            this(name, dateOfBirth, listOf(position), nationality)

    override fun toString(): String {
        return "$name, $dateOfBirth, ${positions.joinToString()}, $nationality"
    }
}
