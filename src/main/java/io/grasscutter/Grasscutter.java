package io.grasscutter;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class Grasscutter {
    @Getter private static final Logger logger =
            LoggerFactory.getLogger(Grasscutter.class);
    @Getter private static final LineReader console =
            Grasscutter.createConsole();

    static {
        // Declare logback configuration.
        System.setProperty("logback.configurationFile", "src/main/resources/logback.xml");
    }

    public static void main(String[] args) {
        logger.info("hello grasscutters");
    }

    /**
     * Creates a {@link LineReader}, or "console" for the application.
     * @return A {@link LineReader} instance.
     *
     * @throws RuntimeException if something impossible happened. (no dumb terminal created)
     */
    @SneakyThrows(IOException.class)
    private static LineReader createConsole() {
        Terminal terminal; try {
            terminal = TerminalBuilder.builder()
                    .jna(true).build();
        } catch (IOException ignored) {
            // Try to get a dumb terminal.
            terminal = TerminalBuilder.builder()
                    .dumb(true).build();
        }

        return LineReaderBuilder.builder()
                .terminal(terminal).build();
    }
}
