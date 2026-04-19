package org.firstinspires.ftc.teamcode.modules.robot

import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups.race

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.annotations.TelemetryDebug
import org.firstinspires.ftc.teamcode.config.AnalogAction
import org.firstinspires.ftc.teamcode.config.GamepadID
import org.firstinspires.ftc.teamcode.config.modules.RobotBaseModuleConfiguration
import org.firstinspires.ftc.teamcode.modules.ModuleCommands
import org.firstinspires.ftc.teamcode.modules.RobotModule
import org.firstinspires.ftc.teamcode.state.RobotModuleContext
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private const val WHEELS_INCHES_TO_TICKS = 140.0 / Math.PI

class RobotBaseModule(context: RobotModuleContext) : RobotModule<RobotBaseModuleConfiguration>(
    context, RobotBaseModuleConfiguration
) {
    private lateinit var leftDrive: DcMotor
    private lateinit var rightDrive: DcMotor

    private var fineAdjustMode = false

    override fun initialize(): Result<Unit> {
        context.hardwareManager.getHardware<DcMotor>("leftDrive")
            .fold({ leftDrive = it }, { return Result.failure(it) })

        context.hardwareManager.getHardware<DcMotor>("rightDrive")
            .fold({ rightDrive = it }, { return Result.failure(it) })

        leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        return Result.success(Unit)
    }

    override fun shutdown(): Result<Unit> {
        Scheduler.reset()
        setMotorPowers(0.0, 0.0)
        return Result.success(Unit)
    }

    fun setMotorPowers(leftPower: Double, rightPower: Double): Command = instant {
        val leftCoefficient = config.leftMotorDirection.getPowerCoefficient()
        val rightCoefficient = config.rightMotorDirection.getPowerCoefficient()

        leftDrive.power = leftCoefficient * leftPower
        rightDrive.power = rightCoefficient * rightPower
    }

    fun stop(): Command = setMotorPowers(0.0, 0.0)

    fun driveInches(
        speed: Double,
        leftInches: Int,
        rightInches: Int,
        timeoutMs: Double = config.defaultAutoDriveTimeoutMs
    ): Command {
        val leftTicks =
            (leftInches * WHEELS_INCHES_TO_TICKS * config.leftMotorDirection.getPowerCoefficient()).roundToInt()
        val rightTicks =
            (rightInches * WHEELS_INCHES_TO_TICKS * config.rightMotorDirection.getPowerCoefficient()).roundToInt()

        val drive = Command.build()
            .setStart {
                leftDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                rightDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                leftDrive.targetPosition = leftTicks
                rightDrive.targetPosition = rightTicks
                leftDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
                rightDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
                leftDrive.power = abs(speed) * config.leftMotorDirection.getPowerCoefficient()
                rightDrive.power = abs(speed) * config.rightMotorDirection.getPowerCoefficient()
            }
            .setDone { !leftDrive.isBusy && !rightDrive.isBusy }
            .setEnd {
                leftDrive.power = 0.0
                rightDrive.power = 0.0
                leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            }

        return race(drive, waitMs(timeoutMs))
    }

    fun drivePower(
        speed: Double,
        leftPower: Double,
        rightPower: Double,
        timeoutMs: Double = config.defaultAutoDriveTimeoutMs
    ): Command {
        val leftTicks =
            (leftPower * WHEELS_INCHES_TO_TICKS * config.leftMotorDirection.getPowerCoefficient()).roundToInt()
        val rightTicks =
            (rightPower * WHEELS_INCHES_TO_TICKS * config.rightMotorDirection.getPowerCoefficient()).roundToInt()

        val drive = Command.build()
            .setStart {
                leftDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                rightDrive.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                leftDrive.targetPosition = leftTicks
                rightDrive.targetPosition = rightTicks
                leftDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
                rightDrive.mode = DcMotor.RunMode.RUN_TO_POSITION
                leftDrive.power = abs(speed) * config.leftMotorDirection.getPowerCoefficient()
                rightDrive.power = abs(speed) * config.rightMotorDirection.getPowerCoefficient()
            }
            .setDone { !leftDrive.isBusy && !rightDrive.isBusy }
            .setEnd {
                leftDrive.power = 0.0
                rightDrive.power = 0.0
                leftDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                rightDrive.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            }

        return race(drive, waitMs(timeoutMs))
    }

    override fun pollInputs() {
        if (readBinaryPressed(ModuleCommands.BASE_SLOW_TOGGLE)) {
            fineAdjustMode = !fineAdjustMode
            context.bindingManager.rumble(250.milliseconds, GamepadID.GAMEPAD_ONE)
        }

        val coefficient = if (fineAdjustMode) config.fineAdjustPowerCoefficient
        else config.basePowerCoefficient

        val leftY = readAnalog(AnalogAction.LEFT_STICK_Y).toDouble()
        val rightY = readAnalog(AnalogAction.RIGHT_STICK_Y).toDouble()

        setMotorPowers(leftY * coefficient, rightY * coefficient).schedule()
    }

    @TelemetryDebug(group = "Base")
    fun fineAdjustEnabled(): Boolean = fineAdjustMode

    @TelemetryDebug(group = "Base/Motors")
    fun leftMotorPower(): Double = leftDrive.power

    @TelemetryDebug(group = "Base/Motors")
    fun rightMotorPower(): Double = rightDrive.power

    @TelemetryDebug(group = "Base/Motors")
    fun leftMotorPosition(): Int = leftDrive.currentPosition

    @TelemetryDebug(group = "Base/Motors")
    fun rightMotorPosition(): Int = rightDrive.currentPosition

    @TelemetryDebug(group = "Base/Motors")
    fun leftMotorTarget(): Int = leftDrive.targetPosition

    @TelemetryDebug(group = "Base/Motors")
    fun rightMotorTarget(): Int = rightDrive.targetPosition
}