package io.grasscutter.script;

import io.grasscutter.world.Scene;
import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/* Manages scripts in a scene. */
@RequiredArgsConstructor
public final class SceneScriptManager {
    /* Script event executor. */
    private static final ExecutorService executor
            = new ThreadPoolExecutor(4, 4, 60,
            TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000),
            FastThreadLocalThread::new, new ThreadPoolExecutor.AbortPolicy());

    private final Scene scene;

    private final Map<String, Integer> variables
            = new ConcurrentHashMap<>();

    /*
     * Scene data.
     */
    private final Map<Integer, Set<Object>> triggers
            = new ConcurrentHashMap<>();
    private final Map<Integer, Object> regions
            = new ConcurrentHashMap<>();
    private final Map<Integer, Object> groups
            = new ConcurrentHashMap<>();
}
