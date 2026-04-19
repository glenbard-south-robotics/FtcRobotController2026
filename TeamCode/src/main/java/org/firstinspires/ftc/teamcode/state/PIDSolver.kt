package org.firstinspires.ftc.teamcode.state

typealias PIDTime = Double

/**
 * Generic PID solver, `T` must implement `toDouble` and `fromDouble`
 */
class PIDSolver<T>(
    private val dt: PIDTime = 0.01,

    private val kp: Double,
    private val ki: Double = 0.0,
    private val kd: Double = 0.0,

    private val toDouble: (T) -> Double,
    private val fromDouble: (Double) -> T,
) {
    private var integral = 0.0
    private var previousError = 0.0

    private var firstRun = true

    fun reset() {
        integral = 0.0
        previousError = 0.0
        firstRun = true
    }

    fun update(setpoint: T, measurement: T): T {
        val error = toDouble(setpoint) - toDouble(measurement)

        integral += error * dt

        val derivative = if (firstRun) {
            firstRun = false
            0.0
        } else {
            (error - previousError) / dt
        }

        previousError = error

        val output = kp * error + ki * integral + kd * derivative

        return fromDouble(
            output
        )
    }
}