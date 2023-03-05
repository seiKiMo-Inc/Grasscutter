package io.grasscutter.commands.defaults;

import io.grasscutter.commands.Command;
import io.grasscutter.commands.CommandData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class TestCommand extends Command {
    public TestCommand() {
        super(CommandData.builder()
                .label("test")
                .description("A test command.")
                .usage("test")
                .build());
    }

    @Override
    public void execute(@Nullable Object sender, List<String> arguments) {
        System.out.println("Test command executed!");
    }
}
