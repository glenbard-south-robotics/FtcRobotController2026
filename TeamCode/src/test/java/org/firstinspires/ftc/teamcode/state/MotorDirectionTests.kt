package org.firstinspires.ftc.teamcode.state

import org.firstinspires.ftc.teamcode.exceptions.InvalidMotorDirectionPowerCoefficientException
import org.junit.Test
import org.junit.Assert.*

class MotorDirectionTests {
    @Test
    fun getPowerCoefficient() {
        // Doubles cannot be compared well, so we need to provide a reasonable amount of error
        assertEquals(1.0, MotorDirection.FORWARD.getPowerCoefficient(), 0.001)
        assertEquals(-1.0, MotorDirection.REVERSE.getPowerCoefficient(), 0.001)
    }

    @Test
    fun fromPowerCoefficient() {
        assertEquals(MotorDirection.FORWARD, fromPowerCoefficient(1.0))
        assertEquals(MotorDirection.REVERSE, fromPowerCoefficient(-1.0))

        assertThrows(
            InvalidMotorDirectionPowerCoefficientException::class.java
        ) { fromPowerCoefficient(-2.0) }
    }

    @Test
    fun getDisplay() {
        assertEquals(MotorDirection.FORWARD.getDisplay(), "forward")
        assertEquals(MotorDirection.REVERSE.getDisplay(), "reverse")
    }

    @Test
    fun times() {
        assertEquals(MotorDirection.FORWARD, MotorDirection.REVERSE * MotorDirection.REVERSE)
        assertEquals(MotorDirection.REVERSE, MotorDirection.FORWARD * MotorDirection.REVERSE)
        assertEquals(MotorDirection.REVERSE, MotorDirection.REVERSE * MotorDirection.FORWARD)
        assertEquals(MotorDirection.FORWARD, MotorDirection.FORWARD * MotorDirection.FORWARD)
    }
}