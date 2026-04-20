package org.firstinspires.ftc.teamcode.opmodes

import com.pedropathing.ivy.Scheduler
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.modules.RobotModule
import org.firstinspires.ftc.teamcode.state.RobotModuleContext

abstract class RobotOpMode : LinearOpMode() {
    protected lateinit var context: RobotModuleContext
        private set

    private val modules = mutableListOf<RobotModule<*>>()
    private var initialized = false

    /**
     * Register modules to be managed by this OpMode
     */
    protected fun registerModules(vararg newModules: RobotModule<*>) {
        check(!initialized) { "Cannot register modules after initialization" }
        modules.addAll(newModules)
    }

    /**
     * Behaviors to run before the start button is pressed
     */
    abstract fun initialize()

    /**
     * Behaviors to run once after start is pressed, before the loop
     */
    open fun onStart() {}

    /**
     * Behaviors to run each iteration
     */
    open fun tick() {}

    /**
     * Behaviors to run after the stop button is pressed
     */
    open fun shutdown() {}

    private fun initializeModules() {
        for (module in modules) {
            module.initialize().getOrElse { throw it }
        }
    }

    private fun pollModules() {
        for (module in modules) {
            module.pollInputs()
            module.emitDebugTelemetry()
        }
    }

    private fun shutdownModules() {
        for (module in modules) {
            module.shutdown().onFailure {
                telemetry.addLine("[WARN] Module shutdown failed: ${it.message}")
            }
        }
    }

    override fun runOpMode() {
        context = RobotModuleContext(this)
        Scheduler.reset()

        try {
            initialize()
            initialized = true
            initializeModules()

            waitForStart()

            onStart()

            while (opModeIsActive()) {
                pollModules()
                Scheduler.execute()
                loop()
                telemetry.update()
                idle()
            }
        } catch (e: Exception) {
            telemetry.addLine("[FATAL] ${e.message}")
            telemetry.update()
            requestOpModeStop()
            throw e
        } finally {
            shutdown()
            shutdownModules()
            telemetry.update()
        }
    }
}