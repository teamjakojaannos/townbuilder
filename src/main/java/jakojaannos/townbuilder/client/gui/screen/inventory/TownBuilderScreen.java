package jakojaannos.townbuilder.client.gui.screen.inventory;

import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class TownBuilderScreen extends ContainerScreen<TownBuilderContainer> {
    public TownBuilderScreen(
        TownBuilderContainer container,
        PlayerInventory playerInventory,
        ITextComponent textComponent
    ) {
        super(container, playerInventory, textComponent);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    }
}
