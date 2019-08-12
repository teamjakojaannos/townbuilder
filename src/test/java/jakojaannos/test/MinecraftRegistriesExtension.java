package jakojaannos.test;

import lombok.NoArgsConstructor;
import net.minecraft.util.registry.Bootstrap;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

@NoArgsConstructor
public class MinecraftRegistriesExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Bootstrap.register();
    }
}
