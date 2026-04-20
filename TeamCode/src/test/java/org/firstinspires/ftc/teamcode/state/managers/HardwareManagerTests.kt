package org.firstinspires.ftc.teamcode.state.managers

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.exceptions.HardwareMissingException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HardwareManagerTests {
    private lateinit var hardwareMap: HardwareMap
    private lateinit var hardwareManager: HardwareManager

    @Before
    fun setUp() {
        hardwareMap = mock()
        hardwareManager = HardwareManager(hardwareMap)
    }

    @Test
    fun `getHardware returns success when hardware is found`() {
        val motor = mock<DcMotor>()
        whenever(hardwareMap.tryGet(DcMotor::class.java, "leftDrive")).thenReturn(motor)

        val result = hardwareManager.getHardware<DcMotor>("leftDrive")

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(motor, result.getOrNull())
    }

    @Test
    fun `getHardware returns failure with HardwareMissingException when hardware is null`() {
        whenever(hardwareMap.tryGet(DcMotor::class.java, "leftDrive")).thenReturn(null)

        val result = hardwareManager.getHardware<DcMotor>("leftDrive")

        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is HardwareMissingException)
    }

    @Test
    fun `getHardware failure contains the hardware ID`() {
        whenever(hardwareMap.tryGet(DcMotor::class.java, "leftDrive")).thenReturn(null)

        val result = hardwareManager.getHardware<DcMotor>("leftDrive")
        val exception = result.exceptionOrNull() as HardwareMissingException

        Assert.assertTrue(exception.message?.contains("leftDrive") ?: false)
    }

    @Test
    fun `getHardware returns failure when hardwareMap throws`() {
        whenever(hardwareMap.tryGet(DcMotor::class.java, "leftDrive"))
            .thenThrow(RuntimeException("Hardware fault"))

        val result = hardwareManager.getHardware<DcMotor>("leftDrive")

        Assert.assertTrue(result.isFailure)
    }

    @Test
    fun `getHardware works for different hardware types`() {
        val servo = mock<Servo>()
        whenever(hardwareMap.tryGet(Servo::class.java, "claw")).thenReturn(servo)

        val result = hardwareManager.getHardware<Servo>("claw")

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(servo, result.getOrNull())
    }
}