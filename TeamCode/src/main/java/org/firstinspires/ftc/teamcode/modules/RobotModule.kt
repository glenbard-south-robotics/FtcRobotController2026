package org.firstinspires.ftc.teamcode.modules

import org.firstinspires.ftc.teamcode.config.AnalogAction
import org.firstinspires.ftc.teamcode.config.RobotModuleConfiguration
import org.firstinspires.ftc.teamcode.state.RobotModuleContext
import org.firstinspires.ftc.teamcode.state.managers.TelemetryManager
import org.firstinspires.ftc.teamcode.state.managers.addDebug
import org.firstinspires.ftc.teamcode.state.managers.read
import org.firstinspires.ftc.teamcode.state.managers.wasPressed
import org.firstinspires.ftc.teamcode.state.managers.wasReleased

abstract class RobotModule<C : RobotModuleConfiguration>(
    protected val context: RobotModuleContext,
    protected val config: C,
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

    fun readBinaryPressed(action: ModuleCommands): Boolean {
        return action.wasPressed(context.bindingManager, config)
    }

    fun readBinary(action: ModuleCommands): Boolean {
        return action.read(context.bindingManager, config)
    }

    fun readBinaryReleased(action: ModuleCommands): Boolean {
        return action.wasReleased(context.bindingManager, config)
    }

    fun emitDebugTelemetry() {
        if (!config.debugTelemetry) return

        TelemetryManager(context.telemetry, this, groupPrefix = this::class.simpleName).addDebug()
    }
}