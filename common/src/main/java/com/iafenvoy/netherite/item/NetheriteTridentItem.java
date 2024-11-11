package com.iafenvoy.netherite.item;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.entity.NetheriteTridentEntity;
import com.iafenvoy.netherite.registry.NetheriteCriteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class NetheriteTridentItem extends TridentItem {
    public NetheriteTridentItem(Item.Settings settings) {
        super(settings);
        AttributeModifiersComponent component = this.getComponents().getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, new AttributeModifiersComponent(List.of(), true));
        component.modifiers().forEach(eam -> eam.modifier().value = eam.modifier().value * NetheriteExtensionConfig.INSTANCE.damage.trident_damage_multiplier + NetheriteExtensionConfig.INSTANCE.damage.trident_damage_addition);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (i >= 10) {
                float riptideLevel = EnchantmentHelper.getTridentSpinAttackStrength(stack, user);
                if (riptideLevel <= 0 || playerEntity.isTouchingWaterOrRain() || playerEntity.isInLava()) {
                    if (!world.isClient) {
                        stack.damage(1, playerEntity, EquipmentSlot.MAINHAND);
                        if (riptideLevel == 0) {
                            NetheriteTridentEntity tridentEntity = new NetheriteTridentEntity(world, playerEntity, stack);
                            tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F + riptideLevel * 0.5F, 1.0F);
                            if (playerEntity.getAbilities().creativeMode)
                                tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

                            world.spawnEntity(tridentEntity);
                            world.playSoundFromEntity(null, tridentEntity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!playerEntity.getAbilities().creativeMode)
                                playerEntity.getInventory().removeOne(stack);
                        } else
                            NetheriteCriteria.RIPTIDE_NETHERITE_TRIDENT.get().trigger((ServerPlayerEntity) playerEntity);
                    }
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    if (riptideLevel > 0) {
                        float f = playerEntity.getYaw();
                        float g = playerEntity.getPitch();
                        float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                        float k = -MathHelper.sin(g * 0.017453292F);
                        float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                        float m = MathHelper.sqrt(h * h + k * k + l * l);
                        float n = 3.0F * ((1.0F + riptideLevel) / 4.0F);
                        h *= n / m;
                        k *= n / m;
                        l *= n / m;
                        playerEntity.addVelocity(h, k, l);
                        playerEntity.useRiptide(20, 8.0F, stack);
                        if (playerEntity.isOnGround()) {
                            float o = 1.1999999F;
                            playerEntity.move(MovementType.SELF, new Vec3d(0.0D, o, 0.0D));
                        }

                        SoundEvent soundEvent3;
                        if (riptideLevel >= 3) soundEvent3 = SoundEvents.ITEM_TRIDENT_RIPTIDE_3.value();
                        else if (riptideLevel == 2) soundEvent3 = SoundEvents.ITEM_TRIDENT_RIPTIDE_2.value();
                        else soundEvent3 = SoundEvents.ITEM_TRIDENT_RIPTIDE_1.value();
                        world.playSoundFromEntity(null, playerEntity, soundEvent3, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1)
            return TypedActionResult.fail(itemStack);
        else if (EnchantmentHelper.getTridentSpinAttackStrength(itemStack, user) > 0 && !(user.isTouchingWaterOrRain() || user.isInLava()))
            return TypedActionResult.fail(itemStack);
        else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }
}
