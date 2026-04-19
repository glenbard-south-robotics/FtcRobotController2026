package org.firstinspires.ftc.teamcode.annotations

import java.lang.reflect.Field
import java.lang.reflect.Method

internal object TelemetryReflectionCache {
    private val fieldCache = mutableMapOf<Class<*>, List<Field>>()

    private val methodCache = mutableMapOf<Class<*>, List<Method>>()

    fun debugFields(clazz: Class<*>): List<Field> = fieldCache.getOrPut(clazz) {
        clazz.declaredFields.filter {
            it.isAnnotationPresent(TelemetryDebug::class.java) && !it.isSynthetic
        }.onEach { it.isAccessible = true }
    }

    fun debugMethods(clazz: Class<*>): List<Method> = methodCache.getOrPut(clazz) {
        clazz.declaredMethods.filter {
            it.isAnnotationPresent(TelemetryDebug::class.java) && it.parameterCount == 0
        }.onEach { it.isAccessible = true }
    }
}