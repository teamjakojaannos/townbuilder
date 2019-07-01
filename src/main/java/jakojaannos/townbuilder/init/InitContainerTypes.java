package jakojaannos.townbuilder.init;

import jakojaannos.townbuilder.TownBuilder;
import jakojaannos.townbuilder.client.gui.screen.inventory.TownBuilderScreen;
import jakojaannos.townbuilder.inventory.container.ModContainerTypes;
import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Log4j2
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitContainerTypes {
    @SubscribeEvent
    public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().registerAll(
            register("town_builder", TownBuilderContainer::new)
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreenFactories() {
        ScreenManager.registerFactory(ModContainerTypes.TOWN_BUILDER, TownBuilderScreen::new);
    }

    private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory) {
        val type = new ContainerType<T>(factory);
        type.setRegistryName(TownBuilder.MOD_ID, key);
        return type;
    }
}
