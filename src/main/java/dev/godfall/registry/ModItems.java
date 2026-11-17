package dev.godfall.registry;

import dev.godfall.Godfall;
import dev.godfall.item.MemoryFragmentItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Godfall.MOD_ID);

    public static final RegistryObject<Item> MEMORY_FRAGMENT = ITEMS.register(
            "memory_fragment",
            () -> new MemoryFragmentItem(new Item.Properties().stacksTo(16).fireResistant())
    );

    private ModItems() {
    }
}
