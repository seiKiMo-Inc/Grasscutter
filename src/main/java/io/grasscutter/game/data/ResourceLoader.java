package io.grasscutter.game.data;

import io.grasscutter.utils.FileUtils;
import io.grasscutter.utils.Preconditions;
import io.grasscutter.utils.constants.DataConstants;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.encodings.Json;
import io.grasscutter.utils.encodings.TabSeparatedJson;
import io.grasscutter.utils.enums.Priority;
import io.grasscutter.utils.objects.lang.TextContainer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.*;

/* Loads game resources. */
@SuppressWarnings("unchecked")
public final class ResourceLoader {
    /**
     * Loads all resources.
     */
    public static void loadResources() {
        var startTime = System.nanoTime();

        // Get the resources to load.
        var loadingExceptions = new ArrayList<Throwable>();
        for (var resource : ResourceLoader.sortResources()) {
            resource.stream().parallel().unordered().forEach(toLoad -> {
                try {
                    var data = toLoad.getAnnotation(Resource.class); // Get the resource data.
                    var map = GameData.getTargetMap(toLoad); // Get the output map.

                    // Validate the data.
                    Preconditions.notNull(data);
                    Preconditions.notNull(map);

                    // Load the resource.
                    ResourceLoader.loadResource(toLoad, data, map);
                } catch (Exception exception) {
                    loadingExceptions.add(exception);
                }
            });
        }

        // Print the loading exceptions.
        if (!loadingExceptions.isEmpty()) {
            loadingExceptions.forEach(exception ->
                    Log.debug(new TextContainer("exception.error"), exception));
        }

        var endTime = System.nanoTime();
        var duration = (endTime - startTime) / 1000000;
        Log.info(new TextContainer("server.dedicated.resources.done", duration));
    }

    /**
     * Sorts the list of resources from highest to lowest priority.
     * @return A sorted set of resources.
     */
    private static List<Set<Class<? extends GameResource>>> sortResources() {
        var resources = DataConstants.GAME_RESOURCES;
        // Sort the resources into a list of sets.
        var sortedResources = new LinkedHashMap<Priority, Set<Class<? extends GameResource>>>();
        Priority.inOrder().forEach(priority -> sortedResources.put(priority, new LinkedHashSet<>()));

        for (var resource : resources) {
            // Add the resource to the correct priority set.
            var priority = resource.getAnnotation(Resource.class).priority();
            sortedResources.get(priority).add(resource);
        }

        return List.copyOf(sortedResources.values());
    }

    /**
     * Loads a game resource.
     *
     * @param resource The resource class.
     * @param data The resource data.
     * @param map The map to add the resource content to.
     */
    private static void loadResource(
            Class<? extends GameResource> resource,
            Resource data, Int2ObjectMap map
    ) throws Exception {
        for (var name : data.name()) {
            // Load the resource's data.
            var path = GameResource.getExcelPath(name);
            var results = switch (FileUtils.extension(path)) {
                case "json" -> Json.toList(path, resource);
                case "tsj" -> TabSeparatedJson.toList(path, resource);
                default -> null;
            };
            if (results == null) return;

            // Add the resource to the map.
            results.forEach(result -> {
                result.onLoad();
                map.put(result.getId(), result);
            });
        }
    }
}
