package jakojaannos.townbuilder.client;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.ChunkRender;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ShaderHelper {
    private static Framebuffer depthFramebuffer;
    private static int renderDistance = 2;

    public static boolean hasDepthFramebuffer() {
        return depthFramebuffer != null;
    }

    public static void applyDepthTextureFramebuffer(Framebuffer framebuffer) {
        ShaderHelper.depthFramebuffer = framebuffer;
    }

    public static void clearDepthTextureFramebuffer() {
        ShaderHelper.depthFramebuffer = null;
    }

    @SubscribeEvent
    public static void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (!hasDepthFramebuffer()) {
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

    // FIXME: CameraSetupEvent is not yet run on 1.14, RenderFog is as close as we can get
    @SubscribeEvent
    public static void onRenderFog(final EntityViewRenderEvent.RenderFogEvent event) {
        if (depthFramebuffer == null) {
            return;
        }

        val mc = Minecraft.getInstance();
        val screenWidth = (float) mc.mainWindow.getScaledWidth();
        val screenHeight = (float) mc.mainWindow.getScaledHeight();
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
