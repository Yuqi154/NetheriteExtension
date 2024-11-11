package com.iafenvoy.netherite.block;

import com.iafenvoy.netherite.block.entity.NetheriteShulkerBoxBlockEntity;
import com.iafenvoy.netherite.block.entity.NetheriteShulkerBoxBlockEntity.AnimationStage;
import com.iafenvoy.netherite.registry.NetheriteBlocks;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NetheriteShulkerBoxBlock extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = FacingBlock.FACING;
    public static final Identifier CONTENTS = Identifier.of("contents");

    @Nullable
    private final DyeColor color;

    public NetheriteShulkerBoxBlock(@Nullable DyeColor color, Settings settings) {
        super(settings);
        this.color = color;
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP));
    }

    public static Block get(DyeColor dyeColor) {
        if (dyeColor == null)
            return NetheriteBlocks.NETHERITE_SHULKER_BOX.get();
        return switch (dyeColor) {
            case WHITE -> NetheriteBlocks.NETHERITE_WHITE_SHULKER_BOX.get();
            case ORANGE -> NetheriteBlocks.NETHERITE_ORANGE_SHULKER_BOX.get();
            case MAGENTA -> NetheriteBlocks.NETHERITE_MAGENTA_SHULKER_BOX.get();
            case LIGHT_BLUE -> NetheriteBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX.get();
            case YELLOW -> NetheriteBlocks.NETHERITE_YELLOW_SHULKER_BOX.get();
            case LIME -> NetheriteBlocks.NETHERITE_LIME_SHULKER_BOX.get();
            case PINK -> NetheriteBlocks.NETHERITE_PINK_SHULKER_BOX.get();
            case GRAY -> NetheriteBlocks.NETHERITE_GRAY_SHULKER_BOX.get();
            case LIGHT_GRAY -> NetheriteBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX.get();
            case CYAN -> NetheriteBlocks.NETHERITE_CYAN_SHULKER_BOX.get();
            case PURPLE -> NetheriteBlocks.NETHERITE_PURPLE_SHULKER_BOX.get();
            case BLUE -> NetheriteBlocks.NETHERITE_BLUE_SHULKER_BOX.get();
            case BROWN -> NetheriteBlocks.NETHERITE_BROWN_SHULKER_BOX.get();
            case GREEN -> NetheriteBlocks.NETHERITE_GREEN_SHULKER_BOX.get();
            case RED -> NetheriteBlocks.NETHERITE_RED_SHULKER_BOX.get();
            case BLACK -> NetheriteBlocks.NETHERITE_BLACK_SHULKER_BOX.get();
        };
    }

    @Environment(EnvType.CLIENT)
    public static DyeColor getColor(Block block) {
        return block instanceof NetheriteShulkerBoxBlock ? ((NetheriteShulkerBoxBlock) block).getColor() : null;
    }

    public static DyeColor getColor(Item item) {
        return getColor(Block.getBlockFromItem(item));
    }

    public static ItemStack getItemStack(DyeColor color) {
        return new ItemStack(get(color));
    }

    private static boolean canOpen(BlockState state, World world, BlockPos pos, NetheriteShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != AnimationStage.CLOSED)
            return true;
        else {
            Box box = ShulkerEntity.calculateBoundingBox(1, state.get(FACING), 0.5f).offset(pos).contract(1.0E-6).shrink(0, -1, 0);
            return world.isSpaceEmpty(box);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        NbtComponent component = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
        if (component != null) {
            NbtCompound compoundTag = component.copyNbt();
            if (compoundTag.contains("LootTable", 8))
                tooltip.add(Text.literal("???????"));
            if (compoundTag.contains("Items", 9)) {
                DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                Inventories.readNbt(compoundTag, defaultedList, context.getRegistryLookup());
                int i = 0;
                int j = 0;
                for (ItemStack itemStack : defaultedList)
                    if (!itemStack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableText mutableText = itemStack.getName().copyContentOnly();
                            mutableText.append(" x").append(String.valueOf(itemStack.getCount()));
                            tooltip.add(mutableText);
                        }
                    }
                if (j - i > 0)
                    tooltip.add(Text.translatable("container.shulkerBox.more", j - i).formatted(Formatting.ITALIC));
            }
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NetheriteShulkerBoxBlockEntity(this.color, pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, blockEntity) -> NetheriteShulkerBoxBlockEntity.tick(world1, pos, state1, (NetheriteShulkerBoxBlockEntity) blockEntity);
    }

    public @Nullable DyeColor getColor() {
        return this.color;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput((Inventory) world.getBlockEntity(pos));
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity)
            builder = builder.addDynamicDrop(CONTENTS, (consumer) -> {
                for (int i = 0; i < shulkerBoxBlockEntity.size(); ++i)
                    consumer.accept(shulkerBoxBlockEntity.getStack(i));
            });
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NetheriteShulkerBoxBlockEntity ? VoxelShapes.cuboid(((NetheriteShulkerBoxBlockEntity) blockEntity).getBoundingBox(state)) : VoxelShapes.fullCube();
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) world.getBlockEntity(pos);
        NbtCompound compoundTag = shulkerBoxBlockEntity.serializeInventory(new NbtCompound(), world.getRegistryManager());
        if (!compoundTag.isEmpty())
            itemStack.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(compoundTag));
        return itemStack;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity netheriteShulkerBoxBlockEntity) {
            if (!world.isClient && player.isCreative() && !netheriteShulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack(this.getColor());
                NbtCompound compoundTag = netheriteShulkerBoxBlockEntity.serializeInventory(new NbtCompound(), world.getRegistryManager());
                if (!compoundTag.isEmpty())
                    itemStack.set(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(compoundTag));
                if (netheriteShulkerBoxBlockEntity.hasCustomName())
                    itemStack.set(DataComponentTypes.CUSTOM_NAME, netheriteShulkerBoxBlockEntity.getCustomName());
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else
                netheriteShulkerBoxBlockEntity.generateLoot(player);
        }
        super.onBreak(world, pos, state, player);
        return state;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlock)
                shulkerBoxBlock.setStackNbt(itemStack, world.getRegistryManager());
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity)
                world.updateComparators(pos, state.getBlock());
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient)
            return ActionResult.SUCCESS;
        else if (player.isSpectator())
            return ActionResult.CONSUME;
        else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                    player.openHandledScreen(shulkerBoxBlockEntity);
                    player.incrementStat(Stats.OPEN_SHULKER_BOX);
                    PiglinBrain.onGuardedBlockInteracted(player, true);
                }
                return ActionResult.CONSUME;
            } else
                return ActionResult.PASS;
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
}
