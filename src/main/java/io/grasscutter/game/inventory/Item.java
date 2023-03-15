package io.grasscutter.game.inventory;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.game.data.GameData;
import io.grasscutter.game.data.excel.ItemData;
import io.grasscutter.player.Player;
import io.grasscutter.proto.*;
import io.grasscutter.proto.AbilitySyncStateInfoOuterClass.AbilitySyncStateInfo;
import io.grasscutter.proto.EquipOuterClass.Equip;
import io.grasscutter.proto.FurnitureOuterClass.Furniture;
import io.grasscutter.proto.ItemHintOuterClass.ItemHint;
import io.grasscutter.proto.ItemParamOuterClass.ItemParam;
import io.grasscutter.proto.MaterialOuterClass.Material;
import io.grasscutter.proto.SceneWeaponInfoOuterClass.SceneWeaponInfo;
import io.grasscutter.proto.WeaponOuterClass.Weapon;
import io.grasscutter.utils.enums.game.ItemType;
import io.grasscutter.utils.interfaces.DatabaseObject;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

/* Item instance. */
@DataSerializable(table = "items")
public final class Item implements DatabaseObject {
    @Special(FieldType.ID)
    @Getter
    private ObjectId id = new ObjectId(); // The item object's unique identifier.

    @Getter private int ownerId = Integer.MAX_VALUE; // The item's owner's user ID.

    @Getter private transient Player owner = null; // The item's owner.
    @Getter private transient long itemGuid = Long.MAX_VALUE; // The item's GUID.
    @Getter @Setter private transient ItemData itemData = null; // The item's data.

    @Getter @Setter private int itemId = Integer.MAX_VALUE; // The item's ID.
    @Getter @Setter private int count = 1; // The item's count.

    /*
     * Equipables.
     */
    @Getter @Setter private int level = 0; // The item's level.
    @Getter @Setter private int experience = 0; // The item's experience. This is sent to the game.

    @Getter @Setter
    private int allocatedExperience = 0; // The item's allocated experience. This is used internally.

    @Getter @Setter private int ascensionLevel = 0; // The item's ascension level.

    @Getter @Setter
    private boolean locked =
            false; // Whether the item is locked. This prevents it from being deleted/used.

    @Getter @Setter private int equippedAvatar; // The avatar ID the item is equipped to.
    @Getter @Setter private transient int entityId; // The entity ID for this item.

    /*
     * Weapon data.
     */
    @Getter private List<Integer> skills; // The item's skills.
    @Getter @Setter private int refinementLevel = 0; // The item's refinement level.

    /*
     * Artifact data.
     */
    @Getter @Setter private int mainPropertyId; // The artifact's main property ID.
    @Getter private List<Integer> otherProperties; // The artifact's other property IDs.

    /** Constructor for de-serialization. */
    public Item() {
        // Empty constructor.
    }

    /**
     * Constructor for creating a new item. Fetches the item's data by the ID. Sets the count to 1.
     *
     * @param itemId The item's ID.
     */
    public Item(int itemId) {
        this(itemId, 1);
    }

    /**
     * Constructor for creating a new item. Fetches the item's data by the ID.
     *
     * @param itemId The item's ID.
     * @param count The item's count.
     */
    public Item(int itemId, int count) {
        this(GameData.getItemDataMap().get(itemId), count);
    }

    /**
     * Constructor for creating a new item. Sets the count to 1.
     *
     * @param itemData The item's data.
     */
    public Item(ItemData itemData) {
        this(itemData, 1);
    }

    /**
     * Constructor for creating a new item.
     *
     * @param itemData The item's data.
     * @param count The item's count.
     */
    public Item(ItemData itemData, int count) {
        this.itemData = itemData;
        this.itemId = itemData.getId();

        switch (itemData.getItemType()) {
            case ITEM_VIRTUAL -> this.setCount(count);
            case ITEM_RELIQUARY -> {
                this.setCount(1);
                this.setLevel(Math.max(count, 1));
                this.otherProperties = new ArrayList<>();

                // Set the item's main property.
                var mainPropData = GameData.getRandomProp(itemData.getMainProperty());
                if (mainPropData != null) this.setMainPropertyId(mainPropData.getId());

                // Create the item's other properties.
                this.addProperties(itemData.getOtherProperties());
            }
            case ITEM_WEAPON -> {
                this.setCount(1);
                this.setLevel(Math.max(count, 1));

                // Add the weapon's skills.
                this.skills = new ArrayList<>(2);
                if (itemData.getWeaponSkills() != null) {
                    for (var skillId : itemData.getWeaponSkills())
                        if (skillId > 0) this.getSkills().add(skillId);
                }
            }

            default -> this.setCount(Math.min(count, itemData.getMaxStack()));
        }
    }

