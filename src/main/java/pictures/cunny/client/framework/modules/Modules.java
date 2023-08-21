package pictures.cunny.client.framework.modules;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;
import pictures.cunny.client.Cunny;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Modules {
    protected final Map<Category, List<Module>> MODULES = new Object2ObjectArrayMap<>();
    protected final Map<String, Module> NAME_TO_MODULE = new Object2ObjectArrayMap<>();
    protected final Map<Class<? extends Module>, Module> CLASS_TO_MODULE = new Object2ObjectArrayMap<>();

    public Modules() {
        for (Category category : Categories.INDEX) {
            MODULES.putIfAbsent(category, new ObjectArrayList<>());
        }
    }

    public List<Module> getModules(Category category) {
        return MODULES.get(category);
    }

    public void add(Module... modules) {
        if (modules == null) {
            Cunny.error("NO ! DUCKS FLIGHTLESS !");
            return;
        }

        for (Module module : modules) {
            if (module == null) {
                Cunny.error("DUCKS CANNOT FLY ! ! !");
                continue;
            }

            List<Module> moduleSet = MODULES.get(module.category);

            if (moduleSet.contains(module) || NAME_TO_MODULE.containsKey(module.name)) {
                Cunny.warn("Module {} has a duplicate.", module.name);
                continue;
            }

            moduleSet.add(module);
            NAME_TO_MODULE.put(module.name, module);
            CLASS_TO_MODULE.put(module.getClass(), module);

            Cunny.log("Module {} was successfully loaded.", module.name);

            module.active(false);
        }
    }

    public boolean exists(Class<? extends Module> clazz) {
        return CLASS_TO_MODULE.containsKey(clazz);
    }

    public boolean exists(String name) {
        return NAME_TO_MODULE.containsKey(name);
    }

    public List<Module> getCategory(Category category) {
        return MODULES.get(category);
    }

    @Nullable
    public Module get(Class<? extends Module> clazz) {
        return CLASS_TO_MODULE.get(clazz);
    }

    @Nullable
    public Module get(String name) {
        return NAME_TO_MODULE.get(name);
    }

    public boolean isActive(Class<? extends Module> clazz) {
        return exists(clazz) && get(clazz).active();
    }
}
