package pictures.cunny.client.utility;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathIndex {
    public static final Path CUNNY = Paths.get(System.getProperty("user.dir")).resolve("cunny");
    public static final Path WORLD_SAVE = CUNNY.resolve("world_saving");
    public static final Path COMPATIBILITY = CUNNY.resolve("compatibility");
    public static final Path GELBOORU = CUNNY.resolve("gelbooru");
    public static final Path MAPS = CUNNY.resolve("maps");

    // Files
    public static final Path CONFIG = CUNNY.resolve("config.json");
}
