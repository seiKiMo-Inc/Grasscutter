package io.grasscutter.utils.objects.text;

import io.grasscutter.utils.objects.lang.TextContainer;
import java.awt.*;
import lombok.Getter;
import lombok.Setter;

/* An instance of text. */
public final class Text {
    /**
     * Creates a new rich instance of text.
     *
     * @param text The text to use.
     * @return The new instance of text.
     */
    public static Text of(String text) {
        return new Text(text, false);
    }

    @Getter private final boolean raw;
    private final Style.StyleBuilder style = Style.builder();

    @Setter private String text;
    @Setter private TextContainer container;

    /**
     * Creates a new rich instance of text.
     *
     * @param text The text to use.
     */
    public Text(String text) {
        this.raw = false;
        this.text = text;
    }

    /**
     * Creates a new instance of text.
     *
     * @param text The text to use.
     * @param raw Whether the text is raw.
     */
    public Text(String text, boolean raw) {
        this.raw = raw;
        this.text = text;
    }

    /**
     * Creates a new rich instance of text.
     *
     * @param text The text to use.
     */
    public Text(TextContainer text) {
        this.raw = false;
        this.container = text;
    }

    /**
     * Creates a new instance of text.
     *
     * @param text The text to use.
     */
    public Text(TextContainer text, boolean raw) {
        this.raw = raw;
        this.container = text;
    }

    /**
     * Sets the size of the text.
     *
     * @param size The size of the text.
     * @return This object.
     */
    public Text size(int size) {
        this.style.size(size);
        return this;
    }

    /**
     * Sets the color of the text.
     *
     * @param color The color of the text.
     * @return This object.
     */
    public Text color(Color color) {
        this.style.color(color);
        return this;
    }

    /**
     * Sets the boldness of the text.
     *
     * @param bold Whether the text is bold.
     * @return This object.
     */
    public Text bold(boolean bold) {
        this.style.bold(bold);
        return this;
    }

    /**
     * Sets the italicness of the text.
     *
     * @param italic Whether the text is italic.
     * @return This object.
     */
    public Text italic(boolean italic) {
        this.style.italic(italic);
        return this;
    }

    /**
     * Converts this object to a string. Converts to a console or Unity format.
     *
     * @param console Whether to convert the text for the console.
     * @return The converted string.
     */
    public String toString(boolean console) {
        // Pull instances of style and text.
        var style = this.style.build();
        var text = this.container != null ? this.container.toString() : this.text;

        return console ? style.toTerminal(text) : style.toUnity(text);
    }
}
