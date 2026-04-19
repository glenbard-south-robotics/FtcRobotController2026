package org.firstinspires.ftc.teamcode.state.managers

import org.firstinspires.ftc.teamcode.config.AnalogAction
import org.firstinspires.ftc.teamcode.config.AnalogBinding
import org.firstinspires.ftc.teamcode.config.BinaryAction
import org.firstinspires.ftc.teamcode.config.BinaryBinding
import org.firstinspires.ftc.teamcode.config.GamepadID
import org.firstinspires.ftc.teamcode.config.RobotModuleConfiguration
import org.firstinspires.ftc.teamcode.exceptions.InvalidRumbleDurationException
import org.firstinspires.ftc.teamcode.modules.ModuleCommands
import org.firstinspires.ftc.teamcode.state.GamepadPair
import kotlin.math.abs
import kotlin.time.Duration

data class BindingManager(val gamepadPair: GamepadPair) {
    /**
     * Get the gamepad associated with this [GamepadID]
     */
    private fun gamepadFor(id: GamepadID) = when (id) {
        GamepadID.GAMEPAD_ONE -> gamepadPair.gamepadOne
        GamepadID.GAMEPAD_TWO -> gamepadPair.gamepadTwo
    }

    /**
     * Check if the binding was pressed since the last call
     */
    fun binaryWasPressed(binding: BinaryBinding): Boolean {
        val gamepad = gamepadFor(binding.gamepad)
        return when (binding.button) {
            BinaryAction.CROSS -> gamepad.crossWasPressed()
            BinaryAction.CIRCLE -> gamepad.circleWasPressed()
            BinaryAction.TRIANGLE -> gamepad.triangleWasPressed()
            BinaryAction.SQUARE -> gamepad.squareWasPressed()
            BinaryAction.DPAD_UP -> gamepad.dpadUpWasPressed()
            BinaryAction.DPAD_DOWN -> gamepad.dpadDownWasPressed()
            BinaryAction.DPAD_LEFT -> gamepad.dpadLeftWasPressed()
            BinaryAction.DPAD_RIGHT -> gamepad.dpadRightWasPressed()
            BinaryAction.LEFT_BUMPER -> gamepad.leftBumperWasPressed()
            BinaryAction.RIGHT_BUMPER -> gamepad.rightBumperWasPressed()
            BinaryAction.LEFT_STICK_BUTTON -> gamepad.leftStickButtonWasPressed()
            BinaryAction.RIGHT_STICK_BUTTON -> gamepad.rightStickButtonWasPressed()
        }
    }

    /**
     *Check if the binding was released since the last call
     */
    fun binaryWasReleased(binding: BinaryBinding): Boolean {
        val gamepad = gamepadFor(binding.gamepad)
        return when (binding.button) {
            BinaryAction.CROSS -> gamepad.crossWasReleased()
            BinaryAction.CIRCLE -> gamepad.circleWasReleased()
            BinaryAction.TRIANGLE -> gamepad.triangleWasReleased()
            BinaryAction.SQUARE -> gamepad.squareWasReleased()
            BinaryAction.DPAD_UP -> gamepad.dpadUpWasReleased()
            BinaryAction.DPAD_DOWN -> gamepad.dpadDownWasReleased()
            BinaryAction.DPAD_LEFT -> gamepad.dpadLeftWasReleased()
            BinaryAction.DPAD_RIGHT -> gamepad.dpadRightWasReleased()
            BinaryAction.LEFT_BUMPER -> gamepad.leftBumperWasReleased()
            BinaryAction.RIGHT_BUMPER -> gamepad.rightBumperWasReleased()
            BinaryAction.LEFT_STICK_BUTTON -> gamepad.leftStickButtonWasReleased()
            BinaryAction.RIGHT_STICK_BUTTON -> gamepad.rightStickButtonWasReleased()
        }
    }

    /**
     * Read the value of a binary binding
     */
    fun readBinary(binding: BinaryBinding): Boolean {
        val gamepad = gamepadFor(binding.gamepad)
        return when (binding.button) {
            BinaryAction.CROSS -> gamepad.cross
            BinaryAction.CIRCLE -> gamepad.circle
            BinaryAction.TRIANGLE -> gamepad.triangle
            BinaryAction.SQUARE -> gamepad.square
            BinaryAction.DPAD_UP -> gamepad.dpad_up
            BinaryAction.DPAD_DOWN -> gamepad.dpad_down
            BinaryAction.DPAD_LEFT -> gamepad.dpad_left
            BinaryAction.DPAD_RIGHT -> gamepad.dpad_right
            BinaryAction.LEFT_BUMPER -> gamepad.left_bumper
            BinaryAction.RIGHT_BUMPER -> gamepad.right_bumper
            BinaryAction.LEFT_STICK_BUTTON -> gamepad.left_stick_button
            BinaryAction.RIGHT_STICK_BUTTON -> gamepad.right_stick_button
        }
    }

    /**
     * Read the value of an analog binding
     */
    fun readAnalog(binding: AnalogBinding): Float {
        val gamepad = gamepadFor(binding.gamepad)
        val raw = when (binding.analogAction) {
            AnalogAction.LEFT_STICK_X -> gamepad.left_stick_x
            AnalogAction.LEFT_STICK_Y -> -gamepad.left_stick_y
            AnalogAction.RIGHT_STICK_X -> gamepad.right_stick_x
            AnalogAction.RIGHT_STICK_Y -> -gamepad.right_stick_y
            AnalogAction.LEFT_TRIGGER -> gamepad.left_trigger
            AnalogAction.RIGHT_TRIGGER -> gamepad.right_trigger
        }
        val v = raw.coerceIn(-1f, 1f)
        return if (abs(v) < binding.deadZone) 0f else v * binding.scale
    }

    /**
     * Rumble the given `gamepads` for `duration`
     */
    fun rumble(duration: Duration, vararg gamepads: GamepadID) {
        for (gamepadId in gamepads) {
            val gamepad = gamepadFor(gamepadId)

            val durationMs = duration.inWholeMilliseconds

            if (durationMs < Integer.MIN_VALUE || durationMs > Integer.MAX_VALUE) {
                throw InvalidRumbleDurationException(durationMs)
            }

            gamepad.rumble(durationMs.toInt())
        }
    }
}

fun AnalogAction.read(
    bindingManager: BindingManager, config: RobotModuleConfiguration
): Float {
    val binding = config.analogBindings[this] ?: return 0f
    return bindingManager.readAnalog(binding)
}

fun ModuleCommands.read(
    bindingManager: BindingManager, config: RobotModuleConfiguration
): Boolean {
    val binding = config.binaryBindings[this] ?: return false
    return bindingManager.readBinary(binding)
}

fun ModuleCommands.wasPressed(
    inputManager: BindingManager, config: RobotModuleConfiguration
): Boolean {
    val binding = config.binaryBindings[this] ?: return false
    return inputManager.binaryWasPressed(binding)
}

fun ModuleCommands.wasReleased(
    inputManager: BindingManager, config: RobotModuleConfiguration
): Boolean {
    val binding = config.binaryBindings[this] ?: return false
    return inputManager.binaryWasReleased(binding)
}