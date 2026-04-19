package org.firstinspires.ftc.teamcode.state

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.exceptions.HardwareMissingException

/**
 * A wrapper around [com.qualcomm.robotcore.hardware.HardwareMap] with our custom exceptions
 */
class HardwareManager(val hardwareMap: HardwareMap) {
    /**
     * Attempt to get hardware by ID
     * @throws HardwareMissingException if the hardware is not found
     */
    inline fun <reified T> getHardware(hardwareID: String): Result<T> {
        return try {
            val hardware = hardwareMap.tryGet(T::class.java, hardwareID)
                ?: return Result.failure(HardwareMissingException(hardwareID))
            Result.success(hardware)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}