package dev.godfall;

import dev.godfall.command.GodfallCommands;
import dev.godfall.event.PlayerEvents;
import dev.godfall.network.NetworkHandler;
import dev.godfall.registry.ModBlocks;
import dev.godfall.registry.ModEntities;
import dev.godfall.registry.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Godfall.MOD_ID)
public final class Godfall {
    public static final String MOD_ID = "godfall";

    public Godfall() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());
        MinecraftForge.EVENT_BUS.addListener(GodfallCommands::register);
    }
}
