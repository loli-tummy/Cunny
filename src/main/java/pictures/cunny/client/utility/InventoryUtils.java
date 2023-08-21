package pictures.cunny.client.utility;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.util.Arrays;
import java.util.function.Predicate;

import static pictures.cunny.client.Cunny.mc;

/**
 * The type Inv utils.
 */
public class InventoryUtils {
    public static final Predicate<ItemStack> IS_BLOCK = (itemStack) -> BuiltInRegistries.BLOCK.containsKey(new ResourceLocation("minecraft:", getKey(itemStack.getItem())));

    public static String getKey(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    /**
     * Is wool boolean.
     *
     * @param item the item
     * @return the boolean
     */
    // Bed Utility
    public static boolean isWool(Item item) {
        return getKey(item).endsWith("_wool");
    }

    /**
     * Is plank boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public static boolean isPlank(Item item) {
        return getKey(item).endsWith("_planks");
    }

    /**
     * Is bed boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public static boolean isBed(Item item) {
        return getKey(item).endsWith("_bed");
    }

    /**
     * Is sword boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public static boolean isSword(Item item) {
        return getKey(item).endsWith("_sword");
    }

    /**
     * Sync hand.
     *
     * @param i the
     */
    public static void syncHand(int i) {
        assert mc.player != null;
        PacketUtils.send(new ServerboundSetCarriedItemPacket(i));
    }

    /**
     * Sync hand.
     */
    public static void syncHand() {
        assert mc.player != null;
        syncHand(mc.player.getInventory().selected);
    }

    /**
     * Swap slot.
     *
     * @param i      the
     * @param silent the silent
     */
    public static void swapSlot(int i, boolean silent) {
        assert mc.player != null;
        if (mc.player.getInventory().selected != i) {
            if (!silent) mc.player.getInventory().selected = i;
            PacketUtils.send(new ServerboundSetCarriedItemPacket(i));
        }
    }


    public static int moveItemToHotbar(int slot, boolean preferEmpty) {
        if (slot != -1) {
            if (slot < 35) {
                assert mc.player != null;
                int prevSlot = mc.player.getInventory().selected;
                int emptySlot = mc.player.getInventory().selected;
                if (preferEmpty) {
                    for (int i = 0; i < 9; i++) {
                        if (mc.player.getInventory().items.get(i).isEmpty()) {
                            emptySlot = i;
                            break;
                        }
                    }
                }

                InventoryUtils.swapSlot(emptySlot, true);

                PacketUtils.send(new ServerboundPickItemPacket(slot));

                InventoryUtils.swapSlot(prevSlot, false);

                return emptySlot;
            }
        }
        return slot;
    }

    /**
     * Find the true slot (can be used in packets)
     *
     * @param items the items
     * @return the int
     */
    public static int findSlotInMain(Item... items) {
        if (mc.player != null) {
            for (var ref = new Object() {
                int i = 9;
            }; ref.i < 36; ref.i++) {
                if (Arrays.stream(items).anyMatch(item -> item == mc.player.getInventory().getItem(ref.i).getItem())) {
                    return ref.i;
                }
            }
        }
        return -1;
    }

    /**
     * Find a non-pure hotbar slot
     *
     * @param items A list of items to search for
     * @return The hotbar slot
     */
    public static int findSlotInHotbar(Item... items) {
        if (mc.player != null) {
            for (var ref = new Object() {
                int i = 0;
            }; ref.i < 9; ref.i++) {
                if (Arrays.stream(items).anyMatch(item -> item == mc.player.getInventory().getItem(getHotbarOffset() + ref.i).getItem())) {
                    return ref.i;
                }
            }
        }
        return -1;
    }

    public static int findEmptySlotInHotbar(int i) {
        if (mc.player != null) {
            for (var ref = new Object() {
                int i = 0;
            }; ref.i < 9; ref.i++) {
                if (mc.player.getInventory().getItem(getHotbarOffset() + ref.i).isEmpty()) {
                    return ref.i;
                }
            }
        }
        return i;
    }

    public static int getHotbarId(int slot) {
        assert mc.player != null;
        return mc.player.containerMenu.slots.get(getHotbarOffset() + slot).index;
    }

    public static boolean isHotbarSlot(int slot) {
        if (slot < 0) return false;
        if (slot < 9) {
            return true;
        }

        return slot > getHotbarOffset();
    }

    public static int getInventoryOffset() {
        assert mc.player != null;
        return mc.player.containerMenu.slots.size() == 46 ? mc.player.containerMenu instanceof CraftingMenu ? 10
                : 9 : mc.player.containerMenu.slots.size() - 36;
    }

    public static int getHotbarOffset() {
        return getInventoryOffset() + 27;
    }

    /**
     * Find the true slot (can be used in packets)
     *
     * @param items A list of items to search for
     * @return The inventory slot
     */
    public static int findSlot(Item... items) {
        if (mc.player != null) {
            NonNullList<Slot> slots = mc.player.containerMenu.slots;
            for (var ref = new Object() {
                int i = getInventoryOffset();
            }; ref.i < slots.size(); ref.i++) {
                if (Arrays.stream(items).anyMatch(item -> slots.get(ref.i).hasItem() && item == slots.get(ref.i).getItem().getItem())) {
                    return mc.player.containerMenu.slots.get(ref.i).index;
                }
            }
        }
        return -1;
    }

    /**
     * Find the true slot (can be used in packets)
     *
     * @param items A list of items to search for
     * @return The inventory slot
     */
    public static int findAnySlot(Item... items) {
        int finalInt = -1;
        if (mc.player != null) {
            NonNullList<Slot> slots = mc.player.containerMenu.slots;
            for (var ref = new Object() {
                int i = 0;
            }; ref.i < slots.size(); ref.i++) {
                if (Arrays.stream(items).anyMatch(item -> slots.get(ref.i).hasItem() && item == slots.get(ref.i).getItem().getItem())) {
                    finalInt = mc.player.containerMenu.slots.get(ref.i).index;
                }
            }
        }
        return finalInt;
    }

    /**
     * Find the true slot (can be used in packets)
     *
     * @param name The item name to look for.
     * @return The inventory slot
     */
    public static int findSlot(String name) {
        return findSlot(name, false);
    }

    /**
     * Find the true slot (can be used in packets)
     *
     * @param name     The item name to look for.
     * @param contains if to look for items that names contain the name
     * @return The inventory slot
     */
    public static int findSlot(String name, boolean contains) {
        if (mc.player != null) {
            NonNullList<Slot> slots = mc.player.containerMenu.slots;
            for (int i = 0; i < slots.size(); i++) {
                if (slots.get(i).hasItem()) {
                    if ((contains && !slots.get(i).getItem().getDisplayName().getString().contains(name))
                            || (!contains && !slots.get(i).getItem().getDisplayName().getString().equalsIgnoreCase(name))) {
                        continue;
                    }
                    return mc.player.containerMenu.slots.get(i).index;
                }
            }
        }
        return -1;
    }

    /**
     * Find blocks in hotbar int.
     *
     * @return the int
     */
    public static int findBlocksInHotbar() {
        var ref1 = new Object() {
            int i = -1;
        };
        if (mc.player != null) {
            for (var ref = new Object() {
                int i = 0;
            }; ref.i < mc.player.containerMenu.slots.size() - 1; ref.i++) {
                if (mc.player.containerMenu.slots.get(ref.i).getItem().getUseAnimation() == UseAnim.BLOCK) {
                    ref1.i = mc.player.containerMenu.slots.get(ref.i).index;
                }
            }
        }
        return ref1.i;
    }

    /**
     * Move item.
     * Expected behavior:
     * * Click Slot, Action Type: PICKUP, Slot: 6, Button: 0, Item Key: item.minecraft.netherite_chestplate, Sync Id: 0, Revision: 23
     * * Click Slot, Action Type: PICKUP, Slot: 11, Button: 0, Item Key: item.minecraft.elytra, Sync Id: 0, Revision: 25
     * * Click Slot, Action Type: SWAP, Slot: 17, Button: 38, Item Key: item.minecraft.elytra, Sync Id: 0, Revision: 25
     * * Click Slot, Action Type: PICKUP, Slot: 17, Button: 0, Item Key: block.minecraft.air, Sync Id: 0, Revision: 25
     * * Click Slot, Action Type: SWAP, Slot: 17, Button: 38, Item Key: block.minecraft.air, Sync Id: 0, Revision: 25
     *
     * @param from the slot to switch from
     * @param to   the slot to switch to
     */
    public static void moveItem(int from, int to) {
        assert mc.player != null;
        MultiPlayerGameMode interact = mc.gameMode;
        assert interact != null;
        clickSlot(from, 0, ClickType.PICKUP);
        clickSlot(to, 0, ClickType.PICKUP);
        clickSlot(to + 6, 38, ClickType.SWAP);
        clickSlot(to + 6, 0, ClickType.PICKUP);
        clickSlot(to + 6, 38, ClickType.SWAP);
    }

    /**
     * Click slot.
     *
     * @param slot   the slot
     * @param button the button
     * @param action the action
     */
    public static void clickSlot(int slot, int button, ClickType action) {
        assert mc.player != null;
        AbstractContainerMenu handler = mc.player.containerMenu;
        handler.suppressRemoteUpdates();
        if (slot > mc.player.getInventory().getContainerSize() || slot < 0) {
            handler.setRemoteCarried(ItemStack.EMPTY);
        } else {
            if (mc.player.getInventory().getItem(slot).isEmpty()) {
                handler.setRemoteCarried(ItemStack.EMPTY);
            } else {
                handler.setRemoteCarried(mc.player.getInventory().getItem(slot));
            }
        }

        assert mc.gameMode != null;
        mc.gameMode.handleInventoryMouseClick(
                handler.containerId,
                slot,
                button,
                action,
                mc.player);
        handler.resumeRemoteUpdates();
    }

    /**
     * Gets offhand.
     *
     * @return the offhand
     */
    public static Item getOffhand() {
        assert mc.player != null;
        return mc.player.getOffhandItem().getItem();
    }

    /**
     * Gets main hand.
     *
     * @return the main hand
     */
    public static Item getMainHand() {
        assert mc.player != null;
        return mc.player.getInventory().getSelected().getItem();
    }
}
