package dev.godfall.client;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
public final class ClientPacketHandler {
    private ClientPacketHandler() {}
    public static void handleDialogue(String message) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.displayClientMessage(Component.literal(message), false);
        }
    }
}
