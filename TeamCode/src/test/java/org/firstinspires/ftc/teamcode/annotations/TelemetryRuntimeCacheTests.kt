package org.firstinspires.ftc.teamcode.annotations

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TelemetryReflectionCacheTests {
    private class NoAnnotations {
        val unannotatedField: String = "hello"
        fun unannotatedMethod(): String = "hello"
    }

    private class WithAnnotations {
        @TelemetryDebug(group = "Test")
        val annotatedField: String = "field value"

        val unannotatedField: String = "ignored"

        @TelemetryDebug(group = "Test")
        fun annotatedMethod(): String = "method value"

        fun unannotatedMethod(): String = "ignored"
    }

    private class WithMethodVariants {
        @TelemetryDebug
        fun noParams(): String = "ok"

        @TelemetryDebug
        fun withParams(x: Int): String = "bad"  // should be excluded, has parameters
    }

    private class WithDefaultGroup {
        @TelemetryDebug
        val field: String = "value"

        @TelemetryDebug
        fun method(): Int = 42
    }

    private class WithMultipleGroups {
        @TelemetryDebug(group = "Motors")
        val motorPower: Double = 0.5

        @TelemetryDebug(group = "Sensors")
        val sensorValue: Int = 100

        @TelemetryDebug(group = "Motors")
        fun motorTemp(): Double = 37.0
    }

    @Before
    fun clearCache() {
        // Clear caches between tests to avoid contamination
        val fieldCache = TelemetryReflectionCache::class.java
            .getDeclaredField("fieldCache")
            .apply { isAccessible = true }
            .get(TelemetryReflectionCache) as MutableMap<*, *>

        val methodCache = TelemetryReflectionCache::class.java
            .getDeclaredField("methodCache")
            .apply { isAccessible = true }
            .get(TelemetryReflectionCache) as MutableMap<*, *>

        fieldCache.clear()
        methodCache.clear()
    }

    @Test
    fun `debugFields returns empty list when no fields are annotated`() {
        assertTrue(TelemetryReflectionCache.debugFields(NoAnnotations::class.java).isEmpty())
    }

    @Test
    fun `debugFields returns only annotated fields`() {
        val fields = TelemetryReflectionCache.debugFields(WithAnnotations::class.java)
        assertEquals(1, fields.size)
        assertEquals("annotatedField", fields.first().name)
    }

    @Test
    fun `debugFields does not return unannotated fields`() {
        val fields = TelemetryReflectionCache.debugFields(WithAnnotations::class.java)
        assertFalse(fields.any { it.name == "unannotatedField" })
    }

    @Test
    fun `debugFields makes fields accessible`() {
        val fields = TelemetryReflectionCache.debugFields(WithAnnotations::class.java)
        assertTrue(fields.all { it.isAccessible })
    }

    @Test
    fun `debugFields returns field with correct annotation group`() {
        val fields = TelemetryReflectionCache.debugFields(WithAnnotations::class.java)
        val annotation = fields.first().getAnnotation(TelemetryDebug::class.java)
        assertEquals("Test", annotation.group)
    }

    @Test
    fun `debugFields uses default group when none specified`() {
        val fields = TelemetryReflectionCache.debugFields(WithDefaultGroup::class.java)
        val annotation = fields.first().getAnnotation(TelemetryDebug::class.java)
        assertEquals("default", annotation.group)
    }

    @Test
    fun `debugFields returns same list instance on second call (caching)`() {
        val first = TelemetryReflectionCache.debugFields(WithAnnotations::class.java)
        val second = TelemetryReflectionCache.debugFields(WithAnnotations::class.java)
        assertSame(first, second)
    }

    @Test
    fun `debugFields returns fields across multiple groups`() {
        val fields = TelemetryReflectionCache.debugFields(WithMultipleGroups::class.java)
        assertEquals(2, fields.size)
    }

    @Test
    fun `debugMethods returns empty list when no methods are annotated`() {
        assertTrue(TelemetryReflectionCache.debugMethods(NoAnnotations::class.java).isEmpty())
    }

    @Test
    fun `debugMethods returns only annotated methods`() {
        val methods = TelemetryReflectionCache.debugMethods(WithAnnotations::class.java)
        assertEquals(1, methods.size)
        assertEquals("annotatedMethod", methods.first().name)
    }

    @Test
    fun `debugMethods does not return unannotated methods`() {
        val methods = TelemetryReflectionCache.debugMethods(WithAnnotations::class.java)
        assertFalse(methods.any { it.name == "unannotatedMethod" })
    }

    @Test
    fun `debugMethods excludes annotated methods with parameters`() {
        val methods = TelemetryReflectionCache.debugMethods(WithMethodVariants::class.java)
        assertFalse(methods.any { it.name == "withParams" })
    }

    @Test
    fun `debugMethods includes annotated methods with no parameters`() {
        val methods = TelemetryReflectionCache.debugMethods(WithMethodVariants::class.java)
        assertTrue(methods.any { it.name == "noParams" })
    }

    @Test
    fun `debugMethods makes methods accessible`() {
        val methods = TelemetryReflectionCache.debugMethods(WithAnnotations::class.java)
        assertTrue(methods.all { it.isAccessible })
    }

    @Test
    fun `debugMethods returns method with correct annotation group`() {
        val methods = TelemetryReflectionCache.debugMethods(WithAnnotations::class.java)
        val annotation = methods.first().getAnnotation(TelemetryDebug::class.java)
        assertEquals("Test", annotation.group)
    }

    @Test
    fun `debugMethods uses default group when none specified`() {
        val methods = TelemetryReflectionCache.debugMethods(WithDefaultGroup::class.java)
        val annotation = methods.first().getAnnotation(TelemetryDebug::class.java)
        assertEquals("default", annotation.group)
    }

    @Test
    fun `debugMethods returns same list instance on second call (caching)`() {
        val first = TelemetryReflectionCache.debugMethods(WithAnnotations::class.java)
        val second = TelemetryReflectionCache.debugMethods(WithAnnotations::class.java)
        assertSame(first, second)
    }

    @Test
    fun `debugMethods returns methods across multiple groups`() {
        val methods = TelemetryReflectionCache.debugMethods(WithMultipleGroups::class.java)
        assertEquals(1, methods.size)
        assertEquals("motorTemp", methods.first().name)
    }

    @Test
    fun `debugFields can read field value via reflection`() {
        val instance = WithAnnotations()
        val field = TelemetryReflectionCache.debugFields(WithAnnotations::class.java).first()
        assertEquals("field value", field.get(instance))
    }

    @Test
    fun `debugMethods can invoke method via reflection`() {
        val instance = WithAnnotations()
        val method = TelemetryReflectionCache.debugMethods(WithAnnotations::class.java).first()
        assertEquals("method value", method.invoke(instance))
    }
}