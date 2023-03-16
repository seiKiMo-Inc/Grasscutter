package io.grasscutter.script;

import io.grasscutter.utils.constants.Log;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.jnlua.JavaFunction;
import org.terasology.jnlua.script.LuaScriptEngine;

import javax.script.ScriptEngineManager;

/* Lua script handler. */
public final class ScriptLoader {
    @Getter private static final Logger logger = LoggerFactory.getLogger("Lua");

    private static final ScriptEngineManager engineManager = new ScriptEngineManager();
    @Getter private static final LuaScriptEngine engine =
            (LuaScriptEngine) engineManager.getEngineByName("Lua");

    @Getter private static final ScriptLib scriptHelper = new ScriptLib();

    static {
        // Apply engine functions.
        engine.put("require", (JavaFunction) luaState -> 0);
        engine.put("print", (JavaFunction) luaState -> {
            Log.debug(logger, luaState.checkString(1));
            return 1;
        });

        // Apply engine bindings.
        ScriptBindings.bind(engine, "ScriptLib", scriptHelper);
    }
}
