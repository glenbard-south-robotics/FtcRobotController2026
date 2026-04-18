package org.firstinspires.ftc.teamcode.state

import org.firstinspires.ftc.teamcode.exceptions.InvalidMotorDirectionPowerCoefficientException

enum class MotorDirection {
    FORWARD, REVERSE;

    /**
     * Get the motor power coefficient of the given MotorDirection
     */
    fun getPowerCoefficient(): Double {
        return when (this) {
            FORWARD -> 1.0
            REVERSE -> -1.0
        }
    }


    /**
     * Get the display string of the given MotorDirection
     */
    fun getDisplay(): String {
        return when (this) {
            FORWARD -> "forward"
            REVERSE -> "reverse"
        }
    }
}

/**
 * Construct a new MotorDirection from its motor power coefficient
 * @throws InvalidMotorDirectionPowerCoefficientException
 */
fun fromPowerCoefficient(power: Double): MotorDirection {
    return when (power) {
        1.0 -> MotorDirection.FORWARD
        -1.0 -> MotorDirection.REVERSE
        else -> {
            throw InvalidMotorDirectionPowerCoefficientException(power)
        }
    }
}

operator fun MotorDirection.times(other: MotorDirection): MotorDirection {
    return fromPowerCoefficient(this.getPowerCoefficient() * other.getPowerCoefficient())
}