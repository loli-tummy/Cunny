package pictures.cunny.client.config.modules;

import java.util.HashMap;
import java.util.Map;

public class ModuleConfig {
    public boolean active = false;
    public int key = -1;
    public Map<String, SettingGroupConfig> groups = new HashMap<>();
}
