package io.grasscutter.game.data.excel;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.game.data.common.ItemUseData;
import io.grasscutter.game.inventory.use.UseAction;
import io.grasscutter.utils.definitions.game.WeaponProperty;
import io.grasscutter.utils.enums.game.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;

@Resource(
        name = {
            "MaterialExcelConfigData.json", // Materials.
            "WeaponExcelConfigData.json", // Weapons.
            "ReliquaryExcelConfigData.json", // Artifacts.
            "HomeWorldFurnitureExcelConfigData.json"
        }) // Furniture.
@Getter
public final class ItemData implements GameResource {
    /*
     * Item details.
     */
    @Getter(onMethod = @__(@Override))
    private int id;

    @SerializedName("stackLimit")
    private int maxStack;

    @SerializedName("maxUseCount")
    private int maxUses;

    @SerializedName("rankLevel")
    private int rarity;

    private int gadgetId;

    /*
     * Additional details.
     */
    @SerializedName("destroyReturnMaterial")
    private int[] destroyMaterials;

    @SerializedName("destroyReturnMaterialCount")
    private int[] destroyMaterialCounts;

    @SerializedName("nameTextMapHash")
    private long nameHash; // The hash of the item's name.

    /*
     * Item types.
     */
    private ItemType itemType = ItemType.ITEM_NONE;
    private MaterialType materialType = MaterialType.MATERIAL_NONE;
    private EquipType equipType = EquipType.EQUIP_NONE;
    private String effectType; // Unused.
    private DestroyRule destroyRule = DestroyRule.DESTROY_NONE;

    /*
     * Food data.
     */
    private String foodQuality;

    @SerializedName("satiationParams")
    private int[] satiation;

    /*
     * Interactable items.
     */
    @SerializedName("useTarget")
    private ItemUseTarget useReason = ItemUseTarget.ITEM_USE_TARGET_NONE;

    @SerializedName("itemUse")
    private List<ItemUseData> useData;

    @SerializedName("itemUseActions")
    private List<UseAction> useActions;

    private boolean useOnGain = false;

    /*
     * Artifact data.
     */
    @SerializedName("mainPropDepotId")
    private int mainProperty; // These are depot IDs.

    @SerializedName("appendPropDepotId")
    private int newProperty; // These are depot IDs.

    @SerializedName("appendPropNum")
    private int otherProperties;

    private int setId; // ID of the artifact set.

    @SerializedName("addPropLevels")
    private Set<Integer> propertyLevels; // Levels of which a new property should be added.

    @SerializedName("baseConvExp")
    private int
            conversionExperience; // Experience gained when adding to another artifact. (base value)

    private int maxLevel; // Maximum level of the artifact.

    /*
     * Weapon data.
     */
    private int weaponPromoteId; // This is the same as the item's ID.
    private int weaponBaseExp;
    /** This is the same as {@link ItemData#conversionExperience}. */
    private int storyId; // Unused.

    private int avatarPromoteId; // Unused.

    @SerializedName("awakenMaterial")
    private int ascensionMaterialId; // The ID of the material needed to ascend the weapon.

    @SerializedName("awakenCosts")
    private int[] ascensionCosts; // The different Mora costs of ascending the item. (based on level)

    @SerializedName("skillAffix")
    private int[] weaponSkills; // The IDs of the weapon's skills.

    @SerializedName("weaponProp")
    private List<WeaponProperty> baseWeaponStats; // The data for the weapon's statistics.

    /*
     * Furniture data.
     */
    private int comfort;

    @SerializedName("furnType")
    private List<Integer> furnitureType;

    @SerializedName("furnitureGadgetID")
    private List<Integer> furnitureGadgetIds;

    @SerializedName(
            value = "roomSceneId",
            alternate = {"BMEPAMCNABE", "DANFGGLKLNO", "JFDLJGDFIGL", "OHIANNAEEAK", "MFGACDIOHGF"})
    private int placementSceneId; // The ID of the scene in which the item can be placed.

    @Override
    public void onLoad() {
        // Check the item type.
        if (this.getItemType() == ItemType.ITEM_WEAPON) this.equipType = EquipType.EQUIP_WEAPON;
        else if (this.getItemType() != ItemType.ITEM_RELIQUARY) this.equipType = EquipType.EQUIP_NONE;

        // Validate the weapon base stats.
        if (this.getBaseWeaponStats() != null)
            this.getBaseWeaponStats().removeIf(property -> property.getProperty() == null);

        // Check furniture data.
        if (this.getFurnitureType() != null) this.getFurnitureType().removeIf(type -> type == 0);
        if (this.getFurnitureGadgetIds() != null) this.getFurnitureGadgetIds().removeIf(id -> id == 0);

        // Check the material type.
        if (this.getMaterialType() == null) this.materialType = MaterialType.MATERIAL_NONE;

        // Parse the item use actions.
        if (this.getUseData() != null && !this.getUseData().isEmpty())
            this.useActions =
                    this.getUseData().stream()
                            .filter(data -> data.getUseOp() != ItemUseReason.ITEM_USE_NONE)
                            .map(UseAction::fetch)
                            .filter(Objects::nonNull)
                            .toList();
    }

    /**
     * Checks if this item is equippable by an avatar.
     *
     * @return True if the item is an artifact or weapon.
     */
    public boolean isEquip() {
        return this.itemType == ItemType.ITEM_RELIQUARY || this.itemType == ItemType.ITEM_WEAPON;
    }
}
