package io.grasscutter.utils.constants;

import io.grasscutter.Grasscutter;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.grasscutter.utils.objects.text.Text;
import org.slf4j.Logger;

/* A language-compliant logging utility. */
public interface Log {
    /* Reference to the root logger. */
    Logger LOGGER = Grasscutter.getLogger();

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void info(String text) {
        LOGGER.info(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void info(String text, Throwable throwable) {
        LOGGER.info(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     */
    static void info(TextContainer container) {
        LOGGER.info(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void info(TextContainer container, Throwable throwable) {
        LOGGER.info(container.toString(), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void info(Text text) {
        LOGGER.info(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void info(Text text, Throwable throwable) {
        LOGGER.info(text.toString(true), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void warn(String text) {
        LOGGER.warn(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void warn(String text, Throwable throwable) {
        LOGGER.warn(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     */
    static void warn(TextContainer container) {
        LOGGER.warn(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void warn(TextContainer container, Throwable throwable) {
        LOGGER.warn(container.toString(), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void warn(Text text) {
        LOGGER.warn(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void warn(Text text, Throwable throwable) {
        LOGGER.warn(text.toString(true), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void error(String text) {
        LOGGER.error(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void error(String text, Throwable throwable) {
        LOGGER.error(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     */
    static void error(TextContainer container) {
        LOGGER.error(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void error(TextContainer container, Throwable throwable) {
        if (Properties.DEBUG) LOGGER.error(container.toString(), throwable);
        else LOGGER.error(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void error(Text text) {
        LOGGER.error(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void error(Text text, Throwable throwable) {
        LOGGER.error(text.toString(true), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void debug(String text) {
        LOGGER.debug(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void debug(String text, Throwable throwable) {
        LOGGER.debug(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     */
    static void debug(TextContainer container) {
        LOGGER.debug(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void debug(TextContainer container, Throwable throwable) {
        LOGGER.debug(container.toString(), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     */
    static void debug(Text text) {
        LOGGER.debug(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void debug(Text text, Throwable throwable) {
        LOGGER.debug(text.toString(true), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void info(Logger logger, String text) {
        logger.info(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void info(Logger logger, String text, Throwable throwable) {
        logger.info(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     */
    static void info(Logger logger, TextContainer container) {
        logger.info(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void info(Logger logger, TextContainer container, Throwable throwable) {
        logger.info(container.toString(), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void info(Logger logger, Text text) {
        logger.info(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void info(Logger logger, Text text, Throwable throwable) {
        logger.info(text.toString(true), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void warn(Logger logger, String text) {
        logger.warn(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void warn(Logger logger, String text, Throwable throwable) {
        logger.warn(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     */
    static void warn(Logger logger, TextContainer container) {
        logger.warn(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void warn(Logger logger, TextContainer container, Throwable throwable) {
        logger.warn(container.toString(), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void warn(Logger logger, Text text) {
        logger.warn(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void warn(Logger logger, Text text, Throwable throwable) {
        logger.warn(text.toString(true), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void error(Logger logger, String text) {
        logger.error(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void error(Logger logger, String text, Throwable throwable) {
        logger.error(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     */
    static void error(Logger logger, TextContainer container) {
        logger.error(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void error(Logger logger, TextContainer container, Throwable throwable) {
        if (Properties.DEBUG) logger.error(container.toString(), throwable);
        else logger.error(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void error(Logger logger, Text text) {
        logger.error(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void error(Logger logger, Text text, Throwable throwable) {
        logger.error(text.toString(true), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void debug(Logger logger, String text) {
        logger.debug(text);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void debug(Logger logger, String text, Throwable throwable) {
        logger.debug(text, throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     */
    static void debug(Logger logger, TextContainer container) {
        logger.debug(container.toString());
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param container Translatable text container.
     * @param throwable Throwable to log.
     */
    static void debug(Logger logger, TextContainer container, Throwable throwable) {
        logger.debug(container.toString(), throwable);
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     */
    static void debug(Logger logger, Text text) {
        logger.debug(text.toString(true));
    }

    /**
     * Logs a message to the console.
     *
     * @param logger The logger to use.
     * @param text The text to log.
     * @param throwable The throwable to log.
     */
    static void debug(Logger logger, Text text, Throwable throwable) {
        logger.debug(text.toString(true), throwable);
    }
}
