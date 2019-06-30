package jakojaannos.townbuilder.init;

import com.google.common.collect.Lists;
import jakojaannos.townbuilder.TownBuilder;
import jakojaannos.townbuilder.block.TownBuilderBlock;
import lombok.extern.log4j.Log4j2;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Function;

@Log4j2
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
class InitBlocks {
    private static final List<Tuple<Block, BlockItem>> blocks = Lists.newArrayList(
        create("town_builder", new TownBuilderBlock(Block.Properties.create(Material.ROCK)))
    );

    private static Tuple<Block, BlockItem> create(String key, Block block) {
        return create(key, block, b -> (BlockItem) new BlockItem(b, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(b.getRegistryName()));
    }

    private static Tuple<Block, BlockItem> create(
        String key,
        Block block,
        Function<Block, BlockItem> blockItemFactory
    ) {
        block.setRegistryName(TownBuilder.MOD_ID, key);
        return new Tuple<>(block, blockItemFactory.apply(block));
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        LOGGER.debug("Registering Blocks");
        blocks.forEach(tuple -> event.getRegistry().register(tuple.getA()));
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        LOGGER.debug("Registering BlockItems");
        blocks.forEach(tuple -> event.getRegistry().register(tuple.getB()));
    }
}
