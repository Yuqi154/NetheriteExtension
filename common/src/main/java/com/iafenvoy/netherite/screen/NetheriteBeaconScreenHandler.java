package com.iafenvoy.netherite.screen;

import com.iafenvoy.netherite.registry.NetheriteBlocks;
import com.iafenvoy.netherite.registry.NetheriteScreenHandlers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NetheriteBeaconScreenHandler extends ScreenHandler {
    private final Inventory payment = new SimpleInventory(1) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.getItem() == Items.NETHERITE_INGOT;
        }

        @Override
        public int getMaxCountPerStack() {
            return 1;
        }
    };
    private final PaymentSlot paymentSlot;
    private final ScreenHandlerContext context;
    private final PropertyDelegate propertyDelegate;

    public NetheriteBeaconScreenHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, new ArrayPropertyDelegate(4), ScreenHandlerContext.EMPTY);
    }

    public NetheriteBeaconScreenHandler(int syncId, Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        super(NetheriteScreenHandlers.NETHERITE_BEACON.get(), syncId);
        checkDataCount(propertyDelegate, 3);
        this.propertyDelegate = propertyDelegate;
        this.context = context;
        this.paymentSlot = new PaymentSlot(this.payment, 0, 136, 110);
        this.addSlot(this.paymentSlot);
        this.addProperties(propertyDelegate);

        for (int m = 0; m < 3; ++m)
            for (int l = 0; l < 9; ++l)
                this.addSlot(new Slot(inventory, l + m * 9 + 9, 36 + l * 18, 137 + m * 18));
        for (int m = 0; m < 9; ++m)
            this.addSlot(new Slot(inventory, m, 36 + m * 18, 195));
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (!player.getWorld().isClient) {
            ItemStack itemStack = this.paymentSlot.takeStack(this.paymentSlot.getMaxItemCount());
            if (!itemStack.isEmpty())
                player.dropItem(itemStack, false);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NetheriteBlocks.NETHERITE_BEACON.get());
    }

    @Override
    public void setProperty(int id, int value) {
        super.setProperty(id, value);
        this.sendContentUpdates();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!this.insertItem(itemStack2, 1, 37, true))
                    return ItemStack.EMPTY;
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (!this.paymentSlot.hasStack() && this.paymentSlot.canInsert(itemStack2) && itemStack2.getCount() == 1) {
                if (!this.insertItem(itemStack2, 0, 1, false))
                    return ItemStack.EMPTY;
            } else if (index >= 1 && index < 28) {
                if (!this.insertItem(itemStack2, 28, 37, false))
                    return ItemStack.EMPTY;
            } else if (index >= 28 && index < 37) {
                if (!this.insertItem(itemStack2, 1, 28, false))
                    return ItemStack.EMPTY;
            } else if (!this.insertItem(itemStack2, 1, 37, false))
                return ItemStack.EMPTY;

            if (itemStack2.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();

            if (itemStack2.getCount() == itemStack.getCount())
                return ItemStack.EMPTY;

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Environment(EnvType.CLIENT)
    public int getProperties() {
        return this.propertyDelegate.get(0);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public RegistryEntry<StatusEffect> getPrimaryEffect() {
        return Registries.STATUS_EFFECT.getEntry(Registries.STATUS_EFFECT.get(this.propertyDelegate.get(1)));
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public RegistryEntry<StatusEffect> getSecondaryEffect() {
        return Registries.STATUS_EFFECT.getEntry(Registries.STATUS_EFFECT.get(this.propertyDelegate.get(2)));
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public RegistryEntry<StatusEffect> getTertiaryEffect() {
        return Registries.STATUS_EFFECT.getEntry(Registries.STATUS_EFFECT.get(this.propertyDelegate.get(3)));
    }

    public void setEffects(Optional<StatusEffect> primaryEffect, Optional<StatusEffect> secondaryEffect, Optional<StatusEffect> tertiaryEffect) {
        if (this.paymentSlot.hasStack()) {
            this.propertyDelegate.set(1, primaryEffect.map(Registries.STATUS_EFFECT::getRawId).orElse(-1));
            this.propertyDelegate.set(2, secondaryEffect.map(Registries.STATUS_EFFECT::getRawId).orElse(-1));
            this.propertyDelegate.set(3, tertiaryEffect.map(Registries.STATUS_EFFECT::getRawId).orElse(-1));
            this.paymentSlot.takeStack(1);
            this.context.run(World::markDirty);
        }
    }

    public boolean hasPayment() {
        return !this.payment.getStack(0).isEmpty();
    }

    static class PaymentSlot extends Slot {
        public PaymentSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.getItem() == Items.NETHERITE_INGOT;
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }
    }
}
