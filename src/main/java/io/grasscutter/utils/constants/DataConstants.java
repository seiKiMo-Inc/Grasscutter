package io.grasscutter.utils.constants;

import io.grasscutter.player.Account;
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
                    Player.class // Server players.
                    );
}
