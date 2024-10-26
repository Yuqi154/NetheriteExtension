package com.iafenvoy.netherite.registry;

import com.iafenvoy.netherite.NetheriteExtension;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public final class NetheriteItemGroups {
    public static final DeferredRegister<ItemGroup> REGISTRY = DeferredRegister.create(NetheriteExtension.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final RegistrySupplier<ItemGroup> MAIN = register("main", () -> CreativeTabRegistry.create(
            Text.translatable("itemGroup." + NetheriteExtension.MOD_ID + ".main"),
            () -> new ItemStack(NetheriteItems.NETHERITE_ELYTRA.get())
    ));

    private static RegistrySupplier<ItemGroup> register(String id, Supplier<ItemGroup> group) {
        return REGISTRY.register(id, group);
    }
}
