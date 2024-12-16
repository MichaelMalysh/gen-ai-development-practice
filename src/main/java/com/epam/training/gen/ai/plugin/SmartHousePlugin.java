package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

/**
 * A plugin for controlling smart house devices.
 */
@Slf4j
public class SmartHousePlugin {

    /**
     * Turn on a device.
     *
     * <p>Example: {{SmartHouse.turnOn deviceName="light"}} => Turned on light
     *
     * @param deviceName Name of the device to turn on.
     * @return A message indicating that the device was turned on.
     */
    @DefineKernelFunction(
            name = "turnOn",
            description = "Turn on a device")
    public String turnOn(@KernelFunctionParameter(
            name = "deviceName",
            description = "Name of the device to turn on") String deviceName) {
        log.info("Turned on " + deviceName);
        return "Turned on " + deviceName;
    }

    /**
     * Turn off a device.
     *
     * <p>Example: {{SmartHouse.turnOff deviceName="light"}} => Turned off light
     *
     * @param deviceName Name of the device to turn off.
     * @return A message indicating that the device was turned off.
     */
    @DefineKernelFunction(
            name = "turnOff",
            description = "Turn off a device")
    public String turnOff(@KernelFunctionParameter(
            name = "deviceName",
            description = "Name of the device to turn off") String deviceName) {
        log.info("Turned off " + deviceName);
        return "Turned off " + deviceName;
    }
}
