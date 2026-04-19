package org.firstinspires.ftc.teamcode.exceptions

/**
 * Exception thrown when the Duration to milliseconds cast overflows in [BindingManager.rumble]
 */
class InvalidRumbleDurationException(durationMs: Long) : Exception("Invalid rumble duration: $durationMs!")
