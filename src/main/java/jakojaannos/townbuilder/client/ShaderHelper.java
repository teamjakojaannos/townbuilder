package jakojaannos.townbuilder.client;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ShaderHelper {
    private static Framebuffer depthFramebuffer;

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
}
