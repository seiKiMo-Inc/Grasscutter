package io.grasscutter.utils.definitions.game;

import io.grasscutter.utils.enums.game.FightProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter public final class CompoundProperty {
    private final FightProperty result;
    private final FightProperty base;
    private final FightProperty percent;
    private final FightProperty flat;
}
