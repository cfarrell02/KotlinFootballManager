package org.setu.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class StaffTest {
    val staff = mutableListOf<Staff>()

    @BeforeEach
    fun setUp() {
        staff.add(Staff("John Doe", java.time.LocalDate.of(1990, 1, 1), "English", "Manager", 100000.0))
        staff.add(Staff("Jim Johnson", java.time.LocalDate.of(1990, 1, 1), "Irish", "Assistant Manager", 50000.0))
        staff.add(Staff("Jack Jackson", java.time.LocalDate.of(1990, 1, 1), "Scottish", "Coach", 25000.0))
    }

    @AfterEach
    fun tearDown() {
        staff.clear()
    }

    @Test
    fun testToString() {
        assertTrue(staff[0].toString().contains("John Doe"))
        assertTrue(staff[0].toString().contains("1990-01-01"))
        assertTrue(staff[0].toString().contains("English"))
        assertTrue(staff[0].toString().contains("Manager"))
        assertTrue(staff[0].toString().contains("100000.0"))
    }

    @Test
    fun getRole() {
        assertEquals("Manager", staff[0].role)
        assertEquals("Assistant Manager", staff[1].role)
        assertEquals("Coach", staff[2].role)
    }

    @Test
    fun setRole() {
        staff[0].role = "Head Coach"
        assertEquals("Head Coach", staff[0].role)
    }

    @Test
    fun getSalary() {
        assertEquals(100000.0, staff[0].salary)
        assertEquals(50000.0, staff[1].salary)
        assertEquals(25000.0, staff[2].salary)
    }

    @Test
    fun setSalary() {
        staff[0].salary = 150000.0
        assertEquals(150000.0, staff[0].salary)
    }
}