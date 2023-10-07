package org.setu.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class LeagueTest {
    var league = League("Test League", "Test Country")

    @BeforeEach
    fun setUp() {
        league.addClub("Test Club", "Test City", "Test Stadium")
        league.clubs[0].addPlayer("John Doe", java.time.LocalDate.of(1990, 1, 1), "Goalkeeper", "English", 1)
        league.clubs[0].addPlayer("Jim Johnson", java.time.LocalDate.of(1990, 1, 1), "Defender", "Irish", 2)
        league.clubs[0].addStaff("Jack Jackson", java.time.LocalDate.of(1990, 1, 1), "Scottish", "Coach", 25000.0)
    }

    @AfterEach
    fun tearDown() {
        league = League("Test League", "Test Country")
    }

    @Test
    fun getClubs() {
        assertEquals(1, league.clubs.size)
        assertTrue(league.clubs.contains(league.clubs[0]))
    }

    @Test
    fun addClub() {
        assertEquals(1, league.clubs.size)
        val addedClub = league.addClub("Test Club 2", "Test City 2", "Test Stadium 2")
        assertEquals(2, league.clubs.size)
        assertTrue(league.clubs.contains(addedClub))
    }

    @Test
    fun removeClub() {
        assertEquals(1, league.clubs.size)
        val removedClub = league.removeClub(league.clubs[0])
        assertEquals(0, league.clubs.size)
        assertFalse(league.clubs.contains(removedClub))
    }

    @Test
    fun listClubs() {
        assertEquals(1, league.clubs.size)
        val list = league.listClubs()
        assertTrue(list.contains("Test Club"))
        assertTrue(list.contains("Test City"))
        assertTrue(list.contains("Test Stadium"))
    }

    @Test
    fun getClub() {
        assertEquals(1, league.clubs.size)
        val club = league.getClub(league.clubs[0].uid)
        assertEquals(league.clubs[0], club)
    }

    @Test
    fun searchClub() {
        assertEquals(1, league.clubs.size)
        val club = league.searchClub("Test Club")
        assertEquals(league.clubs[0], club)
    }

    @Test
    fun updateClub() {
        assertEquals(1, league.clubs.size)
        val updatedClub = league.updateClub(league.clubs[0].uid, "Test Club 2", "Test City 2", "Test Stadium 2")
        assertEquals(league.clubs[0], updatedClub)
        assertEquals("Test Club 2", updatedClub.name)
        assertEquals("Test City 2", updatedClub.city)
        assertEquals("Test Stadium 2", updatedClub.stadium)
    }

    @Test
    fun containsClub() {
        assertEquals(1, league.clubs.size)
        assertTrue(league.containsClub("Test Club"))
        assertFalse(league.containsClub("Test Club 2"))
    }

    @Test
    fun testToString() {
        assertTrue(league.toString().contains("Test League"))
        assertTrue(league.toString().contains("Test Country"))
    }

    @Test
    fun getName() {
        assertEquals("Test League", league.name)
    }

    @Test
    fun setName() {
        league.name = "New Test League"
        assertEquals("New Test League", league.name)
    }

    @Test
    fun getCountry() {
        assertEquals("Test Country", league.country)
    }

    @Test
    fun setCountry() {
        league.country = "New Test Country"
        assertEquals("New Test Country", league.country)
    }

    @Test
    fun getUid() {
        assertTrue(league.uid.isNotBlank())
    }
}