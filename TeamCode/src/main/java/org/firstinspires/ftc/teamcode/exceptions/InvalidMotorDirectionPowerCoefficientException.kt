package org.firstinspires.ftc.teamcode.exceptions

/**
 * Thrown by [org.firstinspires.ftc.teamcode.state.MotorDirection.fromPowerCoefficient]
 */
class InvalidMotorDirectionPowerCoefficientException(power: Double) : Exception("Invalid motor direction power coefficient: $power!")
