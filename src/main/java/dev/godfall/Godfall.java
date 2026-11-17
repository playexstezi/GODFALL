package dev.godfall;
import dev.godfall.command.GodfallCommands;
import dev.godfall.event.PlayerEvents;
import dev.godfall.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
@Mod(Godfall.MOD_ID)
public final class Godfall {
    public static final String MOD_ID = "godfall";
    public Godfall() {
        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());
        MinecraftForge.EVENT_BUS.addListener(GodfallCommands::register);
    }
}
