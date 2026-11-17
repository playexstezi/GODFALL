package dev.godfall.network;

import dev.godfall.Godfall;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Godfall.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static boolean registered;

    private NetworkHandler() {
    }

    public static void register() {
        if (registered) {
            return;
        }

        CHANNEL.registerMessage(
                0,
                DialogueS2CPacket.class,
                DialogueS2CPacket::encode,
                DialogueS2CPacket::decode,
                DialogueS2CPacket::handle
        );
        CHANNEL.registerMessage(
                1,
                RiftC2SPacket.class,
                RiftC2SPacket::encode,
                RiftC2SPacket::decode,
                RiftC2SPacket::handle
        );
        registered = true;
    }

    public static void sendDialogue(ServerPlayer player, String message) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new DialogueS2CPacket(message)
        );
    }

    public static void sendRiftRequest() {
        CHANNEL.sendToServer(new RiftC2SPacket());
    }
}
