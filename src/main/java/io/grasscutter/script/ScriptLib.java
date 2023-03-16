package io.grasscutter.script;

import io.netty.util.concurrent.FastThreadLocal;

/* Java helper for Lua scripts. */
public final class ScriptLib {
    private final FastThreadLocal<Object> scriptManager
            = new FastThreadLocal<>();
    private final FastThreadLocal<Object> groupExecutor
            = new FastThreadLocal<>();
}
