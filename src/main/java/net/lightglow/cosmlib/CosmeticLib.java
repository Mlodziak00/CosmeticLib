package net.lightglow.cosmlib;

import net.fabricmc.api.ModInitializer;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;

import net.fabricmc.fabric.api.util.TriState;
import net.lightglow.cosmlib.common.reg.TagsInit;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Optional;

public class CosmeticLib implements ModInitializer {
	public static final String MOD_ID = "cosmetic-lib";

	@Override
	public void onInitialize() {
		EquipmentSlot slot = EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, 2);
		TrinketsApi.registerTrinketPredicate(id(slot.getName()), ((itemStack, slotReference, livingEntity) -> {
			if (itemStack.isIn(TagsInit.Items.COSMETICS)){
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}));
	}
	public static ItemStack getCosmeticArmor(LivingEntity entity, EquipmentSlot slot) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(entity);
		if(component.isPresent()) {
			List<Pair<SlotReference, ItemStack>> list = component.get().getEquipped(stack -> stack.isIn(TagsInit.Items.COSMETICS));
			for(Pair<SlotReference, ItemStack> equipped : list) {
				SlotType slotType = equipped.getLeft().inventory().getSlotType();
				if(!slotType.getName().equals("clothes")) {
					continue;
				}
				if(!slotType.getGroup().equalsIgnoreCase(slot.getName())) {
					continue;
				}
				return equipped.getRight();
			}
		}
		return ItemStack.EMPTY;
	}

	public static Identifier id(String name){
		return new Identifier(MOD_ID, name);
	}

}