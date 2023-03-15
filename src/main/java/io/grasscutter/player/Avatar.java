package io.grasscutter.player;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.game.data.GameData;
import io.grasscutter.game.data.excel.avatar.AvatarData;
import io.grasscutter.game.data.excel.avatar.AvatarSkillDepotData;
import io.grasscutter.game.inventory.Item;
import io.grasscutter.network.packets.notify.inventory.AvatarEquipChange;
import io.grasscutter.network.packets.notify.inventory.AvatarFightProp;
import io.grasscutter.proto.AvatarInfoOuterClass.AvatarInfo;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.enums.game.EntityIdType;
import io.grasscutter.utils.enums.game.EquipType;
import io.grasscutter.utils.enums.game.FightProperty;
import io.grasscutter.utils.enums.game.PlayerProperty;
import io.grasscutter.utils.interfaces.DatabaseObject;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.ApiStatus;

/* An instance of an avatar. */
@DataSerializable(table = "avatars")
public final class Avatar implements DatabaseObject {
    @Special(FieldType.ID)
    @Getter
    private ObjectId id = new ObjectId(); // The avatar object's unique identifier.

    @Getter private int avatarId = Integer.MAX_VALUE; // The avatar's ID.
    @Getter private int ownerUserId = Integer.MAX_VALUE; // The avatar's owner's game user ID (UID).

    @Getter private int skillDepotId = Integer.MAX_VALUE; // The avatar's skill depot ID.
    @Getter private Set<Integer> talents = new HashSet<>(); // The avatar's talent IDs.

    @Getter private transient Player owner = null; // The avatar's owner.
    @Getter private transient AvatarData data = null; // The avatar's data.
    @Getter private transient AvatarSkillDepotData skill = null; // The avatar's skill data/depot.
    @Getter private transient long guid = Long.MAX_VALUE; // The avatar's player unique ID.

    @Getter @Setter private int level = 1; // The avatar's level.
    @Getter @Setter private int experience = 0; // The avatar's experience.
    @Getter @Setter private int ascension = 0; // The avatar's ascension level.

    @Getter @Setter private int satiation = 0; // The avatar's satiation (fullness).
    @Getter @Setter private int satiationPenalty = 0; // The avatar's satiation penalty.
    @Getter private float currentHealth = 0; // The avatar's current health.
    @Getter private float currentEnergy = 0; // The avatar's current energy.

    @Getter @Setter private int wings = 140001; // The avatar's wings.
    @Getter @Setter private int costume = 0; // The avatar's costume.
    @Getter private int creationTime = 0; // The avatar's creation time.

    @Getter
    private final transient Int2ObjectMap<Item> equipment =
            new Int2ObjectOpenHashMap<>(); // The avatar's equipment.

    @Getter
    private final transient Int2FloatMap combatProperties =
            new Int2FloatOpenHashMap(); // The avatar's combat properties.

    @Getter
    private final transient Int2FloatMap fightOverrides =
            new Int2FloatOpenHashMap(); // The avatar's combat property overrides.

    @ApiStatus.Internal
    public Avatar() {
        // Empty constructor for initialization.
    }

    /**
     * Constructor for newly created avatars. Uses an avatar ID to fetch the avatar data.
     *
     * @param avatarId The avatar's ID.
     */
    public Avatar(int avatarId) {
        this(GameData.getAvatarDataMap().get(avatarId));
    }

    /**
     * Constructor for newly created avatars.
     *
     * @param avatarData The avatar's data.
     */
    public Avatar(AvatarData avatarData) {
        this.data = avatarData;
        this.avatarId = avatarData.getId();
        this.creationTime = (int) (System.currentTimeMillis() / 1000L);

        this.recalculate(); // Set the avatar's properties.
        this.currentEnergy = 0f; // Set the avatar's current energy.
        this.currentHealth =
                this.getProperty(FightProperty.FIGHT_PROP_MAX_HP); // Set the avatar's current health.

        // Set the avatar's current health.
        this.setProperty(FightProperty.FIGHT_PROP_CUR_HP, this.getCurrentHealth());
    }

