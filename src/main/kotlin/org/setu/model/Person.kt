package org.setu.model

import java.time.LocalDate

abstract class Person(
    val name: String,
    val dateOfBirth: LocalDate,
    val nationality: String,
    val uid: String = java.util.UUID.randomUUID().toString()
) {
    init {
        // Validation here
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(dateOfBirth.isBefore(LocalDate.now())) { "Date of birth cannot be in the future" }
    }

    override fun toString(): String {
        return "$name, $dateOfBirth, $nationality"
    }
}