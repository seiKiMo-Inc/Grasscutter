package io.grasscutter.utils.objects.lang;

import io.grasscutter.Grasscutter;

/* Contains a string of (translatable) text. */
public final class TextContainer {
    private final Language targetLanguage;
    private final String key;
    private final Object[] arguments;

    private String result = "";

    /**
     * Initializes a text container. For text without arguments to substitute.
     *
     * @param key Key to load. (e.g. "system.startup")
     */
    public TextContainer(String key) {
        this.targetLanguage = Grasscutter.getServerLanguage();
        this.key = key;
        this.arguments = new Object[0];
    }

    /**
     * Initializes a text container. For text with arguments to substitute.
     *
     * @param key Key to load. (e.g. "system.startup")
     * @param targetLanguage The target language to translate to.
     */
    public TextContainer(String key, Language targetLanguage) {
        this.targetLanguage = targetLanguage;
        this.key = key;
        this.arguments = new Object[0];
    }

    /**
     * Initializes a text container. Substitution occurs with the provided arguments.
     *
     * @param key Key to load. (e.g. "system.startup")
     * @param arguments Arguments to substitute.
     */
    public TextContainer(String key, Object... arguments) {
        this.targetLanguage = Grasscutter.getServerLanguage();
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * Initializes a text container. Substitution occurs with the provided arguments.
     *
     * @param key Key to load. (e.g. "system.startup")
     * @param targetLanguage The target language to translate to.
     * @param arguments Arguments to substitute.
     */
    public TextContainer(String key, Language targetLanguage, Object... arguments) {
        this.targetLanguage = targetLanguage;
        this.key = key;
        this.arguments = arguments;
    }

    /**
     * Substitutes an argument into the text.
     *
     * @param argument The argument to substitute.
     */
    private void substituteArgument(int argument) {
        try {
            this.result = this.result.replace("{" + argument + "}", this.arguments[argument].toString());
        } catch (ArrayIndexOutOfBoundsException ignored) {
            Grasscutter.getLogger()
                    .warn("Unable to substitute argument " + argument + " in text container " + this.key);
        }
    }

    @Override
    public String toString() {
        // Get the string of the key in the target language.
        this.result = this.targetLanguage.translate(this.key);
        // Substitute all arguments.
        for (int i = 0; i < this.arguments.length; i++) {
            this.substituteArgument(i);
        }

        return this.result;
    }
}
