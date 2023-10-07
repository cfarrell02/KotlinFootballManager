package org.setu.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PlayerTest {
    val players = mutableListOf<Player>()
    @BeforeEach
    fun setUp() {
        players.add(Player("John Doe", java.time.LocalDate.of(1990, 1, 1), "Goalkeeper", "English", 1))
        players.add(Player("Jim Johnson", java.time.LocalDate.of(1990, 1, 1), "Defender", "Irish", 2))
        players.add(Player("Jack Jackson", java.time.LocalDate.of(1990, 1, 1), "Midfielder", "Scottish", 3))
        players.add(Player("Jill Smith", java.time.LocalDate.of(1990, 1, 1), "Forward", "Welsh", 4))

    }

    @AfterEach
    fun tearDown() {
        players.clear()
    }

    @Test
    fun getPositions() {
        assertEquals("Goalkeeper", players[0].positions[0])
        assertEquals("Defender", players[1].positions[0])
        assertEquals("Midfielder", players[2].positions[0])
        assertEquals("Forward", players[3].positions[0])
    }

    @Test
    fun testToString() {
        assertTrue(players[0].toString().contains("John Doe"))
        assertTrue(players[0].toString().contains("1990-01-01"))
        assertTrue(players[0].toString().contains("English"))
        assertTrue(players[0].toString().contains("Goalkeeper"))
        assertTrue(players[0].toString().contains("1"))
    }

    @Test
    fun addPosition() {
        assertEquals(1, players[0].positions.size)
        players[0].addPosition("Defender")
        assertEquals(2, players[0].positions.size)
        assertEquals("Goalkeeper", players[0].positions[0])
        assertEquals("Defender", players[0].positions[1])

    }

    @Test
    fun removePosition() {
        assertEquals(1, players[0].positions.size)
        players[0].addPosition("Defender")
        assertEquals(2, players[0].positions.size)
        assertEquals("Goalkeeper", players[0].positions[0])
        assertEquals("Defender", players[0].positions[1])
        players[0].removePosition("Goalkeeper")
        assertEquals(1, players[0].positions.size)
        assertEquals("Defender", players[0].positions[0])
    }

    @Test
    fun clearPositions() {
        assertEquals(1, players[0].positions.size)
        players[0].addPosition("Defender")
        assertEquals(2, players[0].positions.size)
        assertEquals("Goalkeeper", players[0].positions[0])
        assertEquals("Defender", players[0].positions[1])
        players[0].clearPositions()
        assertEquals(0, players[0].positions.size)
    }

    @Test
    fun getNumber() {
        assertEquals(1, players[0].number)
        assertEquals(2, players[1].number)
        assertEquals(3, players[2].number)
        assertEquals(4, players[3].number)

    }

    @Test
    fun setNumber() {
        assertEquals(1, players[0].number)
        players[0].number = 5
        assertEquals(5, players[0].number)

    }
}