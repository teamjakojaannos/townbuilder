package jakojaannos.townbuilder.client;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ShaderHelper {
    private static final String CAMERA_POSITION_UNIFORM_NAME = "CamPos";
    private static final String CAMERA_ROTATION_UNIFORM_NAME = "CamRot";
    private static Framebuffer depthFramebuffer;
    private static int renderDistance = 2;

    public static boolean isInBuildMode() {
        return depthFramebuffer != null;
    }

    // TODO: Actually detect whether or not passing/copying depthbuffer is necessary. Should be doable by iterating over
    //       active ShaderGroup's defined target framebuffers and determine if "townbuilder:depth" is there
    public static boolean shaderRequiresDepthBuffer() {
        return isInBuildMode();
    }

    public static void applyDepthTextureFramebuffer(Framebuffer framebuffer) {
        ShaderHelper.depthFramebuffer = framebuffer;
    }

    public static void clearDepthTextureFramebuffer() {
        ShaderHelper.depthFramebuffer = null;
    }

    @SubscribeEvent
    public static void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!Minecraft.getInstance().gameRenderer.isShaderActive()) {
            return;
        }

        if (shaderRequiresDepthBuffer()) {
            blitDepthBuffer();
        }

        Minecraft.getInstance()
                .gameRenderer
                .getShaderGroup()
                .listShaders
                .stream()
                .map(Shader::func_217624_b) // get underlying ShaderInstances
                .forEach(shaderInstance -> applyUniformsToInstance(shaderInstance, event.getPartialTicks()));
    }

    private static void blitDepthBuffer() {
        if (depthFramebuffer == null) {
            return;
        }

        val mainFramebuffer = Minecraft.getInstance().getFramebuffer();
        mainFramebuffer.bindFramebuffer(false);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthFramebuffer.framebufferTexture);
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D,
                              0,
                              GL11.GL_DEPTH_COMPONENT,
                              0,
                              0,
                              depthFramebuffer.framebufferWidth,
                              depthFramebuffer.framebufferHeight,
                              0);
        mainFramebuffer.bindFramebuffer(false);
        GlStateManager.bindTexture(0);
    }

    private static void applyUniformsToInstance(ShaderInstance shaderInstance, float partialTicks) {
        val renderViewEntity = Minecraft.getInstance().renderViewEntity;
        val cameraEntity = renderViewEntity == null
                ? Minecraft.getInstance().player
                : renderViewEntity;

        val camX = MathHelper.lerp(partialTicks, cameraEntity.lastTickPosX, cameraEntity.posX);
        val camY = MathHelper.lerp(partialTicks, cameraEntity.lastTickPosY, cameraEntity.posY);
        val camZ = MathHelper.lerp(partialTicks, cameraEntity.lastTickPosZ, cameraEntity.posZ);

        shaderInstance.func_216538_b(CAMERA_POSITION_UNIFORM_NAME).set((float) camX, (float) camY, (float) camZ);
        shaderInstance.func_216538_b(CAMERA_ROTATION_UNIFORM_NAME).set(cameraEntity.rotationPitch, cameraEntity.rotationYaw);

    }

    // FIXME: CameraSetupEvent is not yet run on 1.14, RenderFog is as close as we can get
    @SubscribeEvent
    public static void onRenderFog(final EntityViewRenderEvent.RenderFogEvent event) {
        if (isInBuildMode()) {
            makeProjectionOrthographic();
        }
    }

    private static void makeProjectionOrthographic() {
        if (depthFramebuffer == null) {
            return;
        }

        val mc = Minecraft.getInstance();
        val screenWidth = (float) mc.mainWindow.getFramebufferWidth();
        val screenHeight = (float) mc.mainWindow.getFramebufferHeight();
        val aspectRatio = screenWidth / screenHeight;

        val height = renderDistance * 16;
        val width = aspectRatio * height;

        val halfWidth = width / 2.0f;
        val halfHeight = height / 2.0f;

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(-halfWidth, halfWidth, -halfHeight, halfHeight, 0.0f, 255.0f);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
    }
}
