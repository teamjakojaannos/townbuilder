package jakojaannos.townbuilder.client.gui.screen.inventory;

import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TownBuilderScreen extends ContainerScreen<TownBuilderContainer> {
    public static TownBuilderScreen instance;
    public static TownBuilderCameraEntity cameraEntity;

    private boolean cameraActive;

    public TownBuilderScreen(
        TownBuilderContainer container,
        PlayerInventory playerInventory,
        ITextComponent textComponent
    ) {
        super(container, playerInventory, textComponent);
        instance = this;

        activateCamera();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void onClose() {
        disableCamera();
        super.onClose();
    }

    private void activateCamera() {
        if (cameraActive || cameraEntity == null) {
            return;
        }

        cameraActive = true;
        Minecraft.getInstance().renderViewEntity = cameraEntity;
    }

    private void disableCamera() {
        cameraActive = false;
        Minecraft.getInstance().renderViewEntity = null;
    }
}
