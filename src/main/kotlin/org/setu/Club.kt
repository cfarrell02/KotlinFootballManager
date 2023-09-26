package org.setu

import java.util.*
import kotlin.collections.ArrayList

class Club (
    val name: String,
    val city: String,
    val stadium: String,
    private val players: MutableList<Player> = ArrayList()
) {

    fun addPlayer(name: String, dateOfBirth : Date, positions: List<String>, nationality: String) {
        val player = Player(name,dateOfBirth,positions,nationality)
        players.add(player)
    }
    fun removePlayer(player: Player) {
        players.remove(player)
    }
    fun removePlayer(index: Int) {
        players.removeAt(index)
    }
    fun listPlayers(): String {
        val list = players.joinToString("\n") { e -> e.toString() }
        return list
    }
    fun listPlayersWithIndex(): String {
        val list = players.mapIndexed { index, player -> "${index + 1}. $player" }
        return list.joinToString("\n")
    }
    fun getPlayer(index: Int): Player {
        if(index < 0 || index >= players.size){
            throw IndexOutOfBoundsException("Index $index is out of bounds for size ${players.size}")
        }
        return players[index]
    }
    fun searchPlayer(name: String): Player? {
        return players.find { it.name.uppercase() == name.uppercase() }
    }

}
