package org.firstinspires.ftc.teamcode.exceptions

/**
 * Exception thrown when no given hardware could be found
 */
class HardwareMissingException(hardwareID: String) :
    Exception("Could not find hardware with ID: $hardwareID!")
