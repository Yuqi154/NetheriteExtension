package com.iafenvoy.netherite.screen;

import com.iafenvoy.netherite.config.NetheriteExtensionConfig;
import com.iafenvoy.netherite.registry.NetheriteBlocks;
import com.iafenvoy.netherite.registry.NetheriteScreenHandlers;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldEvents;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class NetheriteAnvilScreenHandler extends ForgingScreenHandler {
    public static final int INGREDIENT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int FAIL_COST = 0;
    private static final int BASE_COST = 1;
    private final Property levelCost = Property.create();
    private int repairItemUsage;
    @Nullable
    private String newItemName;

    public NetheriteAnvilScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public NetheriteAnvilScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
        super(NetheriteScreenHandlers.NETHERITE_ANVIL.get(), syncId, inventory, context);
        this.addProperty(this.levelCost);
    }

    public static int getNextCost(int cost) {
        return cost * 2 + 1;
    }

    @Override
    protected ForgingSlotsManager getForgingSlotsManager() {
        return ForgingSlotsManager.create()
                .input(0, 27, 47, stack -> true)
                .input(1, 76, 47, stack -> true)
                .output(2, 134, 47)
                .build();
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return (player.getAbilities().creativeMode || player.experienceLevel >= this.levelCost.get()) && this.levelCost.get() > 0;
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(NetheriteBlocks.NETHERITE_ANVIL_BLOCK.get());
    }

    public int getLevelCost() {
        return this.levelCost.get();
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        if (!player.getAbilities().creativeMode)
            player.addExperienceLevels(-this.levelCost.get());

        this.input.setStack(INGREDIENT_SLOT, ItemStack.EMPTY);
        if (this.repairItemUsage > 0) {
            ItemStack additionStack = this.input.getStack(ADDITIONAL_SLOT);
            if (!additionStack.isEmpty() && additionStack.getCount() > this.repairItemUsage) {
                additionStack.decrement(this.repairItemUsage);
                this.input.setStack(ADDITIONAL_SLOT, additionStack);
            } else this.input.setStack(ADDITIONAL_SLOT, ItemStack.EMPTY);
        } else this.input.setStack(ADDITIONAL_SLOT, ItemStack.EMPTY);

        this.levelCost.set(0);
        this.context.run((world, blockPos) -> world.syncWorldEvent(WorldEvents.ANVIL_USED, blockPos, 0));
    }

    public void setNewItemName(String string) {
        this.newItemName = string;
        if (this.getSlot(RESULT_SLOT).hasStack()) {
            ItemStack itemStack = this.getSlot(RESULT_SLOT).getStack();
            if (StringUtils.isBlank(string) || this.newItemName == null)
                itemStack.remove(DataComponentTypes.CUSTOM_NAME);
            else itemStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName));
        }
        this.updateResult();
    }

    @Override
    public void updateResult() {
        ItemStack itemStack = this.input.getStack(0);
        this.levelCost.set(1);
        int i = 0;
        long l = 0L;
        int j = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.canHaveEnchantments(itemStack)) {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = this.input.getStack(1);
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(itemStack2));
            l += (long) itemStack.getOrDefault(DataComponentTypes.REPAIR_COST, 0) + (long) itemStack3.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
            this.repairItemUsage = 0;
            int k;
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.contains(DataComponentTypes.STORED_ENCHANTMENTS);
                int m;
                int n;
                if (itemStack2.isDamageable() && itemStack2.getItem().canRepair(itemStack, itemStack3)) {
                    k = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    if (k <= 0) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }
                    for (m = 0; k > 0 && m < itemStack3.getCount(); ++m) {
                        n = itemStack2.getDamage() - k;
                        itemStack2.setDamage(n);
                        ++i;
                        k = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    }
                    this.repairItemUsage = m;
                } else {
                    if (!bl && (!itemStack2.isOf(itemStack3.getItem()) || !itemStack2.isDamageable())) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }
                    if (itemStack2.isDamageable() && !bl) {
                        k = itemStack.getMaxDamage() - itemStack.getDamage();
                        m = itemStack3.getMaxDamage() - itemStack3.getDamage();
                        n = m + itemStack2.getMaxDamage() * 12 / 100;
                        int o = k + n;
                        int p = itemStack2.getMaxDamage() - o;
                        if (p < 0) p = 0;
                        if (p < itemStack2.getDamage()) {
                            itemStack2.setDamage(p);
                            i += 2;
                        }
                    }
                    ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;
                    for (RegistryEntry<Enchantment> registryEntry : itemEnchantmentsComponent.getEnchantments()) {
                        Enchantment enchantment = registryEntry.value();
                        int q = builder.getLevel(registryEntry);
                        int r = itemEnchantmentsComponent.getLevel(registryEntry);
                        r = q == r ? r + 1 : Math.max(r, q);
                        boolean bl4 = enchantment.isAcceptableItem(itemStack);
                        if (this.player.getAbilities().creativeMode || itemStack.isOf(Items.ENCHANTED_BOOK))
                            bl4 = true;
                        for (RegistryEntry<Enchantment> enchantmentRegistryEntry : builder.getEnchantments())
                            if (!enchantmentRegistryEntry.equals(registryEntry) && !Enchantment.canBeCombined(enchantmentRegistryEntry, registryEntry)) {
                                bl4 = false;
                                ++i;
                            }
                        if (!bl4) bl3 = true;
                        else {
                            bl2 = true;
                            if (r > enchantment.getMaxLevel()) r = enchantment.getMaxLevel();
                            builder.set(registryEntry, r);
                            int s = enchantment.getAnvilCost();
                            if (bl) s = Math.max(1, s / 2);
                            i += s * r;
                            if (itemStack.getCount() > 1) i = 40;
                        }
                    }
                    if (bl3 && !bl2) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }
                }
            }
            if (this.newItemName != null && !StringHelper.isBlank(this.newItemName)) {
                if (!this.newItemName.equals(itemStack.getName().getString())) {
                    j = 1;
                    i += j;
                    itemStack2.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName));
                }
            } else if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
                j = 1;
                i += j;
                itemStack2.remove(DataComponentTypes.CUSTOM_NAME);
            }
            int t = (int) MathHelper.clamp(l + (long) i, 0L, 2147483647L);
            // this is the important line that changes things
            double cost = (1d - NetheriteExtensionConfig.INSTANCE.anvil.xp_reduction) * t;
            this.levelCost.set(cost < BASE_COST ? BASE_COST : (int) cost);
            if (i <= 0) itemStack2 = ItemStack.EMPTY;
            if (j == i && j > 0 && this.levelCost.get() >= 40) this.levelCost.set(39);
            if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) itemStack2 = ItemStack.EMPTY;
            if (!itemStack2.isEmpty()) {
                k = itemStack2.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
                if (k < itemStack3.getOrDefault(DataComponentTypes.REPAIR_COST, 0))
                    k = itemStack3.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
                if (j != i || j == 0) k = getNextCost(k);
                itemStack2.set(DataComponentTypes.REPAIR_COST, k);
                EnchantmentHelper.set(itemStack2, builder.build());
            }
            this.output.setStack(0, itemStack2);
            this.sendContentUpdates();
        } else {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
        }
    }
}