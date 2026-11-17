package dev.godfall.registry;

import dev.godfall.Godfall;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Godfall.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityAttributes {
    private ModEntityAttributes() {
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.ECHO.get(),
                Mob.createMobAttributes()
                        .add(Attributes.MAX_HEALTH, 20.0D)
                        .add(Attributes.MOVEMENT_SPEED, 0.23D)
                        .add(Attributes.FOLLOW_RANGE, 24.0D)
                        .build()
        );
    }
}
