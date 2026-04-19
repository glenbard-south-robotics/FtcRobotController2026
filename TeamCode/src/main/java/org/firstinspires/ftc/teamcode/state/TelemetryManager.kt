package org.firstinspires.ftc.teamcode.state

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.annotations.TelemetryDebug
import org.firstinspires.ftc.teamcode.annotations.TelemetryReflectionCache

data class TelemetryManager(
    val telemetry: Telemetry, val obj: Any, val groupPrefix: String? = null
)

fun TelemetryManager.addDebug() {
    val groups = mutableMapOf<String, MutableList<Pair<String, Any?>>>()
    val clazz = obj.javaClass

    for (field in TelemetryReflectionCache.debugFields(clazz)) {
        val debug = field.getAnnotation(TelemetryDebug::class.java) ?: continue
        field.isAccessible = true

        val value = runCatching { field.get(obj) }.getOrNull()
        val group = groupPrefix?.let { "$it/${debug.group}" } ?: debug.group

        groups.getOrPut(group) { mutableListOf() }.add(field.name to value)
    }

    for (method in TelemetryReflectionCache.debugMethods(clazz)) {
        val debug = method.getAnnotation(TelemetryDebug::class.java) ?: continue
        if (method.parameterCount != 0) continue

        method.isAccessible = true
        val value = runCatching { method.invoke(obj) }.getOrNull()
        val group = groupPrefix?.let { "$it/${debug.group}" } ?: debug.group

        groups.getOrPut(group) { mutableListOf() }.add(method.name to value)
    }

    for ((group, entries) in groups) {
        telemetry.addLine("[$group]")
        for ((name, value) in entries) {
            telemetry.addData("  $name", value)
        }
    }
}