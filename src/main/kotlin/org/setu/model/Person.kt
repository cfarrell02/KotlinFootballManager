package org.setu.model

import java.time.LocalDate

abstract class Person(
    var name: String,
    var dateOfBirth: LocalDate,
    var nationality: String,
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