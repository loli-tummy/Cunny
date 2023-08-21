package pictures.cunny.client.framework.modules.settings;

import pictures.cunny.client.utility.blocks.BlockUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;


public class BlockListSetting extends ListSetting<BlockListSetting, Block> {
    private final ArrayList<Block> suggestions;

    public BlockListSetting(ListSetting.Builder<BlockListSetting, Block> builder) {
        super(builder);
        this.suggestions = new ArrayList<>(BuiltInRegistries.BLOCK.stream().filter(filter).toList());
    }

    @Override
    public ArrayList<Block> getSuggestions() {
        return suggestions;
    }

    @Override
    public Block parseItem(String v) {
        return BuiltInRegistries.BLOCK.get(new ResourceLocation("minecraft", v));
    }

    @Override
    public String itemToString(Block v) {
        return v == null ? "air" : BlockUtils.getKey(v);
    }

    public static class Builder extends ListSetting.Builder<BlockListSetting, Block> {
        @Override
        public BlockListSetting build() {
            check();
            return new BlockListSetting(this);
        }
    }
}
