package com.iafenvoy.netherite.entity;

import com.iafenvoy.netherite.registry.NetheriteItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.iafenvoy.netherite.registry.NetheriteLoots.LAVA_FISHING_LOOT_TABLE;

public class NetheriteFishingBobberEntity extends FishingBobberEntity {
    public NetheriteFishingBobberEntity(PlayerEntity thrower, World world, int lureLevel, int luckOfTheSeaLevel) {
        super(thrower, world, lureLevel, luckOfTheSeaLevel);
    }

    private void checkForCollision() {
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        this.onCollision(hitResult);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    private FishingBobberEntity.PositionType getPositionType(BlockPos pos) {
        BlockState blockState = this.getWorld().getBlockState(pos);
        if (!blockState.isAir()) {
            FluidState fluidState = blockState.getFluidState();
            return fluidState.isIn(FluidTags.LAVA) && fluidState.isStill() && blockState.getCollisionShape(this.getWorld(), pos).isEmpty() ? FishingBobberEntity.PositionType.INSIDE_WATER : FishingBobberEntity.PositionType.INVALID;
        } else
            return FishingBobberEntity.PositionType.ABOVE_WATER;
    }

    private FishingBobberEntity.PositionType getPositionType(BlockPos start, BlockPos end) {
        return BlockPos.stream(start, end).map(this::getPositionType).reduce((positionType, positionType2) -> positionType == positionType2 ? positionType : PositionType.INVALID).orElse(FishingBobberEntity.PositionType.INVALID);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    private boolean isOpenOrLavaAround(BlockPos pos) {
        FishingBobberEntity.PositionType positionType = FishingBobberEntity.PositionType.INVALID;
        for (int i = -1; i <= 2; ++i) {
            FishingBobberEntity.PositionType positionType2 = this.getPositionType(pos.add(-2, i, -2), pos.add(2, i, 2));
            switch (positionType2) {
                case INVALID -> {
                    return false;
                }
                case ABOVE_WATER -> {
                    if (positionType == PositionType.INVALID)
                        return false;
                }
                case INSIDE_WATER -> {
                    if (positionType == PositionType.ABOVE_WATER)
                        return false;
                }
            }
            positionType = positionType2;
        }
        return true;
    }

    private boolean removeIfInvalid(PlayerEntity playerEntity) {
        ItemStack itemStack = playerEntity.getMainHandStack();
        ItemStack itemStack2 = playerEntity.getOffHandStack();
        boolean bl = itemStack.isOf(NetheriteItems.NETHERITE_FISHING_ROD.get());
        boolean bl2 = itemStack2.isOf(NetheriteItems.NETHERITE_FISHING_ROD.get());
        if (!playerEntity.isRemoved() && playerEntity.isAlive() && (bl || bl2) && this.squaredDistanceTo(playerEntity) <= 1024.0D)
            return false;
        else {
            this.discard();
            return true;
        }
    }

    @Override
    public void tick() {
        BlockPos blockPos = this.getBlockPos();
        FluidState fluidState = this.getWorld().getFluidState(blockPos);
        if (fluidState.isIn(FluidTags.WATER)) {
            super.tick();
            return;
        }

        this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.getWorld().getTime());
        this.baseTick();

        PlayerEntity playerEntity = this.getPlayerOwner();
        if (playerEntity == null) this.discard();
        else if (this.getWorld().isClient || !this.removeIfInvalid(playerEntity)) {
            if (this.isOnGround()) {
                ++this.removalTimer;
                if (this.removalTimer >= 1200) {
                    this.discard();
                    return;
                }
            } else this.removalTimer = 0;

            float fluidHeight = 0.0F;
            if (fluidState.isIn(FluidTags.LAVA))
                fluidHeight = fluidState.getHeight(this.getWorld(), blockPos);

            boolean validFluid = fluidHeight > 0.0F;
            if (this.state == FishingBobberEntity.State.FLYING) {
                if (this.hookedEntity != null) {
                    this.setVelocity(Vec3d.ZERO);
                    this.state = FishingBobberEntity.State.HOOKED_IN_ENTITY;
                    return;
                }
                if (validFluid) {
                    this.setVelocity(this.getVelocity().multiply(0.3D, 0.2D, 0.3D));
                    this.state = FishingBobberEntity.State.BOBBING;
                    return;
                }
                this.checkForCollision();
            } else {
                if (this.state == FishingBobberEntity.State.HOOKED_IN_ENTITY) {
                    if (this.hookedEntity != null) {
                        if (this.hookedEntity.isRemoved() && this.hookedEntity.getWorld().getRegistryKey() == this.getWorld().getRegistryKey()) {
                            this.hookedEntity = null;
                            this.state = FishingBobberEntity.State.FLYING;
                        } else
                            this.updatePosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D), this.hookedEntity.getZ());
                    }
                    return;
                }
                if (this.state == FishingBobberEntity.State.BOBBING) {
                    Vec3d velocity = this.getVelocity();
                    double d = this.getY() - blockPos.getY() + 0.01 * velocity.y - fluidHeight;
                    if (Math.abs(d) < 0.01D)
                        d += Math.signum(d) * 0.1D;

                    this.setVelocity(velocity.x * 0.9D, velocity.y - d * this.random.nextFloat() * 0.4D, velocity.z * 0.9D);
                    if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
                        this.inOpenWater = true;
                    } else
                        this.inOpenWater = this.inOpenWater && this.outOfOpenWaterTicks < 10 && this.isOpenOrLavaAround(blockPos);

                    if (validFluid) {
                        this.outOfOpenWaterTicks = Math.max(0, this.outOfOpenWaterTicks - 1);
                        if (this.caughtFish)
                            this.setVelocity(this.getVelocity().add(0.0D, -0.1D * this.velocityRandom.nextFloat() * this.velocityRandom.nextFloat(), 0.0D));

                        if (!this.getWorld().isClient)
                            this.tickFishingLogic();
                    } else
                        this.outOfOpenWaterTicks = Math.min(10, this.outOfOpenWaterTicks + 1);
                }
            }

