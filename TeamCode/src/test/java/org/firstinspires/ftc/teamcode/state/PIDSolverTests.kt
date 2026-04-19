package org.firstinspires.ftc.teamcode.state

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PIDSolverTest {
    private lateinit var pid: PIDSolver<Double>

    @Before
    fun setup() {
        pid = PIDSolver(
            dt = 0.1,
            kp = 1.0,
            ki = 0.0,
            kd = 0.0,
            toDouble = { it },
            fromDouble = { it }
        )
    }

    @Test
    fun proportionalControlProducesCorrectOutput() {
        val output = pid.update(10.0, 0.0)

        assertEquals(10.0, output, 1e-6)
    }

    @Test
    fun zeroErrorProducesZeroOutput() {
        val output = pid.update(5.0, 5.0)
        assertEquals(0.0, output, 1e-6)
    }

    @Test
    fun integralAccumulatesOverTime() {
        pid = PIDSolver(
            dt = 1.0,
            kp = 0.0,
            ki = 2.0,
            kd = 0.0,
            toDouble = { it },
            fromDouble = { it }
        )

        pid.update(1.0, 0.0) // I = 1
        val output = pid.update(1.0, 0.0) // I = 2

        assertEquals(4.0, output, 1e-6)
    }

    @Test
    fun derivativeDampsChange() {
        pid = PIDSolver(
            dt = 1.0,
            kp = 0.0,
            ki = 0.0,
            kd = 1.0,
            toDouble = { it },
            fromDouble = { it }
        )

        pid.update(10.0, 0.0)

        val output = pid.update(5.0, 0.0)

        assertEquals(-5.0, output, 1e-6)
    }

    @Test
    fun resetClearsState() {
        pid = PIDSolver(
            dt = 1.0,
            kp = 0.0,
            ki = 1.0,
            kd = 0.0,
            toDouble = { it },
            fromDouble = { it }
        )

        pid.update(1.0, 0.0) // I = 1
        pid.reset()

        val output = pid.update(1.0, 0.0)

        assertEquals(1.0, output, 1e-6)
    }
}