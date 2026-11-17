package dev.godfall.registry;

import dev.godfall.Godfall;
import dev.godfall.item.MemoryFragmentItem;
import net.minecraft.world.item.BlockItem;
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

    public static final RegistryObject<Item> MEMORY_ALTAR_ITEM = ITEMS.register(
            "memory_altar",
            () -> new BlockItem(ModBlocks.MEMORY_ALTAR.get(), new Item.Properties())
    );

    private ModItems() {
    }
}
