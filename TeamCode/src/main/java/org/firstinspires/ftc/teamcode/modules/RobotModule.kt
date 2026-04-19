package org.firstinspires.ftc.teamcode.modules

import org.firstinspires.ftc.teamcode.config.AnalogAction
import org.firstinspires.ftc.teamcode.config.Commands
import org.firstinspires.ftc.teamcode.config.ModuleConfiguration
import org.firstinspires.ftc.teamcode.state.ModuleContext
import org.firstinspires.ftc.teamcode.state.read
import org.firstinspires.ftc.teamcode.state.wasPressed
import org.firstinspires.ftc.teamcode.state.wasReleased

abstract class RobotModule(
    protected val context: ModuleContext,
    protected val config: ModuleConfiguration,
) {
    /**
     * Called on the start of the module
     */
    abstract fun initialize(): Result<Unit>

    /**
     * Function is called each loop, this should be used to schedule commands
     */
    abstract fun pollInputs()

    /**
     * Called when the module should be shutdown
     */
    abstract fun shutdown(): Result<Unit>

    fun readAnalog(action: AnalogAction): Float {
        return action.read(context.bindingManager, config)
    }

    fun readBinaryPressed(action: Commands): Boolean {
        return action.wasPressed(context.bindingManager, config)
    }

    fun readBinary(action: Commands): Boolean {
        return action.read(context.bindingManager, config)
    }

    fun readBinaryReleased(action: Commands): Boolean {
        return action.wasReleased(context.bindingManager, config)
    }

    fun emitDebugTelemetry() {
        if (!config.debugTelemetry) return

        // TODO: Hook up runtime-annotations
    }
}