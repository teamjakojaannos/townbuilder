package jakojaannos.townbuilder.client.settings;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class ModGameSettings {
    private static final String CATEGORY_BUILDER = "key.townbuilder.categories.builder";

    public final KeyBinding keyBindBuilderMoveLeft = new KeyBinding("key.townbuilder.builder.moveLeft", ModKeyConflictContext.BUILDER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_W, CATEGORY_BUILDER);
    public final KeyBinding keyBindBuilderMoveRight = new KeyBinding("key.townbuilder.builder.moveRight", ModKeyConflictContext.BUILDER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_W, CATEGORY_BUILDER);
    public final KeyBinding keyBindBuilderMoveForward = new KeyBinding("key.townbuilder.builder.moveForward", ModKeyConflictContext.BUILDER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_W, CATEGORY_BUILDER);
    public final KeyBinding keyBindBuilderMoveBack = new KeyBinding("key.townbuilder.builder.moveBack", ModKeyConflictContext.BUILDER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_W, CATEGORY_BUILDER);
    public final KeyBinding keyBindBuilderRotateLeft = new KeyBinding("key.townbuilder.builder.rotateLeft", ModKeyConflictContext.BUILDER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_W, CATEGORY_BUILDER);
    public final KeyBinding keyBindBuilderRotateRight = new KeyBinding("key.townbuilder.builder.rotateRight", ModKeyConflictContext.BUILDER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_W, CATEGORY_BUILDER);

    public ModGameSettings() {
        ClientRegistry.registerKeyBinding(keyBindBuilderMoveLeft);
        ClientRegistry.registerKeyBinding(keyBindBuilderMoveRight);
        ClientRegistry.registerKeyBinding(keyBindBuilderMoveForward);
        ClientRegistry.registerKeyBinding(keyBindBuilderMoveBack);
        ClientRegistry.registerKeyBinding(keyBindBuilderRotateLeft);
        ClientRegistry.registerKeyBinding(keyBindBuilderRotateRight);
    }
}
