package io.grasscutter.utils.objects;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import io.grasscutter.Grasscutter;
import java.util.Arrays;

/**
 * A logback appender that uses JLine to print above the console.
 *
 * @see <a href="https://bit.ly/3S38YeL">Grasscutter/Grasscutters</a>
 */
public final class JLineLogbackAppender extends ConsoleAppender<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!started) return;

        Arrays.stream(new String(encoder.encode(eventObject)).split(System.lineSeparator()))
                .forEach(Grasscutter.getConsole()::printAbove);
    }
}
