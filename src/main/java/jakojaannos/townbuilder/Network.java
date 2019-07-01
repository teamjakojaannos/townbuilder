package jakojaannos.townbuilder;

import jakojaannos.townbuilder.network.CreateTownBuilderCameraMessage;
import jakojaannos.townbuilder.network.MessageAdapter;
import jakojaannos.townbuilder.network.MessageField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;

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
    private static List<MessageAdapter<?, ?>> adapters = new ArrayList<>();
    private static int discriminator;

    public static ModServerNetworkManager getServer() {
        return ModServerNetworkManager.getInstance(CHANNEL_INSTANCE);
    }

    static void registerMessages() {
        register(new MessageAdapter<>(CreateTownBuilderCameraMessage::builder, CreateTownBuilderCameraMessage.Builder::build)
                         .withField(MessageField.ofInteger(CreateTownBuilderCameraMessage::getEntityId,
                                                           CreateTownBuilderCameraMessage.Builder::entityId))
                         .withClientsideHandler(CreateTownBuilderCameraMessage::handle));
    }

    static void activateServerHandlers() {
        adapters.forEach(MessageAdapter::setServerside);
    }

    @OnlyIn(Dist.CLIENT)
    static void activateClientHandlers() {
        adapters.forEach(MessageAdapter::setClientside);
    }

    private static <TMessage, TBuilder> void register(MessageAdapter<TMessage, TBuilder> messageAdapter) {
        messageAdapter.register(discriminator++, CHANNEL_INSTANCE);
        adapters.add(messageAdapter);
    }
}
