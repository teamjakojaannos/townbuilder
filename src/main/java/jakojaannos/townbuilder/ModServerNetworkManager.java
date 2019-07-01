package jakojaannos.townbuilder;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModServerNetworkManager {
    private static ModServerNetworkManager instance;
    private final SimpleChannel channel;

    public ModServerNetworkManager(SimpleChannel channel) {
        this.channel = channel;
    }

    public static ModServerNetworkManager getInstance(SimpleChannel channel) {
        if (instance == null) {
            instance = new ModServerNetworkManager(channel);
        }

        return instance;
    }

    public void sendTo(ServerPlayerEntity playerEntity, Object message) {
        //channel.send(PacketDistributor.PLAYER.with(() -> playerEntity), message);
        channel.sendTo(message, playerEntity.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
