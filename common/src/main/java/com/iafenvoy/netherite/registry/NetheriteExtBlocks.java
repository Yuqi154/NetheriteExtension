package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import com.iafenvoy.netherite.block.NetheriteAnvilBlock;
import com.iafenvoy.netherite.block.NetheriteBeaconBlock;
import com.iafenvoy.netherite.block.NetheriteShulkerBoxBlock;
import com.iafenvoy.netherite.block.entity.NetheriteBeaconBlockEntity;
import com.iafenvoy.netherite.block.entity.NetheriteShulkerBoxBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class NetheriteExtBlocks {
    public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);

    public static RegistrySupplier<Block> NETHERITE_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_shulker_box", () -> createShulkerBoxBlock(null, AbstractBlock.Settings.create().mapColor(MapColor.GRAY)));
    public static RegistrySupplier<Block> NETHERITE_WHITE_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_white_shulker_box", () -> createShulkerBoxBlock(DyeColor.WHITE, AbstractBlock.Settings.create().mapColor(MapColor.WHITE)));
    public static RegistrySupplier<Block> NETHERITE_ORANGE_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_orange_shulker_box", () -> createShulkerBoxBlock(DyeColor.ORANGE, AbstractBlock.Settings.create().mapColor(MapColor.ORANGE)));
    public static RegistrySupplier<Block> NETHERITE_MAGENTA_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_magenta_shulker_box", () -> createShulkerBoxBlock(DyeColor.MAGENTA, AbstractBlock.Settings.create().mapColor(MapColor.MAGENTA)));
    public static RegistrySupplier<Block> NETHERITE_LIGHT_BLUE_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_light_blue_shulker_box", () -> createShulkerBoxBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE)));
    public static RegistrySupplier<Block> NETHERITE_YELLOW_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_yellow_shulker_box", () -> createShulkerBoxBlock(DyeColor.YELLOW, AbstractBlock.Settings.create().mapColor(MapColor.YELLOW)));
    public static RegistrySupplier<Block> NETHERITE_LIME_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_lime_shulker_box", () -> createShulkerBoxBlock(DyeColor.LIME, AbstractBlock.Settings.create().mapColor(MapColor.LIME)));
    public static RegistrySupplier<Block> NETHERITE_PINK_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_pink_shulker_box", () -> createShulkerBoxBlock(DyeColor.PINK, AbstractBlock.Settings.create().mapColor(MapColor.PINK)));
    public static RegistrySupplier<Block> NETHERITE_GRAY_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_gray_shulker_box", () -> createShulkerBoxBlock(DyeColor.GRAY, AbstractBlock.Settings.create().mapColor(MapColor.GRAY)));
    public static RegistrySupplier<Block> NETHERITE_LIGHT_GRAY_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_light_gray_shulker_box", () -> createShulkerBoxBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY)));
    public static RegistrySupplier<Block> NETHERITE_CYAN_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_cyan_shulker_box", () -> createShulkerBoxBlock(DyeColor.CYAN, AbstractBlock.Settings.create().mapColor(MapColor.CYAN)));
    public static RegistrySupplier<Block> NETHERITE_PURPLE_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_purple_shulker_box", () -> createShulkerBoxBlock(DyeColor.PURPLE, AbstractBlock.Settings.create().mapColor(MapColor.PURPLE)));
    public static RegistrySupplier<Block> NETHERITE_BLUE_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_blue_shulker_box", () -> createShulkerBoxBlock(DyeColor.BLUE, AbstractBlock.Settings.create().mapColor(MapColor.BLUE)));
    public static RegistrySupplier<Block> NETHERITE_BROWN_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_brown_shulker_box", () -> createShulkerBoxBlock(DyeColor.BROWN, AbstractBlock.Settings.create().mapColor(MapColor.BROWN)));
    public static RegistrySupplier<Block> NETHERITE_GREEN_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_green_shulker_box", () -> createShulkerBoxBlock(DyeColor.GREEN, AbstractBlock.Settings.create().mapColor(MapColor.GREEN)));
    public static RegistrySupplier<Block> NETHERITE_RED_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_red_shulker_box", () -> createShulkerBoxBlock(DyeColor.RED, AbstractBlock.Settings.create().mapColor(MapColor.RED)));
    public static RegistrySupplier<Block> NETHERITE_BLACK_SHULKER_BOX = BLOCK_REGISTRY.register("netherite_black_shulker_box", () -> createShulkerBoxBlock(DyeColor.BLACK, AbstractBlock.Settings.create().mapColor(MapColor.BLACK)));

    public static RegistrySupplier<Block> FAKE_NETHERITE_BLOCK = BLOCK_REGISTRY.register("fake_netherite_block", () -> new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BLACK).sounds(BlockSoundGroup.NETHERITE)));

    public static RegistrySupplier<Block> NETHERITE_ANVIL_BLOCK = BLOCK_REGISTRY.register("netherite_anvil", () -> new NetheriteAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL)));

    public static RegistrySupplier<Block> NETHERITE_BEACON = BLOCK_REGISTRY.register("netherite_beacon", () -> new NetheriteBeaconBlock(AbstractBlock.Settings.copy(Blocks.BEACON)));

    public static RegistrySupplier<BlockEntityType<NetheriteShulkerBoxBlockEntity>> NETHERITE_SHULKER_BOX_ENTITY = BLOCK_ENTITY_REGISTRY.register(new Identifier(NetheriteExtension.MOD_ID, "netherite_shulker_box"), () -> BlockEntityType.Builder.create(NetheriteShulkerBoxBlockEntity::new,
                    NETHERITE_SHULKER_BOX.get(), NETHERITE_BLACK_SHULKER_BOX.get(), NETHERITE_BLUE_SHULKER_BOX.get(), NETHERITE_BROWN_SHULKER_BOX.get(), NETHERITE_CYAN_SHULKER_BOX.get(), NETHERITE_GRAY_SHULKER_BOX.get(), NETHERITE_GREEN_SHULKER_BOX.get(), NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), NETHERITE_LIME_SHULKER_BOX.get(), NETHERITE_MAGENTA_SHULKER_BOX.get(), NETHERITE_ORANGE_SHULKER_BOX.get(), NETHERITE_PINK_SHULKER_BOX.get(), NETHERITE_PURPLE_SHULKER_BOX.get(), NETHERITE_RED_SHULKER_BOX.get(), NETHERITE_WHITE_SHULKER_BOX.get(), NETHERITE_YELLOW_SHULKER_BOX.get())
            .build(null));

    private static NetheriteShulkerBoxBlock createShulkerBoxBlock(DyeColor color, AbstractBlock.Settings settings) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return !(blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) || shulkerBoxBlockEntity.suffocates();
        };
        return new NetheriteShulkerBoxBlock(color, settings.solid()
                .strength(2.0F)
                .resistance(1200.0F)
                .dynamicBounds()
                .nonOpaque()
                .suffocates(contextPredicate)
                .blockVision(contextPredicate)
                .pistonBehavior(PistonBehavior.DESTROY)
                .solidBlock((state, world, pos) -> true)
        );
    }

    public static RegistrySupplier<BlockEntityType<NetheriteBeaconBlockEntity>> NETHERITE_BEACON_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register(new Identifier(NetheriteExtension.MOD_ID, "netherite_beacon"), () -> BlockEntityType.Builder.create(NetheriteBeaconBlockEntity::new, NETHERITE_BEACON.get()).build(null));


}
