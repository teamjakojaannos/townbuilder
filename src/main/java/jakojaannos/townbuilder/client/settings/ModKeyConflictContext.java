package jakojaannos.townbuilder.client.settings;

import jakojaannos.townbuilder.client.gui.screen.inventory.TownBuilderScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.IKeyConflictContext;

public enum ModKeyConflictContext implements IKeyConflictContext {
    BUILDER {
        @Override
        public boolean isActive() {
            return Minecraft.getInstance().currentScreen instanceof TownBuilderScreen;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this;
        }
    }
}
