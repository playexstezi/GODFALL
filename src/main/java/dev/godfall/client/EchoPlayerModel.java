package dev.godfall.client;

import dev.godfall.entity.EchoEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class EchoPlayerModel extends PlayerModel<EchoEntity> {
    public EchoPlayerModel(ModelPart root, boolean slimArms) {
        super(root, slimArms);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer consumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        super.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                packedOverlay,
                red * 0.38F,
                green * 0.78F,
                blue,
                alpha * 0.68F
        );
    }
}
