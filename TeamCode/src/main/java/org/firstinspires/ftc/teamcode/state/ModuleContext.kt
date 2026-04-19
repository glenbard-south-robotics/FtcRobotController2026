package org.firstinspires.ftc.teamcode.state

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * A class encompassing common data for a module
 */
class ModuleContext(
    val opMode: LinearOpMode,
    val bindingManager: BindingManager,
    val hardwareManager: HardwareManager,
    val telemetry: Telemetry
) {
    constructor(opMode: LinearOpMode) : this(
        opMode,
        BindingManager(GamepadPair(opMode.gamepad1, opMode.gamepad2)),
        HardwareManager(opMode.hardwareMap),
        opMode.telemetry
    )
}
