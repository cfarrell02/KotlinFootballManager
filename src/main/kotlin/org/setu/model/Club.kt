package org.setu.model

import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class Club (
    var name: String,
    var city: String,
    var stadium: String,
    private val _people : MutableList<Person> = ArrayList(),
    val uid: String = UUID.randomUUID().toString()

) {

    val people : List<Person>
        get() = _people

    init{
        //Validation here
        require(name.isNotBlank()){"Name cannot be blank"}
        require(city.isNotBlank()){"City cannot be blank"}
        require(stadium.isNotBlank()){"Stadium cannot be blank"}
    }

    fun addPlayer(name: String, dateOfBirth : LocalDate, position: String, nationality: String, number : Int): Player {
        val player = Player(name,dateOfBirth,position ,nationality, number)
        _people.add(player)
        return player
    }
    fun addStaff(name: String, dateOfBirth: LocalDate, nationality: String, role: String, salary: Double): Staff{
        val staff = Staff(name,dateOfBirth,nationality,role,salary)
        _people.add(staff)
        return staff
    }
    fun removePerson(person: Person) {
        _people.remove(person)
    }

    fun updatePlayer(uid: String, name: String, dateOfBirth : LocalDate, position: String, nationality: String, number : Int): Player{
        val player = _people.find { it.uid == uid }
        require(player is Player) { "Person with uid $uid is not a player" }
        player.name = name
        player.dateOfBirth = dateOfBirth
        player.clearPositions()
        player.addPosition(position)
        player.nationality = nationality
        player.number = number
        return player
    }
    fun updateStaff(uid: String, name: String, dateOfBirth: LocalDate, nationality: String, role: String, salary: Double): Staff{
        val staff = _people.find { it.uid == uid }
        require(staff is Staff) { "Person with uid $uid is not a staff" }
        staff.name = name
        staff.dateOfBirth = dateOfBirth
        staff.nationality = nationality
        staff.role = role
        staff.salary = salary
        return staff
    }
    fun removePerson(index: Int) {
        _people.removeAt(index)
    }
    fun listPlayers(): String {
        val list = _people.joinToString("\n") { e -> e.toString() }
        return list
    }
    fun listPlayersWithIndex(): String {
        val list = _people.mapIndexed { index, player -> "${index + 1}. $player" }
        return list.joinToString("\n")
    }
    fun getPerson(index: Int): Person {
        if(index < 0 || index >= _people.size){
            throw IndexOutOfBoundsException("Index $index is out of bounds for size ${_people.size}")
        }
        return _people[index]
    }
    fun searchPlayer(name: String): Person? {
        return _people.find { it.name.equals(name, ignoreCase = true) || it.toString().equals(name, ignoreCase = true)}
    }

    fun getPerson(id: String): Person? {
        return _people.find { it.uid == id }
    }

    override fun toString(): String {
        return "Club: $name, $city, $stadium"
    }

}
