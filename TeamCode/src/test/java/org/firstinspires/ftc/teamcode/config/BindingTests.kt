package org.firstinspires.ftc.teamcode.config

import org.junit.Assert.*
import org.junit.Test

class BindingTests {

    @Test
    fun gamepadIDSize() {
        assertEquals(2, GamepadID.entries.size)
    }

    @Test
    fun gamepadIDs() {
        assertTrue(GamepadID.entries.contains(GamepadID.GAMEPAD_ONE))
        assertTrue(GamepadID.entries.contains(GamepadID.GAMEPAD_TWO))
    }

    @Test
    fun binaryActionSize() {
        assertEquals(12, BinaryAction.entries.size)
    }

    @Test
    fun binaryActions() {
        val expected = setOf(
            BinaryAction.CROSS, BinaryAction.CIRCLE, BinaryAction.TRIANGLE, BinaryAction.SQUARE,
            BinaryAction.DPAD_UP, BinaryAction.DPAD_DOWN, BinaryAction.DPAD_LEFT, BinaryAction.DPAD_RIGHT,
            BinaryAction.LEFT_BUMPER, BinaryAction.RIGHT_BUMPER,
            BinaryAction.LEFT_STICK_BUTTON, BinaryAction.RIGHT_STICK_BUTTON
        )
        assertEquals(expected, BinaryAction.entries.toSet())
    }

    @Test
    fun analogActionSize() {
        assertEquals(6, AnalogAction.entries.size)
    }

    @Test
    fun analogActions() {
        val expected = setOf(
            AnalogAction.LEFT_STICK_X, AnalogAction.LEFT_STICK_Y,
            AnalogAction.RIGHT_STICK_X, AnalogAction.RIGHT_STICK_Y,
            AnalogAction.LEFT_TRIGGER, AnalogAction.RIGHT_TRIGGER
        )
        assertEquals(expected, AnalogAction.entries.toSet())
    }

    @Test
    fun binaryBindingFields() {
        val binding = BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS)
        assertEquals(GamepadID.GAMEPAD_ONE, binding.gamepad)
        assertEquals(BinaryAction.CROSS, binding.button)
    }

    @Test
    fun binaryBindingEquals() {
        val a = BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS)
        val b = BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS)
        assertEquals(a, b)
    }

    @Test
    fun binaryBindingDifferentGamepad() {
        val a = BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS)
        val b = BinaryBinding(GamepadID.GAMEPAD_TWO, BinaryAction.CROSS)
        assertNotEquals(a, b)
    }

    @Test
    fun binaryBindingDifferentButton() {
        val a = BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS)
        val b = BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CIRCLE)
        assertNotEquals(a, b)
    }

    @Test
    fun binaryBindingCopy() {
        val original = BinaryBinding(GamepadID.GAMEPAD_ONE, BinaryAction.CROSS)
        val copy = original.copy(button = BinaryAction.TRIANGLE)
        assertEquals(GamepadID.GAMEPAD_ONE, copy.gamepad)
        assertEquals(BinaryAction.TRIANGLE, copy.button)
    }

    @Test
    fun analogBindingFields() {
        val binding = AnalogBinding(GamepadID.GAMEPAD_TWO, AnalogAction.LEFT_STICK_X, deadZone = 0.1f, scale = 2.0f)
        assertEquals(GamepadID.GAMEPAD_TWO, binding.gamepad)
        assertEquals(AnalogAction.LEFT_STICK_X, binding.analogAction)
        assertEquals(0.1f, binding.deadZone)
        assertEquals(2.0f, binding.scale)
    }

    @Test
    fun analogBindingDefaults() {
        val binding = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.RIGHT_STICK_Y)
        assertEquals(0f, binding.deadZone)
        assertEquals(1f, binding.scale)
    }

    @Test
    fun analogBindingEquality() {
        val a = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_TRIGGER, 0.1f, 1.5f)
        val b = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_TRIGGER, 0.1f, 1.5f)
        assertEquals(a, b)
    }

    @Test
    fun analogBindingDifferentDeadzone() {
        val a = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_TRIGGER, deadZone = 0.1f)
        val b = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_TRIGGER, deadZone = 0.2f)
        assertNotEquals(a, b)
    }

    @Test
    fun analogBindingDifferentScale() {
        val a = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_TRIGGER, scale = 1.0f)
        val b = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_TRIGGER, scale = 2.0f)
        assertNotEquals(a, b)
    }

    @Test
    fun analogBindingCopy() {
        val original = AnalogBinding(GamepadID.GAMEPAD_ONE, AnalogAction.LEFT_STICK_X, deadZone = 0.1f, scale = 1.0f)
        val copy = original.copy(deadZone = 0.2f)
        assertEquals(GamepadID.GAMEPAD_ONE, copy.gamepad)
        assertEquals(AnalogAction.LEFT_STICK_X, copy.analogAction)
        assertEquals(0.2f, copy.deadZone)
        assertEquals(1.0f, copy.scale)
    }
}