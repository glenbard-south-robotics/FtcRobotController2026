package org.firstinspires.ftc.teamcode.modules

import org.junit.Assert
import org.junit.Test

class CommandsTests {

    @Test
    fun commandsSize() {
        Assert.assertEquals(6, ModuleCommands.entries.size)
    }

    @Test
    fun commandsHaveAllExpected() {
        val expected = setOf(
            ModuleCommands.INTAKE_FORWARD,
            ModuleCommands.INTAKE_REVERSE,
            ModuleCommands.INTAKE_SLOW_TOGGLE,
            ModuleCommands.FLYWHEEL_TOGGLE,
            ModuleCommands.FLYWHEEL_TOGGLE_SLOW_MODE,
            ModuleCommands.BASE_SLOW_TOGGLE,
        )
        Assert.assertEquals(expected, ModuleCommands.entries.toSet())
    }
}