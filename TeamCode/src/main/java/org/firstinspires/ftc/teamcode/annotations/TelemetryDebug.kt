package org.firstinspires.ftc.teamcode.annotations

@Target(
    AnnotationTarget.FIELD, AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
annotation class TelemetryDebug(
    val group: String = "default"
)