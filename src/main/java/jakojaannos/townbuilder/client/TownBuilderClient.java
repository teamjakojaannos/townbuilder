package jakojaannos.townbuilder.client;

import jakojaannos.townbuilder.TownBuilder;
import jakojaannos.townbuilder.client.entity.ClientTownBuilderCameraEntity;
import jakojaannos.townbuilder.client.handler.KeyInputEventHandler;
import jakojaannos.townbuilder.client.settings.ModGameSettings;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.init.InitContainerTypes;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TownBuilderClient {
    @Getter private final ModGameSettings gameSettings;

    public TownBuilderClient() {
        InitContainerTypes.registerScreenFactories();
        ClientRegistry.registerEntityShader(ClientTownBuilderCameraEntity.class,
                                            new ResourceLocation(TownBuilder.MOD_ID, "shaders/post/blueprint.json"));

        MinecraftForge.EVENT_BUS.register(ShaderHelper.class);
        MinecraftForge.EVENT_BUS.register(KeyInputEventHandler.class);

        this.gameSettings = new ModGameSettings();
    }
}
