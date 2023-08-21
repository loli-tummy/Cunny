package pictures.cunny.client.framework.modules.settings;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import pictures.cunny.client.utility.InventoryUtils;

import java.util.ArrayList;


public class ItemListSetting extends ListSetting<ItemListSetting, Item> {
    private final ArrayList<Item> suggestions;

    public ItemListSetting(ListSetting.Builder<ItemListSetting, Item> builder) {
        super(builder);
        this.suggestions = new ArrayList<>(BuiltInRegistries.ITEM.stream().filter(filter).toList());
    }

    @Override
    public ArrayList<Item> getSuggestions() {
        return suggestions;
    }

    @Override
    public Item parseItem(String v) {
        return BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft", v));
    }

    @Override
    public String itemToString(Item v) {
        return InventoryUtils.getKey(v);
    }

    public static class Builder extends ListSetting.Builder<ItemListSetting, Item> {
        @Override
        public ItemListSetting build() {
            check();
            return new ItemListSetting(this);
        }
    }
}
