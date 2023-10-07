package org.setu.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ClubTest {
    var club = Club("Test Club", "Test City", "Test Stadium")

    @BeforeEach
    fun setUp() {
        club.addPlayer("John Doe", java.time.LocalDate.of(1990, 1, 1), "Goalkeeper", "English", 1)
        club.addPlayer("Jim Johnson", java.time.LocalDate.of(1990, 1, 1), "Defender", "Irish", 2)
        club.addStaff("Jack Jackson", java.time.LocalDate.of(1990, 1, 1), "Scottish", "Coach", 25000.0)
    }

    @AfterEach
    fun tearDown() {
        club = Club("Test Club", "Test City", "Test Stadium")
    }

    @Test
    fun getPeople() {
        assertEquals(3, club.people.size)
        assertTrue(club.people.contains(club.people[0]))
    }

    @Test
    fun addPlayer() {
        assertEquals(3, club.people.size)
        club.addPlayer("Jill Smith", java.time.LocalDate.of(1990, 1, 1), "Forward", "Welsh", 4)
        assertEquals(4, club.people.size)
        assertTrue(club.people.contains(club.people[3]))
    }

    @Test
    fun addStaff() {
        assertEquals(3, club.people.size)
        club.addStaff("Jill Smith", java.time.LocalDate.of(1990, 1, 1), "Welsh", "Assistant Coach", 15000.0)
        assertEquals(4, club.people.size)
        assertTrue(club.people.contains(club.people[3]))
    }

    @Test
    fun removePerson() {
        assertEquals(3, club.people.size)
        val removedPlayer = club.removePerson(club.people[0])
        assertEquals(2, club.people.size)
        assertFalse(club.people.contains(removedPlayer))
    }

    @Test
    fun updatePlayer() {
        assertEquals(3, club.people.size)
        val updatedPlayer = club.updatePlayer(club.people[0].uid, "John Doe", java.time.LocalDate.of(1990, 1, 1), "Defender", "English", 1)
        assertEquals(3, club.people.size)
        assertEquals("Defender", updatedPlayer.positions[0])

    }

    @Test
    fun updateStaff() {
        assertEquals(3, club.people.size)
        val updatedStaff = club.updateStaff(club.people[2].uid, "Jack Jackson", java.time.LocalDate.of(1990, 1, 1), "Scottish", "Assistant Coach", 15000.0)
        assertEquals(3, club.people.size)
        assertEquals("Assistant Coach", updatedStaff.role)
    }


    @Test
    fun listPlayers() {
        assertEquals(3, club.people.size)
        val list = club.listPlayers()
        assertEquals(3, club.people.size)
        assertTrue(list.contains(club.people[0].toString()))
    }

    @Test
    fun listPlayersWithIndex() {
        assertEquals(3, club.people.size)
        val list = club.listPlayersWithIndex()
        assertEquals(3, club.people.size)
        assertTrue(list.contains("1. ${club.people[0]}"))
    }

    @Test
    fun getPerson() {
        assertEquals(3, club.people.size)
        val person = club.getPerson(0)
        assertEquals(3, club.people.size)
        assertEquals(club.people[0], person)
    }

    @Test
    fun searchPlayer() {
        assertEquals(3, club.people.size)
        val person = club.searchPlayer("John Doe")
        assertEquals(3, club.people.size)
        assertEquals(club.people[0], person)
    }

    @Test
    fun testGetPerson() {
        assertEquals(3, club.people.size)
        val person = club.getPerson(club.people[0].uid)
        assertEquals(3, club.people.size)
        assertEquals(club.people[0], person)
    }

    @Test
    fun testToString() {
        assertTrue(club.toString().contains("Test Club"))
        assertTrue(club.toString().contains("Test City"))
        assertTrue(club.toString().contains("Test Stadium"))
    }

    @Test
    fun getName() {
        assertEquals("Test Club", club.name)
    }

    @Test
    fun setName() {
        club.name = "New Test Club"
        assertEquals("New Test Club", club.name)
    }

    @Test
    fun getCity() {
        assertEquals("Test City", club.city)
    }

    @Test
    fun setCity() {
        club.city = "New Test City"
        assertEquals("New Test City", club.city)
    }

    @Test
    fun getStadium() {
        assertEquals("Test Stadium", club.stadium)
    }

    @Test
    fun setStadium() {
        club.stadium = "New Test Stadium"
        assertEquals("New Test Stadium", club.stadium)
    }

    @Test
    fun getUid() {
        assertTrue(club.uid.isNotBlank())
    }
}