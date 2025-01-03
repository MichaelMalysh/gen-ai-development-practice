package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.util.StringUtils.parseLocale;

/**
 * A plugin for working with time.
 */
public class TimePlugin {

    /**
     * Get the current date and time for the system default timezone.
     *
     * @return a ZonedDateTime object with the current date and time.
     */
    public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.systemDefault());
    }

    /**
     * Get the current time.
     *
     * <p>Example: {{time.time}} => 9:15:00 AM
     *
     * @return The current time.
     */
    @DefineKernelFunction(
            name = "time",
            description = "Get the current time")
    public String time(
            @KernelFunctionParameter(
                    name = "locale",
                    description = "Locale to use when formatting the date",
                    required = false)
            String locale) {
        return DateTimeFormatter.ofPattern("EEEE HH:mm")
                .withLocale(parseLocale(locale))
                .format(now());
    }

}
