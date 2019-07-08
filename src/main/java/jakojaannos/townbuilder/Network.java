package jakojaannos.townbuilder;

import jakojaannos.townbuilder.client.ModClientNetworkManager;
import jakojaannos.townbuilder.network.MessageAdapter;
import jakojaannos.townbuilder.network.MessageField;
import jakojaannos.townbuilder.network.SpawnTownBuilderCameraMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling communication between client and the server.
 */
public class Network {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TownBuilder.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static Map<Class, MessageAdapter<?, ?>> adapters = new HashMap<>();
    private static int discriminator;

    public static ModServerNetworkManager getServer() {
        return ModServerNetworkManager.getInstance(CHANNEL_INSTANCE);
    }

    @OnlyIn(Dist.CLIENT)
    public static ModClientNetworkManager getClient() {
        return ModClientNetworkManager.getInstance(CHANNEL_INSTANCE);
    }

    static void registerMessages() {
        register(new MessageAdapter<>(SpawnTownBuilderCameraMessage::builder, SpawnTownBuilderCameraMessage.Builder::build)
                         .withField(MessageField.ofInteger(SpawnTownBuilderCameraMessage::getEntityId,
                                                           SpawnTownBuilderCameraMessage.Builder::entityId))
                         .withField(MessageField.ofUUID(SpawnTownBuilderCameraMessage::getUuid,
                                                        SpawnTownBuilderCameraMessage.Builder::uuid))
                         .withField(MessageField.ofUUID(SpawnTownBuilderCameraMessage::getOwner,
                                                        SpawnTownBuilderCameraMessage.Builder::owner))
                         .withField(MessageField.ofBlockPos(SpawnTownBuilderCameraMessage::getOrigin,
                                                            SpawnTownBuilderCameraMessage.Builder::origin))
                         .withField(MessageField.ofFloat(SpawnTownBuilderCameraMessage::getYaw,
                                                         SpawnTownBuilderCameraMessage.Builder::yaw))
                         .withField(MessageField.ofFloat(SpawnTownBuilderCameraMessage::getOffset,
                                                         SpawnTownBuilderCameraMessage.Builder::offset))
                         .withField(MessageField.ofFloat(SpawnTownBuilderCameraMessage::getHeight,
                                                         SpawnTownBuilderCameraMessage.Builder::height))
                         .withClientsideHandler(SpawnTownBuilderCameraMessage::handleClientside));
    }

    static void activateServerHandlers() {
        adapters.values().forEach(MessageAdapter::setServerside);
    }

    @OnlyIn(Dist.CLIENT)
    static void activateClientHandlers() {
        adapters.values().forEach(MessageAdapter::setClientside);
    }

    private static <TMessage, TBuilder> void register(MessageAdapter<TMessage, TBuilder> messageAdapter) {
        messageAdapter.register(discriminator++, CHANNEL_INSTANCE);
        adapters.put(messageAdapter.getMessageClass(), messageAdapter);
    }
}
