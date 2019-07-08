package jakojaannos.townbuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ModServerNetworkManager {
    private static ModServerNetworkManager instance;

    private final SimpleChannel channel;

    public static ModServerNetworkManager getInstance(SimpleChannel channel) {
        if (instance == null) {
            instance = new ModServerNetworkManager(channel);
        }

        return instance;
    }

    public void sendTo(PacketDistributor.PacketTarget target, Object message) {
        channel.send(target, message);
    }

    public void sendTo(ServerPlayerEntity playerEntity, Object message) {
        sendTo(PacketDistributor.PLAYER.with(() -> playerEntity), message);
    }

    public <MSG> IPacket<?> asPacket(MSG message) {
        return channel.toVanillaPacket(message, NetworkDirection.PLAY_TO_CLIENT);
    }
}
