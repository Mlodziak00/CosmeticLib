package net.lightglow.cosmlib.mixin;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.lightglow.cosmlib.CosmeticLib;
import net.lightglow.cosmlib.client.model.ClothingModel;
import net.lightglow.cosmlib.client.model.SlimClothingModel;
import net.lightglow.cosmlib.client.renderer.feature.ClothingFeature;
import net.lightglow.cosmlib.common.item.ClothingItem;
import net.lightglow.cosmlib.common.reg.TagsInit;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	@Shadow protected abstract void setModelPose(AbstractClientPlayerEntity player);

	@Unique
	private final ClothingModel clothingModel;

	@Unique
	private boolean getSlim;


	public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius, ClothingModel clothingModel, boolean getSlim) {
		super(ctx, model, shadowRadius);
		this.clothingModel = clothingModel;
		this.getSlim = getSlim;
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
		this.addFeature(new ClothingFeature(this, ctx.getModelLoader(), slim));
		getSlim = slim;
	}

	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
	at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), cancellable = true)
	private void hideUnNeededParts$render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
		setVisible(abstractClientPlayerEntity);
	}
	@Unique
	protected void setVisible(LivingEntity entity) {
			TrinketsApi.getTrinketComponent(entity).ifPresent(trinketComponent -> {
				EquipmentSlot slot = EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, 2);
				ItemStack cosmeticStack = CosmeticLib.getCosmeticArmor(entity, slot);
				List<Pair<SlotReference, ItemStack>> res = trinketComponent.getEquipped(itemStack -> itemStack.isIn(TagsInit.Items.COSMETICS));
				if (res.size() == 0){
					this.model.jacket.visible = true;
					this.model.rightSleeve.visible = true;
					this.model.leftSleeve.visible = true;
					this.model.rightPants.visible = true;
					this.model.leftPants.visible = true;
					this.model.hat.visible = true;
				} else {
					this.model.jacket.visible = false;
					this.model.rightSleeve.visible = false;
					this.model.leftSleeve.visible = false;
					this.model.rightPants.visible = false;
					this.model.leftPants.visible = false;
					if (cosmeticStack.getItem() instanceof ClothingItem clothingItem){
						if (!clothingItem.getIsHatLayerHidden()){
							this.model.hat.visible = false;
						}
					}
				}
			});

	}
	@Inject(method = "renderLeftArm", at = @At("TAIL"))
	private void renderLArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci){
		this.renderClothingArm(matrices, vertexConsumers, light, player, ((PlayerEntityModel)this.model).leftArm, ((PlayerEntityModel)this.model).leftSleeve);
	}
	@Inject(method = "renderRightArm", at = @At("TAIL"))
	private void renderRArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci){
		this.renderClothingArm(matrices, vertexConsumers, light, player, ((PlayerEntityModel)this.model).rightArm, ((PlayerEntityModel)this.model).rightSleeve);
	}

	@Unique
	private void renderClothingArm(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity player, ModelPart cArm, ModelPart sleeve){
		PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = (PlayerEntityModel)this.getModel();
		this.setModelPose(player);
		playerEntityModel.handSwingProgress = 0.0F;
		playerEntityModel.sneaking = false;
		playerEntityModel.leaningPitch = 0.0F;
		playerEntityModel.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		EquipmentSlot slot = EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, 2);
		ItemStack cosmeticStack = CosmeticLib.getCosmeticArmor(player, slot);
		cArm.pitch = 0.0F;
		sleeve.pitch = 0.0F;
		if (!cosmeticStack.isEmpty()) {
			if (cosmeticStack.getItem() instanceof ClothingItem clothingItem) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getArmorCutoutNoCull(new Identifier(clothingItem.modID, "textures/clothing/" + isSlim(getSlim) + clothingItem.getClothTexture() + ".png")));
				if (getSlim){
					cArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
					sleeve.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
				} else {
					cArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
					sleeve.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
				}

			}
		}
		//cArm.render(matrices, vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(CosmeticLib.id("textures/clothing/example_clothing.png"))), light, OverlayTexture.DEFAULT_UV);
	}

	@Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1))
	public void renderArmSleeve(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci){
		EquipmentSlot slot = EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, 2);
		ItemStack cosmeticStack = CosmeticLib.getCosmeticArmor(player, slot);
		sleeve.visible = cosmeticStack.isEmpty();
	}
	@Unique
	public String isSlim(boolean slim){
		if (slim){
			return "slim_";
		} else {
			return "";
		}
	}
}