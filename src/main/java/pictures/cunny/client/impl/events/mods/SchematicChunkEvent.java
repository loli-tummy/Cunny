package pictures.cunny.client.impl.events.mods;

import fi.dy.masa.litematica.schematic.projects.SchematicProject;
import fi.dy.masa.litematica.world.ChunkSchematic;
import net.minecraft.world.level.ChunkPos;
import pictures.cunny.client.framework.events.Event;

public class SchematicChunkEvent extends Event {
    public static SchematicChunkEvent INSTANCE = new SchematicChunkEvent();
    public ChunkSchematic chunk;

    public SchematicChunkEvent() {
        super(false);
    }


    public static SchematicChunkEvent get(ChunkSchematic chunk) {
        INSTANCE.chunk = chunk;
        return INSTANCE;
    }
}

