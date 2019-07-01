package jakojaannos.townbuilder.entity;

import jakojaannos.townbuilder.TownBuilder;
import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

@Log4j2
@ObjectHolder(TownBuilder.MOD_ID)
public class ModEntityTypes {
    public static final EntityType<TownBuilderCameraEntity> TOWN_BUILDER_CAMERA = null;
}
