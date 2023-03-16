package io.grasscutter.script;

import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.objects.lang.TextContainer;
import org.terasology.jnlua.LuaState;
import org.terasology.jnlua.LuaState53;
import org.terasology.jnlua.NamedJavaFunction;
import org.terasology.jnlua.script.LuaBindings;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/* Binds global variables of a Lua script engine. */
public final class ScriptBindings {
    /**
     * Binds a global variable to a Lua script engine.
     *
     * @param engine The script engine to apply.
     * @param bind The name of the variable.
     * @param object The object to reference.
     */
    @SuppressWarnings("unchecked")
    public static void bind(ScriptEngine engine, String bind, Object object) {
        // Create bindings for the engine.
        var methodBindings = new ArrayList<NamedJavaFunction>();
        var bindings = engine.createBindings();
        if (!(bindings instanceof LuaBindings luaBindings))
            return;

        // Fetch the Lua state.
        var luaState = (LuaState53) luaBindings.getLuaState();

        if (object instanceof Map) {
            var fields = (Map<String, Integer>) object;
            for (var type : fields.entrySet()) {
                luaState.pushJavaObject(type.getValue());
                luaState.setField(-2, type.getKey());
            }
        } else {
            // Bind methods in the class to the Lua state.
            var objClass = object.getClass();
            var methods = objClass.getMethods();
            Arrays.stream(methods).forEach(method -> {
                class Func implements NamedJavaFunction {
                    @Override public String getName() {
                        return method.getName();
                    }

                    @Override public int invoke(LuaState luaState) {
                        var size = luaState.getTop();
                        var args = new ArrayList<>(size);

                        // Add the arguments from the state.
                        for (var i = 0; i < size; i++)
                            args.add(luaState.checkJavaObject(i + 1, Object.class));

                        try {
                            var result = method.invoke(object, args.toArray());
                        } catch (Exception exception) {
                            // Log the exception.
                            Log.error(ScriptLoader.getLogger(),
                                    new TextContainer("exception.lua"), exception);
                        }

                        return 1;
                    }
                }

                methodBindings.add(new Func());
            });

            // Bind fields in the class to the Lua state.
            luaState.newTable();
            var fields = objClass.getFields();
            Arrays.stream(fields).forEach(field -> {
                try {
                    luaState.pushJavaObject(field.get(object));
                    luaState.setField(-2, field.getName());
                } catch (Exception exception) {
                    // Log the exception.
                    Log.error(ScriptLoader.getLogger(),
                            new TextContainer("exception.lua"), exception);
                }
            });
        }

        // Add functions to the Lua state.
        for (var function : methodBindings) {
            var name = function.getName();
            luaState.pushJavaFunction(function);
            luaState.setField(-2, name);
        }

        // Add extra data to the Lua state.
        luaState.luaGetsubtable(luaState.REGISTRYINDEX, "_LOADED");
        luaState.pushValue(-2);
        luaState.setField(-2, bind);
        luaState.pop(1);
        luaState.rawGet(luaState.REGISTRYINDEX, 2);
        luaState.pushValue(-2);
        luaState.setField(-2, bind);
        luaState.pop(1);
    }
}
