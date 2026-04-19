package org.firstinspires.ftc.teamcode.config

import org.junit.Assert
import org.junit.Test

class CommandsTests {

    @Test
    fun commandsSize() {
        Assert.assertEquals(6, Commands.entries.size)
    }

    @Test
    fun commandsHaveAllExpected() {
        val expected = setOf(
            Commands.INTAKE_FORWARD,
            Commands.INTAKE_REVERSE,
            Commands.INTAKE_SLOW_TOGGLE,
            Commands.FLYWHEEL_TOGGLE,
            Commands.FLYWHEEL_TOGGLE_SLOW_MODE,
            Commands.BASE_SLOW_TOGGLE,
        )
        Assert.assertEquals(expected, Commands.entries.toSet())
    }
}