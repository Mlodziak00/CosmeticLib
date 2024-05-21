package net.lightglow.cosmlib.client.renderer.feature;

import net.lightglow.cosmlib.CosmeticLib;
import net.lightglow.cosmlib.CosmeticLibClient;
import net.lightglow.cosmlib.client.model.ClothingModel;
import net.lightglow.cosmlib.client.model.SlimClothingModel;
import net.lightglow.cosmlib.common.item.ClothingItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ClothingFeature extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final ClothingModel clothingModel;
    private final SlimClothingModel slimclothingModel;
    private final boolean slim;
    public ClothingFeature(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, EntityModelLoader loader, boolean slim) {
        super(context);
        this.slim = slim;

        this.clothingModel = new ClothingModel(loader.getModelPart(CosmeticLibClient.CLOTHING_MODEL_LAYER), !slim);
        this.slimclothingModel = new SlimClothingModel(loader.getModelPart(CosmeticLibClient.SLIM_CLOTHING_MODEL_LAYER), slim);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        EquipmentSlot slot = EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, 2);
        ItemStack cosmeticStack = CosmeticLib.getCosmeticArmor(entity, slot);
        if (!cosmeticStack.isEmpty()) {
            if (cosmeticStack.getItem() instanceof ClothingItem clothingItem) {
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(new Identifier(clothingItem.modID, "textures/clothing/" + isSlim(slim) + clothingItem.getClothTexture() + ".png")));
                if (slim){
                    SlimClothingModel model = slimclothingModel;
                    getContextModel().copyBipedStateTo(model);
                    model.adjustArmPivots(slim);
                    model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0F);
                } else {
                    ClothingModel model = clothingModel;
                    getContextModel().copyBipedStateTo(model);
                    model.adjustArmPivots(slim);
                    model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0F);
                }
            }
        }
    }
    public String isSlim(boolean slim){
        if (slim){
            return "slim_";
        } else {
            return "";
        }
    }
}
