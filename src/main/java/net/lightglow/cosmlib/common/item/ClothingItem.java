package net.lightglow.cosmlib.common.item;

import dev.emi.trinkets.api.TrinketItem;

public class ClothingItem extends TrinketItem {
    public final String modID;
    public final String texture;
    public final boolean hideHatLayer;
    public ClothingItem(String modID, String texture, Boolean hideHatLayer, Settings settings) {
        super(settings);
        this.modID = modID;
        this.texture = texture;
        this.hideHatLayer = hideHatLayer;
    }

    public String getClothTexture() {
        return this.texture;
    }

    public boolean getIsHatLayerHidden(){return this.hideHatLayer;}


}
