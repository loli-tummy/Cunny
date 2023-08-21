package pictures.cunny.client.framework.modules;

import lombok.Getter;
import lombok.Setter;
import pictures.cunny.client.utility.StringUtils;

public class Category {
    public final String name;
    public final String description;
    @Getter
    @Setter
    private String label;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.label = StringUtils.readable(name);
    }
}
