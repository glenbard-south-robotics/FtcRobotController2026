package org.firstinspires.ftc.teamcode.config

import org.firstinspires.ftc.teamcode.commands.Commands
import org.firstinspires.ftc.teamcode.exceptions.ModuleValidationException
import org.junit.Assert.*
import org.junit.Test

class ModuleConfigurationTests {
    private val emptyConfig = object : ModuleConfiguration {
        override val debugTelemetry = false
        override val binaryBindings: Map<Commands, BinaryBinding> = emptyMap()
        override val analogBindings: Map<AnalogAction, AnalogBinding> = emptyMap()
    }

    private val populatedConfig = object : ModuleConfiguration {
        override val debugTelemetry = true
        override val binaryBindings: Map<Commands, BinaryBinding> = mapOf(
            Commands.INTAKE_FORWARD to BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS),
            Commands.INTAKE_REVERSE to BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CIRCLE),
        )
        override val analogBindings: Map<AnalogAction, AnalogBinding> = mapOf(
            AnalogAction.LEFT_STICK_X to AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_STICK_X, deadZone = 0.1f, scale = 1.0f),
        )
    }

    private fun configWithBinaryBindings(vararg pairs: Pair<Commands, BinaryBinding>) = object : ModuleConfiguration {
        override val debugTelemetry = false
        override val binaryBindings: Map<Commands, BinaryBinding> = mapOf(*pairs)
        override val analogBindings: Map<AnalogAction, AnalogBinding> = emptyMap()
    }

    private fun configWithAnalogBindings(vararg pairs: Pair<AnalogAction, AnalogBinding>) = object : ModuleConfiguration {
        override val debugTelemetry = false
        override val binaryBindings: Map<Commands, BinaryBinding> = emptyMap()
        override val analogBindings: Map<AnalogAction, AnalogBinding> = mapOf(*pairs)
    }

    @Test
    fun `debugTelemetry can be false`() {
        assertFalse(emptyConfig.debugTelemetry)
    }

    @Test
    fun `debugTelemetry can be true`() {
        assertTrue(populatedConfig.debugTelemetry)
    }

    @Test
    fun `binaryBindings can be empty`() {
        assertTrue(emptyConfig.binaryBindings.isEmpty())
    }

    @Test
    fun `binaryBindings returns correct binding for command`() {
        assertEquals(
            BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS),
            populatedConfig.binaryBindings[Commands.INTAKE_FORWARD]
        )
    }

    @Test
    fun `binaryBindings returns null for unbound command`() {
        assertNull(populatedConfig.binaryBindings[Commands.FLYWHEEL_TOGGLE])
    }

    @Test
    fun `analogBindings can be empty`() {
        assertTrue(emptyConfig.analogBindings.isEmpty())
    }

    @Test
    fun `analogBindings returns correct binding for action`() {
        assertEquals(
            AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_STICK_X, deadZone = 0.1f, scale = 1.0f),
            populatedConfig.analogBindings[AnalogAction.LEFT_STICK_X]
        )
    }

    @Test
    fun `analogBindings returns null for unbound action`() {
        assertNull(populatedConfig.analogBindings[AnalogAction.RIGHT_STICK_X])
    }

    @Test
    fun `validate passes with empty binary bindings`() {
        emptyConfig.validate()
    }

    @Test
    fun `validate passes with no duplicate binary bindings`() {
        populatedConfig.validate()
    }

    @Test
    fun `validate throws on duplicate binary bindings`() {
        val config = configWithBinaryBindings(
            Commands.INTAKE_FORWARD to BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS),
            Commands.INTAKE_REVERSE to BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS),
        )
        assertThrows(ModuleValidationException::class.java) { config.validate() }
    }

    @Test
    fun `validate throws on duplicate binary bindings across gamepads with same button`() {
        val config = configWithBinaryBindings(
            Commands.INTAKE_FORWARD to BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS),
            Commands.INTAKE_REVERSE to BinaryBinding(GamepadID.GAMEPAD_TWO, BinaryAction.CROSS),
        )

        config.validate()
    }

    @Test
    fun `validate passes with empty analog bindings`() {
        emptyConfig.validate()
    }

    @Test
    fun `validate passes with no duplicate analog bindings`() {
        populatedConfig.validate()
    }

    @Test
    fun `validate throws on duplicate analog bindings`() {
        val config = configWithAnalogBindings(
            AnalogAction.LEFT_STICK_X to AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_STICK_X),
            AnalogAction.RIGHT_STICK_X to AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_STICK_X),
        )
        assertThrows(ModuleValidationException::class.java) { config.validate() }
    }

    @Test
    fun `validate throws on duplicate analog bindings across gamepads with same action`() {
        val config = configWithAnalogBindings(
            AnalogAction.LEFT_STICK_X to AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_STICK_X),
            AnalogAction.RIGHT_STICK_X to AnalogBinding(GamepadID.GAMEPAD_TWO, AnalogAction.LEFT_STICK_X),
        )

        config.validate()
    }

}