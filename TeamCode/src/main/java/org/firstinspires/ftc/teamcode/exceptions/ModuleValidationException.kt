package org.firstinspires.ftc.teamcode.exceptions

/**
 * Exception thrown when an invalid module configuration is found
 */
class ModuleValidationException(message: String) : Exception("Invalid module configuration: $message!")
