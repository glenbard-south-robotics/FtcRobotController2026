package org.firstinspires.ftc.teamcode.exceptions

/**
 * Exception thrown when an invalid value is passed in constructing a [MotorDirection][org.firstinspires.ftc.teamcode.state.MotorDirection] from a double
 */
class InvalidMotorDirectionPowerCoefficientException(power: Double) : Exception("Invalid motor direction power coefficient: $power!")
