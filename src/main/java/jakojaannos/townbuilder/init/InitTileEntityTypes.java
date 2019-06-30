package jakojaannos.townbuilder.init;

import jakojaannos.townbuilder.TownBuilder;
import jakojaannos.townbuilder.block.ModBlocks;
import jakojaannos.townbuilder.tileentity.TownBuilderTileEntity;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Log4j2
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
class InitTileEntityTypes {
    @SubscribeEvent
    public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        LOGGER.debug("Registering TileEntityTypes");
        if (ModBlocks.TOWN_BUILDER == null) {
            LOGGER.warn("Blocks are not registered yet! Valid blocks won't be populated correctly!");
        }

        LOGGER.debug("Registering TileEntityTypes");
        event.getRegistry().register(create("townbuilder", TileEntityType.Builder.create(TownBuilderTileEntity::new/*, ModBlocks.TOWN_BUILDER*/)));
    }

    private static <T extends TileEntity> TileEntityType<T> create(String key, TileEntityType.Builder<T> builder) {
        val type = builder.build(null);
        type.setRegistryName(TownBuilder.MOD_ID, key);
        return type;
    }
}