            if (!fluidState.isIn(FluidTags.LAVA))
                this.setVelocity(this.getVelocity().add(0.0D, -0.06D, 0.0D));

            this.move(MovementType.SELF, this.getVelocity());
            this.updateRotation();
            if (this.state == FishingBobberEntity.State.FLYING && (this.isOnGround() || this.horizontalCollision))
                this.setVelocity(Vec3d.ZERO);

            double e = 0.92D;
            this.setVelocity(this.getVelocity().multiply(e));
            this.refreshPosition();
        }
    }

    private void tickFishingLogic() {
        ServerWorld serverWorld = (ServerWorld) this.getWorld();
        int i = 1;

        if (this.hookCountdown > 0) {
            --this.hookCountdown;
            if (this.hookCountdown <= 0) {
                this.waitCountdown = 0;
                this.fishTravelCountdown = 0;
                this.getDataTracker().set(CAUGHT_FISH, false);
            }
        } else {
            float n;
            float o;
            float p;
            double q;
            double r;
            double s;
            BlockState blockState2;
            if (this.fishTravelCountdown > 0) {
                this.fishTravelCountdown -= i;
                if (this.fishTravelCountdown > 0) {
                    this.fishAngle = (float) (this.fishAngle + this.random.nextGaussian() * 4.0D);
                    n = this.fishAngle * 0.017453292F;
                    o = MathHelper.sin(n);
                    p = MathHelper.cos(n);
                    q = this.getX() + o * this.fishTravelCountdown * 0.1F;
                    r = MathHelper.floor(this.getY()) + 1.0F;
                    s = this.getZ() + p * this.fishTravelCountdown * 0.1F;
                    blockState2 = serverWorld.getBlockState(BlockPos.ofFloored(q, r - 1.0D, s));
                    if (blockState2.isOf(Blocks.LAVA)) {
                        if (this.random.nextFloat() < 0.15F)
                            serverWorld.spawnParticles(ParticleTypes.LANDING_LAVA, q, r - 0.10000000149011612D, s, 1, o, 0.1D, p, 0.0D);

                        float k = o * 0.04F;
                        float l = p * 0.04F;
                        serverWorld.spawnParticles(ParticleTypes.FLAME, q, r, s, 0, l, 0.01D, -k, 1.0D);
                        serverWorld.spawnParticles(ParticleTypes.FLAME, q, r, s, 0, -l, 0.01D, k, 1.0D);
                    }
                } else {
                    this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                    double m = this.getY() + 0.5D;
                    serverWorld.spawnParticles(ParticleTypes.LANDING_LAVA, this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
                    serverWorld.spawnParticles(ParticleTypes.FLAME, this.getX(), m, this.getZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
                    this.hookCountdown = MathHelper.nextInt(this.random, 20, 40);
                    this.getDataTracker().set(CAUGHT_FISH, true);
                }
            } else if (this.waitCountdown > 0) {
                this.waitCountdown -= i;
                n = 0.15F;
                if (this.waitCountdown < 20)
                    n = (float) (n + (20 - this.waitCountdown) * 0.05D);
                else if (this.waitCountdown < 40)
                    n = (float) (n + (40 - this.waitCountdown) * 0.02D);
                else if (this.waitCountdown < 60)
                    n = (float) (n + (60 - this.waitCountdown) * 0.01D);

                if (this.random.nextFloat() < n) {
                    o = MathHelper.nextFloat(this.random, 0.0F, 360.0F) * 0.017453292F;
                    p = MathHelper.nextFloat(this.random, 25.0F, 60.0F);
                    q = this.getX() + MathHelper.sin(o) * p * 0.1F;
                    r = MathHelper.floor(this.getY()) + 1.0F;
                    s = this.getZ() + MathHelper.cos(o) * p * 0.1F;
                    blockState2 = serverWorld.getBlockState(BlockPos.ofFloored(q, r - 1.0D, s));
                    if (blockState2.isOf(Blocks.LAVA))
                        serverWorld.spawnParticles(ParticleTypes.SMOKE, q, r, s, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                }

                if (this.waitCountdown <= 0) {
                    this.fishAngle = MathHelper.nextFloat(this.random, 0.0F, 360.0F);
                    this.fishTravelCountdown = MathHelper.nextInt(this.random, 20, 80);
                }
            } else {
                this.waitCountdown = MathHelper.nextInt(this.random, 100, 600);
                this.waitCountdown -= this.lureLevel * 20 * 5;
            }
        }

    }

    @Override
    public int use(ItemStack usedItem) {
        BlockPos blockPos = this.getBlockPos();
        FluidState fluidState = this.getWorld().getFluidState(blockPos);
        if (fluidState.isIn(FluidTags.WATER))
            return super.use(usedItem);

        PlayerEntity playerEntity = this.getPlayerOwner();
        if (!this.getWorld().isClient && playerEntity != null) {
            int i = 0;
            if (this.hookedEntity != null) {
                this.pullHookedEntity(this.hookedEntity);
                this.getWorld().sendEntityStatus(this, (byte) 31);
                i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
            } else if (this.hookCountdown > 0) {
                LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder((ServerWorld) this.getWorld())
                        .add(LootContextParameters.ORIGIN, this.getPos())
                        .add(LootContextParameters.TOOL, usedItem)
                        .add(LootContextParameters.THIS_ENTITY, this)
                        .luck((float) this.luckOfTheSeaLevel + playerEntity.getLuck())
                        .build(LootContextTypes.FISHING);
                LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(LAVA_FISHING_LOOT_TABLE);
                List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
                Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, list);

                for (ItemStack itemStack : list) {
                    ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), itemStack);
                    double d = playerEntity.getX() - this.getX();
                    double e = playerEntity.getY() - this.getY();
                    double f = playerEntity.getZ() - this.getZ();
                    double g = 0.1D;
                    itemEntity.setVelocity(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * g);
                    itemEntity.setInvulnerable(true);
                    this.getWorld().spawnEntity(itemEntity);
                    playerEntity.getWorld().spawnEntity(new ExperienceOrbEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, this.random.nextInt(6) + 1));
                }
                i = 1;
            }

            if (this.isOnGround()) i = 2;
            this.discard();
            return i;
        } else return 0;
    }
}
