package net.lightglow.cosmlib.common.reg;

import net.lightglow.cosmlib.CosmeticLib;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class TagsInit {
    public static class Items {
        public static final TagKey<Item> COSMETICS = createTag("cosmetics");
        private static TagKey<Item> createTag(String name){
            return TagKey.of(RegistryKeys.ITEM, CosmeticLib.id(name));
        }
    }
}
