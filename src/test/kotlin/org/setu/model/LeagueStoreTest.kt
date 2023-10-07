package org.setu.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class LeagueStoreTest {
    var leagueStore = LeagueStore()

    @BeforeEach
    fun setUp() {
        leagueStore.addLeague("Test League", "Test Country")
        leagueStore.leagues[0].addClub("Test Club", "Test City", "Test Stadium")
        leagueStore.leagues[0].clubs[0].addPlayer("John Doe", java.time.LocalDate.of(1990, 1, 1), "Goalkeeper", "English", 1)
        leagueStore.leagues[0].clubs[0].addPlayer("Jim Johnson", java.time.LocalDate.of(1990, 1, 1), "Defender", "Irish", 2)
        leagueStore.leagues[0].clubs[0].addStaff("Jack Jackson", java.time.LocalDate.of(1990, 1, 1), "Scottish", "Coach", 25000.0)
    }

    @AfterEach
    fun tearDown() {
        leagueStore = LeagueStore()
    }

    @Test
    fun getLeagues() {
        assertEquals(1, leagueStore.leagues.size)
        assertTrue(leagueStore.leagues.contains(leagueStore.leagues[0]))
    }

    @Test
    fun addLeague() {
        assertEquals(1, leagueStore.leagues.size)
        val addedLeague = leagueStore.addLeague("Test League 2", "Test Country 2")
        assertEquals(2, leagueStore.leagues.size)
        assertTrue(leagueStore.leagues.contains(addedLeague))
    }

    @Test
    fun updateLeague() {
        assertEquals(1, leagueStore.leagues.size)
        val updatedLeague = leagueStore.updateLeague(leagueStore.leagues[0].uid, "Test League 2", "Test Country 2")
        assertEquals(1, leagueStore.leagues.size)
        assertEquals("Test League 2", updatedLeague.name)
        assertEquals("Test Country 2", updatedLeague.country)
    }

    @Test
    fun search() {
        assertEquals(1, leagueStore.leagues.size)
        val search = leagueStore.search("Test League")
        assertEquals(1, search.size)
        assertTrue(search.contains(leagueStore.leagues[0]))

        //Check search for player
        val searchPlayer = leagueStore.search("John Doe")
        assertEquals(1, searchPlayer.size)
        assertTrue(searchPlayer.contains(leagueStore.leagues[0].clubs[0].people[0]))
    }


    @Test
    fun getLeague() {
        assertEquals(1, leagueStore.leagues.size)
        val league = leagueStore.getLeague(leagueStore.leagues[0].uid)
        assertEquals(leagueStore.leagues[0], league)
    }
}