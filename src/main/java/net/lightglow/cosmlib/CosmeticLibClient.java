package net.lightglow.cosmlib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.lightglow.cosmlib.client.model.ClothingModel;
import net.lightglow.cosmlib.client.model.SlimClothingModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class CosmeticLibClient implements ClientModInitializer {
    public static final EntityModelLayer CLOTHING_MODEL_LAYER = new EntityModelLayer(CosmeticLib.id("clothing"), "main");
    public static final EntityModelLayer SLIM_CLOTHING_MODEL_LAYER = new EntityModelLayer(CosmeticLib.id("slim_clothing"), "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(CLOTHING_MODEL_LAYER, ClothingModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SLIM_CLOTHING_MODEL_LAYER, SlimClothingModel::getTexturedModelData);
    }
}
