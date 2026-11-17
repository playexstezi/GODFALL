package dev.godfall.client;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.godfall.entity.EchoEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

public final class EchoRenderer extends LivingEntityRenderer<EchoEntity, EchoPlayerModel> {
    private final EchoPlayerModel wideModel;
    private final EchoPlayerModel slimModel;

    public EchoRenderer(EntityRendererProvider.Context context) {
        super(context, new EchoPlayerModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.28F);
        this.wideModel = this.model;
        this.slimModel = new EchoPlayerModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);

        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new EchoPlayerModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR), false),
                new EchoPlayerModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR), false),
                context.getModelManager()
        ));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public void render(
            EchoEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        this.model = entity.hasSlimArms() ? slimModel : wideModel;
        this.model.setAllVisible(true);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    protected RenderType getRenderType(
            EchoEntity entity,
            boolean bodyVisible,
            boolean translucent,
            boolean glowing
    ) {
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }

    @Override
    public ResourceLocation getTextureLocation(EchoEntity entity) {
        GameProfile profile = entity.getOwnerProfile();
        PlayerSkin skin = Minecraft.getInstance().getSkinManager().getInsecureSkin(profile);
        return skin.texture();
    }

    @Override
    protected boolean shouldShowName(EchoEntity entity) {
        return false;
    }
}
