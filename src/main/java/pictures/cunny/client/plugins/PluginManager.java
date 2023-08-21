package pictures.cunny.client.plugins;

import pictures.cunny.client.Cunny;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginManager {
    public Map<String, JavaPlugin> plugins = new HashMap<>();
    public Map<JavaPlugin, List<Object>> events = new HashMap<>();

    public void findPlugins() {
        for (EntrypointContainer<?> entrypointContainer
                : FabricLoader.getInstance().getEntrypointContainers("cunny", JavaPlugin.class)) {
            JavaPlugin addon = (JavaPlugin) entrypointContainer.getEntrypoint();
            ModMetadata metadata = entrypointContainer.getProvider().getMetadata();
            plugins.put(metadata.getId(), addon);
        }
    }

    public void addEventListener(JavaPlugin addon, Object o) {
        Cunny.EVENT_HANDLER.add(o);
        events.putIfAbsent(addon, new ArrayList<>());
        events.get(addon).add(o);
    }

    public void disableEvents(JavaPlugin addon) {
        for (Object o : events.get(addon))
            Cunny.EVENT_HANDLER.disable(o);
    }

    public void enableEvents(JavaPlugin addon) {
        for (Object o : events.get(addon))
            Cunny.EVENT_HANDLER.enable(o);
    }

    public void onInitialize() {
        long ns = System.nanoTime();

        EntrypointUtils.invoke("cunny", JavaPlugin.class, JavaPlugin::onInit);

        Cunny.LOG.info("Took {} nanoseconds for Cunny to run initialization methods.", System.nanoTime() - ns);
    }
}
