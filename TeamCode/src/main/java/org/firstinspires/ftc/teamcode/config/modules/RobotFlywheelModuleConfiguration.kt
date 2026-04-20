package org.firstinspires.ftc.teamcode.config.modules

import org.firstinspires.ftc.teamcode.config.AnalogAction
import org.firstinspires.ftc.teamcode.config.AnalogBinding
import org.firstinspires.ftc.teamcode.config.BinaryAction
import org.firstinspires.ftc.teamcode.config.BinaryBinding
import org.firstinspires.ftc.teamcode.config.GamepadID
import org.firstinspires.ftc.teamcode.config.RobotModuleConfiguration
import org.firstinspires.ftc.teamcode.modules.ModuleCommands
import org.firstinspires.ftc.teamcode.state.MotorDirection

@Suppress("ConstPropertyName")
object RobotFlywheelModuleConfiguration : RobotModuleConfiguration {
    override val debugTelemetry = false

    override val binaryBindings = mapOf(
        ModuleCommands.BASE_SLOW_TOGGLE to BinaryBinding(
            GamepadID.GAMEPAD_ONE,
            BinaryAction.LEFT_STICK_BUTTON
        )
    )
    override val analogBindings = mapOf(
        AnalogAction.LEFT_STICK_Y to AnalogBinding(
            GamepadID.GAMEPAD_ONE,
            AnalogAction.LEFT_STICK_Y,
            deadZone = 0.05f
        ),
        AnalogAction.RIGHT_STICK_Y to AnalogBinding(
            GamepadID.GAMEPAD_ONE,
            AnalogAction.RIGHT_STICK_Y,
            deadZone = 0.05f
        ),
    )

    val motorDirection = MotorDirection.FORWARD

    val teleopVelocity = 2500.0
    val teleopSlowVelocity = 2000.0

    val rumbleErrorEpsilon = 10.0

    init {
        validate()
    }
}