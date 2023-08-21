package pictures.cunny.client.impl.events.mods;

import fi.dy.masa.litematica.schematic.projects.SchematicProject;
import pictures.cunny.client.framework.events.Event;

public class CloseSchematicEvent extends Event {
    public static CloseSchematicEvent INSTANCE = new CloseSchematicEvent();
    public SchematicProject project;

    public CloseSchematicEvent() {
        super(true);
    }


    public static CloseSchematicEvent get(SchematicProject project) {
        INSTANCE.project = project;
        return INSTANCE;
    }
}
