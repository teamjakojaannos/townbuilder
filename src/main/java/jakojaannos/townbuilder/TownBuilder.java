package jakojaannos.townbuilder;

import jakojaannos.townbuilder.client.TownBuilderClient;
import jakojaannos.townbuilder.init.InitEntityTypes;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Log4j2
@Mod(TownBuilder.MOD_ID)
public class TownBuilder {
    public static final String MOD_ID = "townbuilder";

    private static TownBuilder instance;

    public TownBuilder() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this); // Lifecycle/mod events
        MinecraftForge.EVENT_BUS.register(this); // Server/Game events

        if (instance != null) {
            throw LOGGER.throwing(new IllegalStateException("Multiple instances of mod TownBuilder being constructed!"));
        }
        instance = this;

        Network.registerMessages();
    }

    @OnlyIn(Dist.CLIENT)
    private TownBuilderClient client;

    @OnlyIn(Dist.CLIENT)
    public static TownBuilderClient getClient() {
        return instance.client;
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        //Network.registerMessages();
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        InitEntityTypes.flag = true;
        Network.activateClientHandlers();
        client = new TownBuilderClient();
    }

    @SubscribeEvent
    public void onServerStarting(final FMLServerStartingEvent event) {
        Network.activateServerHandlers();
    }
}
