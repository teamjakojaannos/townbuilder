package jakojaannos.townbuilder;

import jakojaannos.townbuilder.client.ModClientNetworkManager;
import jakojaannos.townbuilder.network.MessageAdapter;
import jakojaannos.townbuilder.network.MessageField;
import jakojaannos.townbuilder.network.messages.CameraMessages;
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
        register(new MessageAdapter<>(CameraMessages.Spawn::builder, CameraMessages.Spawn.Builder::build)
                         .withField(MessageField.ofInteger(CameraMessages.Spawn::getEntityId,
                                                           CameraMessages.Spawn.Builder::entityId))
                         .withField(MessageField.ofUUID(CameraMessages.Spawn::getUuid,
                                                        CameraMessages.Spawn.Builder::uuid))
                         .withField(MessageField.ofUUID(CameraMessages.Spawn::getOwner,
                                                        CameraMessages.Spawn.Builder::owner))
                         .withField(MessageField.ofBlockPos(CameraMessages.Spawn::getOrigin,
                                                            CameraMessages.Spawn.Builder::origin))
                         .withField(MessageField.ofFloat(CameraMessages.Spawn::getYaw,
                                                         CameraMessages.Spawn.Builder::yaw))
                         .withField(MessageField.ofFloat(CameraMessages.Spawn::getOffset,
                                                         CameraMessages.Spawn.Builder::offset))
                         .withField(MessageField.ofFloat(CameraMessages.Spawn::getHeight,
                                                         CameraMessages.Spawn.Builder::height))
                         .withClientsideHandler(CameraMessages.Spawn.Handler::new)
        );
        register(new MessageAdapter<>(CameraMessages.UpdateOrigin::builder, CameraMessages.UpdateOrigin.Builder::build)
                         .withField(MessageField.ofInteger(CameraMessages.UpdateOrigin::getEntityId,
                                                           CameraMessages.UpdateOrigin.Builder::entityId))
                         .withField(MessageField.ofVec3d(CameraMessages.UpdateOrigin::getPos,
                                                         CameraMessages.UpdateOrigin.Builder::pos))
                         .withServersideHandler(CameraMessages.UpdateOrigin.Handler::new)
        );
        register(new MessageAdapter<>(CameraMessages.UpdateFacing::builder, CameraMessages.UpdateFacing.Builder::build)
                         .withField(MessageField.ofInteger(CameraMessages.UpdateFacing::getEntityId,
                                                           CameraMessages.UpdateFacing.Builder::entityId))
                         .withField(MessageField.ofInteger(CameraMessages.UpdateFacing::getFacingIndex,
                                                           CameraMessages.UpdateFacing.Builder::facingIndex))
                         .withServersideHandler(CameraMessages.UpdateFacing.Handler::new)
        );
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
