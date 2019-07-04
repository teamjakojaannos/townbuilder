package jakojaannos.townbuilder.init;

import jakojaannos.townbuilder.TownBuilder;
import jakojaannos.townbuilder.client.entity.ClientTownBuilderCameraEntity;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Log4j2
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
class InitEntityTypes {
    @SubscribeEvent
    public static void onEntityTypeRegistry(final RegistryEvent.Register<EntityType<?>> event) {
        LOGGER.debug("Registering EntityTypes");

        event.getRegistry()
             .register(create("town_builder_camera",
                              EntityType.Builder.create(EntityClassification.MISC)
                                                .setCustomClientFactory((spawnEntity, world) -> new ClientTownBuilderCameraEntity(world))
                                                .immuneToFire()
                                                .disableSummoning()
                                                .disableSerialization()
                                                .size(0.1f, 0.1f)));
    }

    private static <T extends Entity> EntityType<T> create(String key, EntityType.Builder<T> builder) {
        val type = builder.build(TownBuilder.MOD_ID + ":" + key);
        type.setRegistryName(TownBuilder.MOD_ID, key);
        return type;
    }
}
