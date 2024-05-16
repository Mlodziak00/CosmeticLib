package net.lightglow.cosmlib.common.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ClothingItem extends TrinketItem {
    public final String namespace;
    public final String texture;
    public ClothingItem(Settings settings, String namespace, String texture) {
        super(settings);
        this.namespace = namespace;
        this.texture = texture;
    }

    public String getClothTexture() {
        return this.texture;
    }


}