    /**
     * Sets the avatar's owner.
     *
     * @param owner The avatar's owner.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
        this.ownerUserId = owner.getUserId();
        this.guid = owner.getNextGameId();
    }

    /**
     * Sets the avatar's skill depot.
     *
     * @param depotId The avatar's skill depot ID.
     */
    public void setSkillDepot(int depotId) {
        // Set the skill depot.
        this.skill = GameData.getAvatarSkillDepotDataMap().get(depotId);
        this.skillDepotId = depotId;
    }

    /*
     * Combat properties.
     */

    /**
     * Fetches the avatar's combat property. If the property is not found, 0 is returned.
     *
     * @param property The property to fetch.
     * @return The property's value.
     */
    public float getProperty(FightProperty property) {
        return this.getCombatProperties().getOrDefault(property.getId(), 0f);
    }

    /**
     * Sets the avatar's combat property.
     *
     * @param property The property to set.
     * @param value The property's value.
     */
    public void setProperty(FightProperty property, float value) {
        this.getCombatProperties().put(property.getId(), value);
    }

    /**
     * Adds a value to the avatar's combat property.
     *
     * @param property The property to add to.
     * @param value The value to add.
     */
    public void addProperty(FightProperty property, float value) {
        this.setProperty(property, this.getProperty(property) + value);
    }

    /**
     * Fetches the avatar's combat property. If the property is not found, 0 is returned.
     *
     * @param property The property to fetch.
     * @return The property's value.
     */
    public float getProperty(int property) {
        return this.getCombatProperties().getOrDefault(property, 0f);
    }

    /**
     * Sets the avatar's combat property.
     *
     * @param property The property to set.
     * @param value The property's value.
     */
    public void setProperty(int property, float value) {
        this.getCombatProperties().put(property, value);
    }

    /**
     * Adds a value to the avatar's combat property.
     *
     * @param property The property to add to.
     * @param value The value to add.
     */
    public void addProperty(int property, float value) {
        this.setProperty(property, this.getProperty(property) + value);
    }

    /*
     * Items.
     */

    /**
     * Equips an item to the avatar.
     *
     * @param item The item to equip.
     * @param recalculate Whether to recalculate the avatar's combat properties.
     */
    public boolean equipItem(Item item, boolean recalculate) {
        // Check the equipment type.
        var type = item.getItemData().getEquipType();
        if (type == EquipType.EQUIP_NONE) return false;

        // Check if other avatars are using the item.
        var avatar = this.getOwner().getAvatars().get(item.getEquippedAvatar());
        if (avatar != null) {
            // Un-equip the item from the avatar.
            // TODO: Un-equip the item from the avatar.
        }

        // Add the item to the avatar's equipment.
        this.getEquipment().put(type.getValue(), item);
        // Add an entity ID if required.
        if (type == EquipType.EQUIP_WEAPON && this.getOwner().getWorld() != null) {
            item.setEntityId(this.getOwner().getWorld().nextEntityId(EntityIdType.WEAPON));
        }

        // Save the item.
        item.setEquippedAvatar(this.getAvatarId());
        item.save();

        // Send the equipment change packet.
        if (this.getOwner().isLoggedIn())
            this.getOwner().getSession().send(new AvatarEquipChange(this, item));

        // Recalculate the avatar's combat properties.
        if (recalculate) this.recalculate();

        return true;
    }

    /**
     * Fetches an item from the avatar's equipment.
     *
     * @param type The equipment type.
     * @return The item, or {@code null} if the avatar doesn't have an item equipped.
     */
    public Item getItem(EquipType type) {
        return this.getEquipment().get(type.getValue());
    }

    /**
     * Fetches an item from the avatar's equipment.
     *
     * @param slotId The equipment slot ID.
     * @return The item, or {@code null} if the avatar doesn't have an item equipped.
     */
    public Item getItem(int slotId) {
        return this.getEquipment().get(slotId);
    }

    /**
     * Shortcut method for fetching the avatar's weapon.
     *
     * @return The avatar's weapon, or {@code null} if the avatar doesn't have a weapon equipped.
     */
    public Item getWeapon() {
        return this.getItem(EquipType.EQUIP_WEAPON);
    }

    /*
     * Statistics.
     */

    /**
     * Sets the avatar's energy.
     *
     * @param energy The energy to set.
     */
    public void setCurrentEnergy(float energy) {
        var skill = this.getSkill();
        if (skill != null && skill.getEnergySkillData() != null) {
            var element = skill.getElementType();
            this.setProperty(element.getCurEnergyProp(), energy);
            var maxEnergy = skill.getEnergySkillData().getElementalCost();
            this.setProperty(element.getMaxEnergyProp(), maxEnergy);
        }
    }

