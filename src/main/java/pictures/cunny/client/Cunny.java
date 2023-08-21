package pictures.cunny.client;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pictures.cunny.client.config.Config;
import pictures.cunny.client.framework.events.EventBus;
import pictures.cunny.client.framework.gui.Gui;
import pictures.cunny.client.framework.gui.utility.ColoredText;
import pictures.cunny.client.framework.gui.views.client.ModuleConfigView;
import pictures.cunny.client.framework.gui.views.client.ModulesView;
import pictures.cunny.client.framework.gui.views.features.BookEditorView;
import pictures.cunny.client.framework.gui.views.features.ClientLogView;
import pictures.cunny.client.framework.modules.Categories;
import pictures.cunny.client.framework.modules.Category;
import pictures.cunny.client.framework.modules.Module;
import pictures.cunny.client.framework.modules.Modules;
import pictures.cunny.client.impl.modules.exploits.FakeDDOS;
import pictures.cunny.client.impl.modules.exploits.InstantClose;
import pictures.cunny.client.impl.modules.exploits.SilentClose;
import pictures.cunny.client.impl.modules.misc.BrandSpoof;
import pictures.cunny.client.impl.modules.misc.DetachMouse;
import pictures.cunny.client.impl.modules.misc.Rotation;
import pictures.cunny.client.impl.modules.misc.TestModule;
import pictures.cunny.client.impl.modules.movement.DeSyncInputs;
import pictures.cunny.client.impl.modules.movement.Sneak;
import pictures.cunny.client.impl.modules.world.AutoPlace;
import pictures.cunny.client.impl.modules.world.SignFucker;
import pictures.cunny.client.plugins.PluginManager;
import pictures.cunny.client.utility.LatencyUtils;
import pictures.cunny.client.utility.StringUtils;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cunny implements ModInitializer {
    public static final RandomSource RANDOM = RandomSource.createThreadSafe();
    public static final Logger LOG = LoggerFactory.getLogger("Cunny");
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1);
    public static final PluginManager ADDON_MANAGER = new PluginManager();
    public static final EventBus EVENT_HANDLER = new EventBus();
    public static final Modules MODULES = new Modules();
    public static Minecraft mc;
    public static Cunny INSTANCE;
    public static Gui GUI;
    public static Config CONFIG;

    public static void postInit() {
        GUI = new Gui();
        GUI.launch();
    }

    public static void log(Color color, String str, Object... args) {
        if (str.contains("{}")) {
            for (Object arg : args) {
                str = str.replaceFirst("\\{}", arg.toString());
            }
        }

        if (ClientLogView.LOGS.size64() >= 5000)
            ClientLogView.LOGS.remove(0);

        ClientLogView.LOGS.add(new ColoredText(str, color));

        LOG.info(str);
    }

    public static void log(String str, Object... args) {
        log(Color.WHITE, str, args);
    }

    public static void warn(String str, Object... args) {
        log(Color.ORANGE, str, args);
    }

    public static void error(String str, Object... args) {
        log(Color.RED, str, args);
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;

        new StringUtils();

        mc = Minecraft.getInstance();

        CONFIG = new Config();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (CONFIG != null) {
                CONFIG.save();
            }
        }));

        CONFIG.load();

        new ModulesView().load();
        new ModuleConfigView().load();
        new ClientLogView().load();
        new BookEditorView().load();

        new TestModule().load();

        new FakeDDOS().load();
        new SilentClose().load();
        new BrandSpoof().load();
        new InstantClose().load();

        new DeSyncInputs().load();
        new Sneak().load();

        new DetachMouse().load();
        new Rotation().load();

        new SignFucker().load();

        // Litematica modules
        if (FabricLoader.getInstance().isModLoaded("litematica")) {
            new AutoPlace().load();
        }

        EVENT_HANDLER.add(new LatencyUtils());

        // Gets the list of add-ons.
        ADDON_MANAGER.findPlugins();

        // Calls preload methods.
        ADDON_MANAGER.onInitialize();

        // Populate modules.
        for (Category category : Categories.INDEX) {
            for (Module module : MODULES.getModules(category)) {
                Config.get().populateModule(module);
            }
        }
    }
}
