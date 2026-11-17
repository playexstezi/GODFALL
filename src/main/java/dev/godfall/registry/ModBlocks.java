package dev.godfall.registry;

import dev.godfall.Godfall;
import dev.godfall.block.MemoryAltarBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Godfall.MOD_ID);

    public static final RegistryObject<Block> MEMORY_ALTAR = BLOCKS.register(
            "memory_altar",
            () -> new MemoryAltarBlock(Block.Properties.of()
                    .strength(3.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(MemoryAltarBlock.ACTIVATED) ? 2 : 7))
    );

    private ModBlocks() {
    }
}