    /**
     * Sets the item's owner. Also sets the item's GUID.
     *
     * @param player The player to set as the owner.
     */
    public void setOwner(Player player) {
        this.owner = player;
        this.ownerId = player.getUserId();
        this.itemGuid = player.getNextGameId();
    }

    /**
     * @return The item's type.
     */
    public ItemType getItemType() {
        return this.getItemData().getItemType();
    }

    /**
     * @return The slot equipment type's value.
     */
    public int getSlot() {
        return this.getItemData().getEquipType().getValue();
    }

    /*
     * Artifact manipulation.
     */

    /**
     * Adds properties to the item. This is used for artifacts.
     *
     * @param count The amount of properties to add.
     */
    public void addProperties(int count) {
        for (var i = 0; i < Math.max(count, 0); i++) this.addProperty();
    }

    /** Adds or upgrades an existing property. */
    public void addProperty() {
        // Check if the properties list exists.
        if (this.getOtherProperties() == null) this.otherProperties = new ArrayList<>();

        if (this.getOtherProperties().size() < 4) this.addNewProperty();
        else this.upgradeProperty();
    }

    /** Adds a new random property to the artifact. */
    public void addNewProperty() {
        // TODO: Add a new property to the artifact.
    }

    /** Upgrades an existing property's stats. */
    public void upgradeProperty() {
        // TODO: Upgrade an existing property.
    }

    /*
     * Serialization methods.
     */

    @Override
    public void save() {
        if (this.getCount() > 0 && this.getOwner() != null) DatabaseObject.super.save();
        else if (this.getId() != null) DatabaseObject.super.delete();
    }

    /**
     * Converts this item into a {@link SceneWeaponInfo} object.
     *
     * @return The {@link SceneWeaponInfo} object.
     */
    public SceneWeaponInfo toWeaponInfo() {
        var skills = this.getSkills();
        var hasSkills = skills != null && skills.size() > 0;

        var info =
                SceneWeaponInfo.newBuilder()
                        .setEntityId(this.getEntityId())
                        .setItemId(this.getItemId())
                        .setGuid(this.getItemGuid())
                        .setLevel(this.getLevel())
                        .setGadgetId(this.getItemData().getGadgetId())
                        .setAbilityInfo(AbilitySyncStateInfo.newBuilder().setIsInited(hasSkills));

        // Add the item's skills.
        if (hasSkills) {
            for (var skillId : skills) info.putAffixMap(skillId, this.getRefinementLevel());
        }

        return info.build();
    }

    /**
     * Converts this item into a {@link Weapon} object.
     *
     * @return The {@link Weapon} object.
     */
    public Weapon toWeapon() {
        var weapon =
                Weapon.newBuilder()
                        .setLevel(this.getLevel())
                        .setExp(this.getExperience())
                        .setPromoteLevel(this.getAscensionLevel());

        // Add the item's skills.
        if (this.getSkills() != null) {
            for (var skillId : this.getSkills()) weapon.putAffixMap(skillId, this.getRefinementLevel());
        }

        return weapon.build();
    }

    /**
     * Converts this item into an {@link ItemOuterClass.Item} object.
     *
     * @return The {@link ItemOuterClass.Item} object.
     */
    public ItemOuterClass.Item toGameObject() {
        var item =
                ItemOuterClass.Item.newBuilder().setGuid(this.getItemGuid()).setItemId(this.getItemId());

        // Add the item-specific data.
        switch (this.getItemType()) {
            case ITEM_WEAPON -> item.setEquip(
                    Equip.newBuilder().setWeapon(this.toWeapon()).setIsLocked(this.isLocked()));
            case ITEM_RELIQUARY -> item.setEquip(
                    Equip.newBuilder()
                            // .setReliquary(this.toReliquary())
                            .setIsLocked(this.isLocked()));
            case ITEM_FURNITURE -> item.setFurniture(Furniture.newBuilder().setCount(this.getCount()));
            default -> item.setMaterial(Material.newBuilder().setCount(this.getCount()));
        }

        return item.build();
    }

    /**
     * Converts this item into an {@link ItemHint} object. This is used in the HUD for receiving a new
     * item.
     *
     * @return The {@link ItemHint} object.
     */
    public ItemHint toHint() {
        return ItemHint.newBuilder()
                .setItemId(this.getItemId())
                .setCount(this.getCount())
                .setIsNew(false)
                .build();
    }

    /**
     * Converts this item into an {@link ItemParam} object.
     *
     * @return The {@link ItemParam} object.
     */
    public ItemParam toParam() {
        return ItemParam.newBuilder().setItemId(this.getItemId()).setCount(this.getCount()).build();
    }
}
