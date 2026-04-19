package org.firstinspires.ftc.teamcode.modules

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.config.AnalogAction
import org.firstinspires.ftc.teamcode.config.AnalogBinding
import org.firstinspires.ftc.teamcode.config.BinaryBinding
import org.firstinspires.ftc.teamcode.config.GamepadID
import org.firstinspires.ftc.teamcode.config.RobotModuleConfiguration
import org.firstinspires.ftc.teamcode.state.GamepadPair
import org.firstinspires.ftc.teamcode.state.RobotModuleContext
import org.firstinspires.ftc.teamcode.state.managers.BindingManager
import org.firstinspires.ftc.teamcode.state.managers.HardwareManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class RobotModuleTests {
    private inner class TestModule(
        context: RobotModuleContext,
        config: RobotModuleConfiguration,
    ) : RobotModule(context, config) {
        var initializeCalled = false
        var pollInputsCalled = false
        var shutdownCalled = false

        override fun initialize(): Result<Unit> {
            initializeCalled = true
            return Result.success(Unit)
        }

        override fun pollInputs() {
            pollInputsCalled = true
        }

        override fun shutdown(): Result<Unit> {
            shutdownCalled = true
            return Result.success(Unit)
        }
    }

    private lateinit var opMode: LinearOpMode
    private lateinit var context: RobotModuleContext
    private lateinit var config: RobotModuleConfiguration
    private lateinit var module: TestModule

    @Before
    fun setUp() {
        opMode = mock()

        context = RobotModuleContext(
            opMode = mock(),
            bindingManager = BindingManager(GamepadPair(mock(), mock())),
            hardwareManager = HardwareManager(mock()),
            telemetry = mock(),
        )

        config = object : RobotModuleConfiguration {
            override val debugTelemetry = false
            override val binaryBindings = emptyMap<ModuleCommands, BinaryBinding>()
            override val analogBindings = emptyMap<AnalogAction, AnalogBinding>()
        }

        module = TestModule(context, config)
    }

    @Test
    fun `initialize returns success`() {
        assertTrue(module.initialize().isSuccess)
    }

    @Test
    fun `initialize is called`() {
        module.initialize()
        assertTrue(module.initializeCalled)
    }

    @Test
    fun `pollInputs is called`() {
        module.pollInputs()
        assertTrue(module.pollInputsCalled)
    }

    @Test
    fun `shutdown returns success`() {
        assertTrue(module.shutdown().isSuccess)
    }

    @Test
    fun `shutdown is called`() {
        module.shutdown()
        assertTrue(module.shutdownCalled)
    }

    @Test
    fun `readBinary returns false for unbound command`() {
        assertFalse(module.readBinary(ModuleCommands.INTAKE_FORWARD))
    }

    @Test
    fun `readBinaryPressed returns false for unbound command`() {
        assertFalse(module.readBinaryPressed(ModuleCommands.INTAKE_FORWARD))
    }

    @Test
    fun `readBinaryReleased returns false for unbound command`() {
        assertFalse(module.readBinaryReleased(ModuleCommands.INTAKE_FORWARD))
    }

    @Test
    fun `readAnalog returns zero for unbound action`() {
        assertEquals(0f, module.readAnalog(AnalogAction.LEFT_STICK_X))
    }

    @Test
    fun `readAnalog returns zero for bound action with no input`() {
        val configWithBinding = object : RobotModuleConfiguration {
            override val debugTelemetry = false
            override val binaryBindings = emptyMap<ModuleCommands, BinaryBinding>()
            override val analogBindings = mapOf(
                AnalogAction.LEFT_STICK_X to AnalogBinding(
                    GamepadID.GAMEPAD_ONE,
                    AnalogAction.LEFT_STICK_X,
                    deadZone = 0.1f
                )
            )
        }
        val moduleWithBinding = TestModule(context, configWithBinding)

        // Gamepad stick defaults to 0f within deadzone, so result should be 0f
        assertEquals(0f, moduleWithBinding.readAnalog(AnalogAction.LEFT_STICK_X))
    }

    @Test
    fun `emitDebugTelemetry runs when debugTelemetry is true`() {
        val debugConfig = object : RobotModuleConfiguration {
            override val debugTelemetry = true
            override val binaryBindings = emptyMap<ModuleCommands, BinaryBinding>()
            override val analogBindings = emptyMap<AnalogAction, AnalogBinding>()
        }
        val debugModule = TestModule(context, debugConfig)

        // Should not throw even with no annotated methods on TestModule
        debugModule.emitDebugTelemetry()
    }
}