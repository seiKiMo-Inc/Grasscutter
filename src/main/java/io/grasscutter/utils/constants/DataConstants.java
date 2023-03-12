package io.grasscutter.utils.constants;

import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.excel.avatar.*;
import io.grasscutter.player.Account;
import io.grasscutter.player.Avatar;
import io.grasscutter.player.Player;
import io.grasscutter.utils.interfaces.Serializable;
import io.grasscutter.utils.objects.Counter;

import java.util.Set;

/* Constants seen in data-scoped classes. */
public interface DataConstants {
    // Classes to be serialized.
    Set<Class<? extends Serializable>> SERIALIZABLE_DATA =
            Set.of(
                    Counter.class, // Server counters.
                    Account.class, // Server accounts.
                    Player.class, // Server players.
                    Avatar.class // Player avatars.
                    );

    // Classes to be loaded.
    Set<Class<? extends GameResource>> GAME_RESOURCES =
            Set.of(
                    AvatarData.class, // Avatar data.
                    AvatarSkillData.class, // Avatar skill data.
                    AvatarTalentData.class, // Avatar talent data.
                    AvatarSkillDepotData.class // Avatar skill depot data.
                    );
}
