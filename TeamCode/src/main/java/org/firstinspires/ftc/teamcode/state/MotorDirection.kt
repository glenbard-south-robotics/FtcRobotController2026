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
     * Construct a new MotorDirection from it's motor power coefficient
     * @throws InvalidMotorDirectionPowerCoefficientException
     */
    fun fromPowerCoefficient(power: Double): MotorDirection {
        return when (power) {
            1.0 -> FORWARD
            -1.0 -> REVERSE
            else -> {
                throw InvalidMotorDirectionPowerCoefficientException(power)
            }
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

operator fun MotorDirection.times(other: MotorDirection): MotorDirection {
    return fromPowerCoefficient(this.getPowerCoefficient() * other.getPowerCoefficient())
}