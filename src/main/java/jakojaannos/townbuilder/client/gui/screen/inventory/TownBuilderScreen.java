package jakojaannos.townbuilder.client.gui.screen.inventory;

import jakojaannos.townbuilder.client.ShaderHelper;
import jakojaannos.townbuilder.client.entity.ClientTownBuilderCameraEntity;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Log4j2
@OnlyIn(Dist.CLIENT)
public class TownBuilderScreen extends Screen implements IHasContainer<TownBuilderContainer> {
    private static ClientTownBuilderCameraEntity cameraEntity;
    private int oldThirdPersonView = 0;

    public static void receiveCameraEntity(ClientTownBuilderCameraEntity cameraEntity) {
        TownBuilderScreen.cameraEntity = cameraEntity;
    }

    @Getter private final TownBuilderContainer container;

    private boolean cameraActive;

    public TownBuilderScreen(
            TownBuilderContainer container,
            PlayerInventory playerInventory,
            ITextComponent textComponent
    ) {
        super(textComponent);
        this.container = container;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        activateCamera();
        super.tick();
        if (!minecraft.player.isAlive() || minecraft.player.removed) {
            this.minecraft.player.closeScreen();
        }
    }

    @Override
    public void removed() {
        if (this.minecraft.player != null) {
            this.container.onContainerClosed(this.minecraft.player);
        }
        disableCamera();
    }

    private void activateCamera() {
        if (minecraft == null) {
            throw LOGGER.throwing(new IllegalStateException("activateCamera() called too early!"));
        }

        if (cameraActive || cameraEntity == null) {
            return;
        }

        cameraActive = true;
        minecraft.setRenderViewEntity(cameraEntity);
        oldThirdPersonView = minecraft.gameSettings.thirdPersonView;
        minecraft.gameSettings.thirdPersonView = 1;

        val loadedShader = minecraft.gameRenderer.getShaderGroup();
        if (loadedShader != null) {
            ShaderHelper.applyDepthTextureFramebuffer(loadedShader.getFramebufferRaw("townbuilder:depth"));
        }
    }

    private void disableCamera() {
        cameraActive = false;
        cameraEntity = null;
        ShaderHelper.clearDepthTextureFramebuffer();
        if (minecraft != null) {
            minecraft.renderViewEntity = null;
            minecraft.gameSettings.thirdPersonView = oldThirdPersonView;
        } else {
            LOGGER.error("Minecraft instance was null when disabling town builder camera!");
        }
        KeyBinding.unPressAllKeys();
    }
}
