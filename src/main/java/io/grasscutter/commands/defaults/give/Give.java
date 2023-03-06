package io.grasscutter.commands.defaults.give;

import io.grasscutter.commands.CommandData;
import io.grasscutter.commands.SubCommand;
import io.grasscutter.commands.defaults.GiveCommand;
import java.util.List;

/* Common functions for item giving. */
public abstract class Give extends SubCommand {
    public Give(String label, String description) {
        super(
                CommandData.builder()
                        .ordered(false)
                        .async(true)
                        .label(label)
                        .description(description)
                        .permission("grasscutter.give." + label)
                        .arguments(List.of(GiveCommand.TARGET, GiveCommand.LEVEL))
                        .build());
    }
}
