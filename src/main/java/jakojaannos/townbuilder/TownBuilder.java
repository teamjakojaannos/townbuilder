package jakojaannos.townbuilder;

import jakojaannos.townbuilder.client.ShaderHelper;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.init.InitContainerTypes;
import lombok.extern.log4j.Log4j2;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Log4j2
@Mod(TownBuilder.MOD_ID)
public class TownBuilder {
    public static final String MOD_ID = "townbuilder";

    public TownBuilder() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this); // Lifecycle/mod events
        MinecraftForge.EVENT_BUS.register(this); // Server/Game events
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        Network.registerMessages();
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        // TODO: Client setup class for client-specific stuff
        Network.activateClientHandlers();
        InitContainerTypes.registerScreenFactories();
        ClientRegistry.registerEntityShader(TownBuilderCameraEntity.class,
                                            new ResourceLocation(TownBuilder.MOD_ID, "shaders/post/blueprint.json"));

        MinecraftForge.EVENT_BUS.register(ShaderHelper.class);
    }

    @SubscribeEvent
    public void onServerStarting(final FMLServerStartingEvent event) {
        Network.activateServerHandlers();
    }
}
