package jakojaannos.townbuilder.client;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ModClientNetworkManager {
    private static ModClientNetworkManager instance;

    private final SimpleChannel channel;

    public static ModClientNetworkManager getInstance(SimpleChannel channel) {
        if (instance == null) {
            instance = new ModClientNetworkManager(channel);
        }

        return instance;
    }

    public void sendToServer(Object message) {
        channel.sendToServer(message);
    }
}
