package jakojaannos.townbuilder.client.handler;

import jakojaannos.townbuilder.client.settings.ModKeyConflictContext;
import lombok.val;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class KeyInputEventHandler {
    @SubscribeEvent
    public static void onKeyInputEvent(final InputEvent.KeyInputEvent event) {
        if (!ModKeyConflictContext.BUILDER.isActive()) {
            return;
        }

        val input = InputMappings.getInputByCode(event.getKey(), event.getScanCode());
        KeyBinding.KEYBIND_ARRAY.values()
                                .stream()
                                .filter(kb -> kb.getKeyConflictContext() == ModKeyConflictContext.BUILDER)
                                .filter(kb -> kb.matchesKey(event.getKey(), event.getScanCode()))
                                .findFirst()
                                .ifPresent(kb -> {
                                    if (event.getAction() == GLFW.GLFW_RELEASE) {
                                        KeyBinding.setKeyBindState(input, false);
                                    } else {
                                        KeyBinding.setKeyBindState(input, true);
                                        KeyBinding.onTick(input);
                                    }
                                });
    }
}
