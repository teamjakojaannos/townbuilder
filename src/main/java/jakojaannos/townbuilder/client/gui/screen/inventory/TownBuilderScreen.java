package jakojaannos.townbuilder.client.gui.screen.inventory;

import jakojaannos.townbuilder.block.ModBlocks;
import jakojaannos.townbuilder.client.ShaderHelper;
import jakojaannos.townbuilder.client.entity.ClientTownBuilderCameraEntity;
import jakojaannos.townbuilder.inventory.container.TownBuilderContainer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Log4j2
@OnlyIn(Dist.CLIENT)
public class TownBuilderScreen extends Screen implements IHasContainer<TownBuilderContainer> {
    private static final double BUILDER_RAY_LENGTH = 255.0;

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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return true;
        }

        val mainWindow = Minecraft.getInstance().mainWindow;
        double windowWidth = mainWindow.getScaledWidth();
        double windowHeight = mainWindow.getScaledHeight();

        val screenRatio = windowWidth / windowHeight;
        val screenHeightInWorld = 2 * 16.0;
        val screenWidthInWorld = screenRatio * screenHeightInWorld;

        val normalizedMouseX = (mouseX / windowWidth) - 0.5;
        val normalizedMouseY = (mouseY / windowHeight) - 0.5;
        val mouseXInWorld = normalizedMouseX * screenWidthInWorld;
        val mouseYInWorld = normalizedMouseY * screenHeightInWorld;

        val upVector = Vec3d.fromPitchYaw(cameraEntity.rotationPitch - 90.0f, cameraEntity.rotationYaw);
        val rightVector = Vec3d.fromPitchYaw(0.0f, cameraEntity.rotationYaw - 90.0f);

        val startPoint = cameraEntity.getPositionVector()
                                     .add(upVector.scale(-mouseYInWorld))
                                     .add(rightVector.scale(-mouseXInWorld));
        val endPoint = startPoint.add(cameraEntity.getForward().scale(BUILDER_RAY_LENGTH));
        RayTraceContext context = new RayTraceContext(startPoint,
                                                      endPoint,
                                                      RayTraceContext.BlockMode.COLLIDER,
                                                      RayTraceContext.FluidMode.NONE,
                                                      cameraEntity);

        val world = cameraEntity.world;
        val traceResult = world.rayTraceBlocks(context);
        if (traceResult.getType() != RayTraceResult.Type.MISS) {
            val pos = traceResult.getPos();
            world.setBlockState(pos.up(), ModBlocks.MYTESTBLOCK.getDefaultState());
        }

        return true;
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
