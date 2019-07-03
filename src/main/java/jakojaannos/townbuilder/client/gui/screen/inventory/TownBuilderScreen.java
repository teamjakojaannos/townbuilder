package jakojaannos.townbuilder.client.gui.screen.inventory;

import jakojaannos.townbuilder.client.ShaderHelper;
import jakojaannos.townbuilder.entity.TownBuilderCameraEntity;
import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Log4j2
@OnlyIn(Dist.CLIENT)
public class TownBuilderScreen extends ContainerScreen<TownBuilderContainer> {
    private static int cameraEntityId = -1;
    private int oldThirdPersonView = 0;

    public static void receiveCameraEntityId(int entityId) {
        cameraEntityId = entityId;
    }

    private final World world;

    private TownBuilderCameraEntity cameraEntity;
    private boolean cameraActive;

    public TownBuilderScreen(
            TownBuilderContainer container,
            PlayerInventory playerInventory,
            ITextComponent textComponent
    ) {
        super(container, playerInventory, textComponent);
        this.world = playerInventory.player.world;
    }

    @Override
    public void tick() {
        activateCamera();
        super.tick();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void removed() {
        super.removed();
        disableCamera();
    }

    private void activateCamera() {
        if (minecraft == null) {
            throw LOGGER.throwing(new IllegalStateException("activateCamera() called too early!"));
        }

        if (cameraActive || cameraEntityId == -1) {
            return;
        }

        val entity = world.getEntityByID(cameraEntityId);
        if (entity == null) {
            LOGGER.warn("Camera entity ID was set but could not find entity with given ID.");
            cameraEntityId = -1;
            return;
        }

        if (!(entity instanceof TownBuilderCameraEntity)) {
            LOGGER.error("Received camera entity ID but the entity is of wrong type \"{}\"", entity.getType());
            cameraEntityId = -1;
            return;
        }

        cameraActive = true;
        cameraEntity = (TownBuilderCameraEntity) entity;
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
        cameraEntityId = -1;
        cameraEntity = null;
        ShaderHelper.clearDepthTextureFramebuffer();
        if (minecraft != null) {
            minecraft.renderViewEntity = null;
            minecraft.gameSettings.thirdPersonView = oldThirdPersonView;
        } else {
            LOGGER.error("Minecraft instance was null when disabling town builder camera!");
        }
    }
}
