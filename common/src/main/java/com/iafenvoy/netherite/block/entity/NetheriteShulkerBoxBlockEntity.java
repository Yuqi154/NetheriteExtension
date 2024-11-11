package com.iafenvoy.netherite.block.entity;

import com.iafenvoy.netherite.block.NetheriteShulkerBoxBlock;
import com.iafenvoy.netherite.registry.NetheriteBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

public class NetheriteShulkerBoxBlockEntity extends LootableContainerBlockEntity implements SidedInventory {
    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 27).toArray();
    @Nullable
    private final DyeColor cachedColor;
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private int viewerCount;
    private AnimationStage animationStage = AnimationStage.CLOSED;
    private float animationProgress;
    private float prevAnimationProgress;

    public NetheriteShulkerBoxBlockEntity(@Nullable DyeColor dyeColor, BlockPos blockPos, BlockState blockState) {
        super(NetheriteBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), blockPos, blockState);
        this.cachedColor = dyeColor;
    }

    public NetheriteShulkerBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NetheriteBlocks.NETHERITE_SHULKER_BOX_ENTITY.get(), blockPos, blockState);
        this.cachedColor = ShulkerBoxBlock.getColor(blockState.getBlock());
    }

    public static void tick(World world, BlockPos pos, BlockState state, NetheriteShulkerBoxBlockEntity blockEntity) {
        blockEntity.updateAnimation(world, pos, state);
    }

    private static void updateNeighborStates(World world, BlockPos pos, BlockState state) {
        state.updateNeighbors(world, pos, 3);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return !(block instanceof NetheriteShulkerBoxBlock) && !(block instanceof ShulkerBoxBlock);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ShulkerBoxScreenHandler(syncId, playerInventory, this);
    }

    public void deserializeInventory(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(tag) && tag.contains("Items", 9))
            Inventories.readNbt(tag, this.inventory, lookup);
    }

    @Override
    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(tag, lookup);
        this.deserializeInventory(tag, lookup);
    }

    public float getAnimationProgress(float f) {
        return MathHelper.lerp(f, this.prevAnimationProgress, this.animationProgress);
    }

    public AnimationStage getAnimationStage() {
        return this.animationStage;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    public Box getBoundingBox(BlockState state) {
        return this.getBoundingBox(state.get(NetheriteShulkerBoxBlock.FACING));
    }

    public Box getBoundingBox(Direction openDirection) {
        float f = this.getAnimationProgress(1.0F);
        return VoxelShapes.fullCube().getBoundingBox().stretch(0.5F * f * openDirection.getOffsetX(), 0.5F * f * openDirection.getOffsetY(), 0.5F * f * openDirection.getOffsetZ());
    }

    private Box getCollisionBox(Direction facing) {
        Direction direction = facing.getOpposite();
        return this.getBoundingBox(facing).shrink(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
    }

    public DyeColor getColor() {
        return this.cachedColor;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.netheriteShulkerBox");
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.viewerCount < 0) this.viewerCount = 0;

            ++this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount == 1) {
                this.world.emitGameEvent(player, GameEvent.CONTAINER_OPEN, this.pos);
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount <= 0) {
                this.world.emitGameEvent(player, GameEvent.CONTAINER_CLOSE, this.pos);
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            if (data == 0) {
                this.animationStage = AnimationStage.CLOSING;
                updateNeighborStates(this.getWorld(), this.pos, this.getCachedState());
            }
            if (data == 1) {
                this.animationStage = AnimationStage.OPENING;
                updateNeighborStates(this.getWorld(), this.pos, this.getCachedState());
            }
            return true;
        } else
            return super.onSyncedBlockEvent(type, data);
    }

    private void pushEntities(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof NetheriteShulkerBoxBlock) {
            Direction direction = state.get(NetheriteShulkerBoxBlock.FACING);
            Box box = ShulkerEntity.calculateBoundingBox(this.prevAnimationProgress, direction, this.animationProgress).offset(pos);
            List<Entity> list = world.getOtherEntities(null, box);
            if (!list.isEmpty())
                for (Entity entity : list)
                    if (entity.getPistonBehavior() != PistonBehavior.IGNORE)
                        entity.move(MovementType.SHULKER_BOX, new Vec3d(
                                (box.getLengthX() + 0.01) * (double) direction.getOffsetX(),
                                (box.getLengthY() + 0.01) * (double) direction.getOffsetY(),
                                (box.getLengthZ() + 0.01) * (double) direction.getOffsetZ()
                        ));
        }
    }

    public NbtCompound serializeInventory(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        if (!this.writeLootTable(tag))
            Inventories.readNbt(tag, this.inventory, lookup);
        return tag;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    public boolean suffocates() {
        return this.animationStage == AnimationStage.CLOSED;
    }

    @Override
    public void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(tag, lookup);
        this.serializeInventory(tag, lookup);
    }

    private void updateAnimation(World world, BlockPos pos, BlockState state) {
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage) {
            case CLOSED -> this.animationProgress = 0.0F;
            case OPENING -> {
                this.animationProgress += 0.1F;
                if (this.animationProgress >= 1.0F) {
                    this.animationStage = AnimationStage.OPENED;
                    this.animationProgress = 1.0F;
                    updateNeighborStates(world, pos, state);
                }
                this.pushEntities(world, pos, state);
            }
            case CLOSING -> {
                this.animationProgress -= 0.1F;
                if (this.animationProgress <= 0.0F) {
                    this.animationStage = AnimationStage.CLOSED;
                    this.animationProgress = 0.0F;
                    updateNeighborStates(world, pos, state);
                }
            }
            case OPENED -> this.animationProgress = 1.0F;
        }
    }

    public enum AnimationStage {
        CLOSED, OPENING, OPENED, CLOSING
    }
}
