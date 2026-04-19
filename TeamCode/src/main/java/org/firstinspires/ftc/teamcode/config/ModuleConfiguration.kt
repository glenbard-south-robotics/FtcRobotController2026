package org.firstinspires.ftc.teamcode.config

import org.firstinspires.ftc.teamcode.exceptions.ModuleValidationException

interface ModuleConfiguration {
    val debugTelemetry: Boolean

    val binaryBindings: Map<Commands, BinaryBinding>
    val analogBindings: Map<AnalogAction, AnalogBinding>

    /**
     * Validate the module configuration
     * @throws org.firstinspires.ftc.teamcode.exceptions.ModuleValidationException
     */
    fun validate() {
        val binaryValues = binaryBindings.values.toList()
        if (binaryValues.size != binaryValues.toSet().size) {
            throw ModuleValidationException("Duplicate binary bindings in ${this::class.simpleName}")
        }

        val analogValues = analogBindings.values.toList()
        if (analogValues.size != analogValues.toSet().size) {
            throw ModuleValidationException("Duplicate analog bindings in ${this::class.simpleName}")
        }
    }
}