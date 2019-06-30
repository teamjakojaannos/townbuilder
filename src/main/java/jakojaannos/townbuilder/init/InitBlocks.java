package jakojaannos.townbuilder.init;

import jakojaannos.townbuilder.TownBuilder;
import jakojaannos.townbuilder.block.TownBuilderBlock;
import lombok.extern.log4j.Log4j2;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Log4j2
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
class InitBlocks {
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        LOGGER.debug("Registering Blocks");
        event.getRegistry().register(create("townbuilder", new TownBuilderBlock(Block.Properties.create(Material.ROCK))));
    }

    private static Block create(String key, Block block) {
        block.setRegistryName(TownBuilder.MOD_ID, key);
        return block;
    }
}