    /** Recalculates the avatar's combat stats. Does not send an ability change packet. */
    public void recalculate() {
        this.recalculate(false);
    }

    /**
     * Recalculates the avatar's combat stats.
     *
     * @param change Whether to send an ability change packet.
     */
    public void recalculate(boolean change) {
        // Fetch avatar data.
        var data = this.getData();

        // Calculate the current health.
        var currentHealth =
                this.getProperty(FightProperty.FIGHT_PROP_MAX_HP) <= 0
                        ? 1f
                        : this.getProperty(FightProperty.FIGHT_PROP_CUR_HP)
                                / this.getProperty(FightProperty.FIGHT_PROP_MAX_HP);
        // Get the current amount of energy.
        var currentEnergy =
                this.getSkill() != null
                        ? this.getProperty(this.getSkill().getElementType().getCurEnergyProp())
                        : 0f;

        // Recalculate properties.
        this.getCombatProperties().clear();
        // Add the base properties.
        var level = this.getLevel();
        this.setProperty(FightProperty.FIGHT_PROP_BASE_HP, data.getBaseHealth(level));
        this.setProperty(FightProperty.FIGHT_PROP_BASE_ATTACK, data.getBaseAttack(level));
        this.setProperty(FightProperty.FIGHT_PROP_BASE_DEFENSE, data.getBaseDefense(level));
        this.setProperty(FightProperty.FIGHT_PROP_CRITICAL, data.getBaseCritChance());
        this.setProperty(FightProperty.FIGHT_PROP_CRITICAL_HURT, data.getBaseCritDamage());
        this.setProperty(FightProperty.FIGHT_PROP_CHARGE_EFFICIENCY, 1f);

        // Add extra ascension statistics.
        var ascensionData = GameData.getAscensionData(data.getAvatarPromoteId(), this.getAscension());
        if (ascensionData != null)
            for (var property : ascensionData.getAddProps())
                this.addProperty(property.getId(), property.getCount());

        // Set the energy.
        this.setCurrentEnergy(currentEnergy);

        // Calculate artifact properties.
        for (var i = 1; i <= 5; i++) {
            // Get the artifact.
            var artifact = this.getItem(i);
            if (artifact == null) continue;

            // Get the primary stat.
            var mainProperty = GameData.getReliquaryMainPropDataMap().get(artifact.getMainPropertyId());
            if (mainProperty != null) {
                var property = mainProperty.getProperty();
                var levelData =
                        GameData.getArtifactLevelData(artifact.getItemData().getRarity(), artifact.getLevel());
                if (levelData != null) this.addProperty(property, levelData.getPropValue(property.getId()));
            }
        }

        // Set the current health.
        this.setProperty(
                FightProperty.FIGHT_PROP_CUR_HP,
                this.getProperty(FightProperty.FIGHT_PROP_MAX_HP) * currentHealth);

        // Send change packets.
        var player = this.getOwner();
        if (player != null && player.isLoggedIn()) {
            // Update properties.
            player.getSession().send(new AvatarFightProp(this));
            // TODO: Send packet to update abilities.
        }
    }

    /*
     * Conversions.
     */

    /**
     * Converts this object into an {@link AvatarInfo} object.
     *
     * @return The converted object.
     */
    public AvatarInfo toProto() {
        var avatarInfo =
                AvatarInfo.newBuilder()
                        .setAvatarId(this.getAvatarId())
                        .setGuid(this.getGuid())
                        .setLifeState(1)
                        .putAllFightPropMap(this.getCombatProperties())
                        .setSkillDepotId(this.getSkillDepotId())
                        .setAvatarType(1)
                        .setBornTime(this.getCreationTime())
                        .setWearingFlycloakId(this.getWings())
                        .setCostumeId(this.getCostume());

        // Add the other avatar properties.
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_LEVEL, this.getLevel());
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_EXP, this.getExperience());
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_BREAK_LEVEL, this.getAscension());
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_SATIATION_VAL, this.getSatiation());
        ServerUtils.property(
                avatarInfo, PlayerProperty.PROP_SATIATION_PENALTY_TIME, this.getSatiationPenalty());

        return avatarInfo.build();
    }
}
