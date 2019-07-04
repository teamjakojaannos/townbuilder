package jakojaannos.townbuilder.client;

import jakojaannos.townbuilder.TownBuilder;
import jakojaannos.townbuilder.client.settings.ModGameSettings;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.init.InitContainerTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TownBuilderClient {
    private final ModGameSettings gameSettings;

    public TownBuilderClient() {
        InitContainerTypes.registerScreenFactories();
        ClientRegistry.registerEntityShader(TownBuilderCameraEntity.class,
                                            new ResourceLocation(TownBuilder.MOD_ID, "shaders/post/blueprint.json"));

        MinecraftForge.EVENT_BUS.register(ShaderHelper.class);

        this.gameSettings = new ModGameSettings();
    }
}
