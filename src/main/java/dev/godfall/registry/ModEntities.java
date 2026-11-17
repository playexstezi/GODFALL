package dev.godfall.registry;

import dev.godfall.Godfall;
import dev.godfall.entity.EchoEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Godfall.MOD_ID);

    public static final RegistryObject<EntityType<EchoEntity>> ECHO = ENTITY_TYPES.register(
            "echo",
            () -> EntityType.Builder.<EchoEntity>of(EchoEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(12)
                    .updateInterval(3)
                    .build("echo")
    );

    private ModEntities() {
    }
}
