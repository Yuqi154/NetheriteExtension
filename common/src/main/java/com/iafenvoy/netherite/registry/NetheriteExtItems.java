package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.block.NetheriteShulkerBoxBlock;
import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.item.NetheriteBowItem;
import com.iafenvoy.netherite.item.NetheriteFishingRodItem;
import com.iafenvoy.netherite.item.NetheriteHorseArmorItem;
import com.iafenvoy.netherite.item.NetheriteTridentItem;
import com.iafenvoy.netherite.item.impl.NetheriteElytraItem;
import com.iafenvoy.netherite.item.impl.NetheriteShieldItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.function.Supplier;

public final class NetheriteExtItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.ITEM);

    public static final Item.Settings NETHERITE_SHULKER_BOX_ITEM_SETTINGS = new Item.Settings().maxCount(1).fireproof();
    public static final CauldronBehavior CLEAN_NETHERITE_BOX = (state, world, pos, player, hand, stack) -> {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (!(block instanceof NetheriteShulkerBoxBlock)) return ActionResult.PASS;
        else {
            if (!world.isClient) {
                ItemStack itemStack = new ItemStack(NetheriteExtBlocks.NETHERITE_SHULKER_BOX.get());
                if (stack.hasNbt()) itemStack.setNbt(stack.getOrCreateNbt().copy());
                player.setStackInHand(hand, itemStack);
                player.incrementStat(Stats.CLEAN_SHULKER_BOX);
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            }
            return ActionResult.success(world.isClient);
        }
    };
    public static final RegistrySupplier<Item> NETHERITE_ELYTRA = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_elytra"), () -> NetheriteElytraItem.create(new Item.Settings().maxDamage(NetheriteExtensionConfig.getInstance().durability.elytra).rarity(Rarity.UNCOMMON).fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_FISHING_ROD = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_fishing_rod"), () -> new NetheriteFishingRodItem(new Item.Settings().maxDamage(NetheriteExtensionConfig.getInstance().durability.fishing_rod).fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_BOW = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_bow"), () -> new NetheriteBowItem(new Item.Settings().maxDamage(NetheriteExtensionConfig.getInstance().durability.bow).fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_CROSSBOW = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_crossbow"), () -> new CrossbowItem(new Item.Settings().maxDamage(NetheriteExtensionConfig.getInstance().durability.crossbow).fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_TRIDENT = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_trident"), () -> new NetheriteTridentItem(new Item.Settings().maxDamage(NetheriteExtensionConfig.getInstance().durability.trident).fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_SHULKER_BOX = register("netherite_shulker_box", () -> new BlockItem(NetheriteExtBlocks.NETHERITE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_WHITE_SHULKER_BOX = register("netherite_white_shulker_box", () -> new BlockItem(NetheriteExtBlocks.NETHERITE_WHITE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_ORANGE_SHULKER_BOX = register("netherite_orange_shulker_box", () -> new BlockItem(NetheriteExtBlocks.NETHERITE_ORANGE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_MAGENTA_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_MAGENTA_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_MAGENTA_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_LIGHT_BLUE_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_YELLOW_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_YELLOW_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_YELLOW_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_LIME_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_LIME_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_LIME_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_PINK_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_PINK_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_PINK_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_GRAY_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_GRAY_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_GRAY_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_LIGHT_GRAY_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_CYAN_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_CYAN_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_CYAN_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_PURPLE_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_PURPLE_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_PURPLE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_BLUE_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_BLUE_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_BLUE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_BROWN_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_BROWN_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_BROWN_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_GREEN_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_GREEN_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_GREEN_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_RED_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_RED_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_RED_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_BLACK_SHULKER_BOX = register(NetheriteExtBlocks.NETHERITE_BLACK_SHULKER_BOX, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_BLACK_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    public static final RegistrySupplier<Item> NETHERITE_BEACON = register(NetheriteExtBlocks.NETHERITE_BEACON, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_BEACON.get(), new Item.Settings().maxCount(64).fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_HORSE_ARMOR = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_horse_armor"), () -> new NetheriteHorseArmorItem(15, new Item.Settings().maxCount(1).fireproof()));
    public static final RegistrySupplier<Item> FAKE_NETHERITE_BLOCK = register(NetheriteExtBlocks.FAKE_NETHERITE_BLOCK, () -> new BlockItem(NetheriteExtBlocks.FAKE_NETHERITE_BLOCK.get(), new Item.Settings().fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_ANVIL_ITEM = register(NetheriteExtBlocks.NETHERITE_ANVIL_BLOCK, () -> new BlockItem(NetheriteExtBlocks.NETHERITE_ANVIL_BLOCK.get(), new Item.Settings().fireproof()));
    public static final RegistrySupplier<Item> NETHERITE_SHEARS = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_shears"), () -> new ShearsItem(new Item.Settings().fireproof().maxDamage(NetheriteExtensionConfig.getInstance().durability.shears)));
    public static final RegistrySupplier<Item> NETHERITE_SHIELD = register(new Identifier(NetheriteExtension.MOD_ID, "netherite_shield"), () -> NetheriteShieldItem.create(new Item.Settings().fireproof().maxDamage(NetheriteExtensionConfig.getInstance().durability.shield)));

    public static final List<RegistrySupplier<Item>> NETHERITE_SHULKERS = List.of(NETHERITE_SHULKER_BOX, NETHERITE_WHITE_SHULKER_BOX, NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_GRAY_SHULKER_BOX, NETHERITE_BLACK_SHULKER_BOX, NETHERITE_BROWN_SHULKER_BOX, NETHERITE_RED_SHULKER_BOX, NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_YELLOW_SHULKER_BOX, NETHERITE_LIME_SHULKER_BOX, NETHERITE_GREEN_SHULKER_BOX, NETHERITE_CYAN_SHULKER_BOX, NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_BLUE_SHULKER_BOX, NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_PINK_SHULKER_BOX);

    private static RegistrySupplier<Item> register(RegistrySupplier<Block> block, Supplier<BlockItem> item) {
        return register(block.getId(), () -> {
            Item i = item.get();
            if (block.get() instanceof NetheriteShulkerBoxBlock)
                CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(i, CLEAN_NETHERITE_BOX);
            return i;
        });
    }

    private static RegistrySupplier<Item> register(String id, Supplier<Item> item) {
        return REGISTRY.register(id, () -> {
            Item i = item.get();
            if (i instanceof BlockItem blockItem) blockItem.appendBlocks(Item.BLOCK_ITEMS, i);
            return i;
        });
    }

    private static RegistrySupplier<Item> register(Identifier id, Supplier<Item> item) {
        return REGISTRY.register(id, () -> {
            Item i = item.get();
            if (i instanceof BlockItem blockItem) blockItem.appendBlocks(Item.BLOCK_ITEMS, i);
            return i;
        });
    }

    public static void init() {
        CreativeTabRegistry.append(NetheriteExtItemGroups.MAIN,
                NETHERITE_SHULKER_BOX,
                NETHERITE_WHITE_SHULKER_BOX,
                NETHERITE_LIGHT_GRAY_SHULKER_BOX,
                NETHERITE_GRAY_SHULKER_BOX,
                NETHERITE_BLACK_SHULKER_BOX,
                NETHERITE_BROWN_SHULKER_BOX,
                NETHERITE_RED_SHULKER_BOX,
                NETHERITE_ORANGE_SHULKER_BOX,
                NETHERITE_YELLOW_SHULKER_BOX,
                NETHERITE_LIME_SHULKER_BOX,
                NETHERITE_GREEN_SHULKER_BOX,
                NETHERITE_CYAN_SHULKER_BOX,
                NETHERITE_LIGHT_BLUE_SHULKER_BOX,
                NETHERITE_BLUE_SHULKER_BOX,
                NETHERITE_PURPLE_SHULKER_BOX,
                NETHERITE_MAGENTA_SHULKER_BOX,
                NETHERITE_PINK_SHULKER_BOX,
                NETHERITE_ANVIL_ITEM,
                NETHERITE_BEACON,
                NETHERITE_SHEARS,
                NETHERITE_ELYTRA,
                NETHERITE_FISHING_ROD,
                NETHERITE_BOW,
                NETHERITE_CROSSBOW,
                NETHERITE_HORSE_ARMOR,
                NETHERITE_TRIDENT,
                NETHERITE_SHIELD,
                FAKE_NETHERITE_BLOCK);
        NETHERITE_SHULKERS.forEach(x -> CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(x.get(), CLEAN_NETHERITE_BOX));
        DispenserBlock.registerBehavior(NETHERITE_SHEARS.get(), new ShearsDispenserBehavior());
    }
}
