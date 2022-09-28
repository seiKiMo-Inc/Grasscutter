package io.grasscutter.utils.constants;

import io.grasscutter.account.Account;
import io.grasscutter.utils.interfaces.Serializable;
import java.util.Set;

/* Constants seen in data-scoped classes. */
public interface DataConstants {
    // Classes to be serialized.
    Set<Class<? extends Serializable>> SERIALIZABLE_DATA =
            Set.of(
                    Account.class // Server accounts.
                    );
}
