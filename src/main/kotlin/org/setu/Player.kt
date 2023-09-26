package org.setu

import java.util.Date

data class Player(
    val name: String,
    val dateOfBirth : Date,
    val positions: List<String>,
    val nationality: String
){
    override fun toString(): String {
        return "$name, $dateOfBirth, ${positions[0]}, $nationality"
    }
}