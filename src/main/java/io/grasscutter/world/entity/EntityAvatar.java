package io.grasscutter.world.entity;

import io.grasscutter.game.data.GameData;
import io.grasscutter.player.Avatar;
import io.grasscutter.player.Player;
import io.grasscutter.proto.AbilityControlBlockOuterClass.AbilityControlBlock;
import io.grasscutter.proto.AbilityEmbryoOuterClass.AbilityEmbryo;
import io.grasscutter.proto.AbilitySyncStateInfoOuterClass.AbilitySyncStateInfo;
import io.grasscutter.proto.AnimatorParameterValueInfoPairOuterClass.AnimatorParameterValueInfoPair;
import io.grasscutter.proto.EntityAuthorityInfoOuterClass.EntityAuthorityInfo;
import io.grasscutter.proto.EntityClientDataOuterClass.EntityClientData;
import io.grasscutter.proto.EntityRendererChangedInfoOuterClass.EntityRendererChangedInfo;
import io.grasscutter.proto.ProtEntityTypeOuterClass.ProtEntityType;
import io.grasscutter.proto.SceneAvatarInfoOuterClass.SceneAvatarInfo;
import io.grasscutter.proto.SceneEntityInfoOuterClass.SceneEntityInfo;
import io.grasscutter.proto.SceneWeaponInfoOuterClass.SceneWeaponInfo;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.enums.game.EntityIdType;
import io.grasscutter.utils.enums.game.FightProperty;
import io.grasscutter.utils.enums.game.PlayerProperty;
import io.grasscutter.world.Position;
import io.grasscutter.world.Scene;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/** An entity instance of an {@link Avatar}. */
public final class EntityAvatar extends Entity {
    @Getter private final Avatar avatar;

    /**
     * Constructor for a despawned avatar.
     *
     * @param avatar The avatar.
     */
    public EntityAvatar(Avatar avatar) {
        this(null, avatar);
    }

    /**
     * Constructor for an avatar in a scene.
     *
     * @param scene The scene.
     * @param avatar The avatar.
     */
    public EntityAvatar(Scene scene, Avatar avatar) {
        super(scene);
        this.avatar = avatar;

        if (scene == null) return;
        // Update the entity IDs.
        this.id = scene.getWorld()
                .nextEntityId(EntityIdType.AVATAR);
    }

    /**
     * Gets the owner of this avatar.
     *
     * @return The owner as a player.
     */
    public Player getPlayer() {
        return this.avatar.getOwner();
    }

    /**
     * Collects the info of the avatar into a {@link SceneAvatarInfo}.
     * This is used to send the avatar's info to the client.
     *
     * @return The avatar info object.
     */
    public SceneAvatarInfo toSceneAvatarInfo() {
        var player = this.getPlayer();
        var avatar = this.getAvatar();

        var info = SceneAvatarInfo.newBuilder()
                .setUid(player.getUserId())
                .setPeerId(player.getPeerId())
                .setAvatarId(avatar.getAvatarId())
                .setGuid(avatar.getGuid())
                .addAllTalentIdList(avatar.getTalents())
                .setCoreProudSkillLevel(0) // avatar.getSkillLevels())
                .setSkillDepotId(avatar.getSkillDepotId())
                .addAllInherentProudSkillList(List.of()) // avatar.getSkillList()
                .putAllProudSkillExtraLevelMap(Map.of()) // avatar.getExtraSkills()
                .addAllTeamResonanceList(player.getTeams().getResonances())
                .setWearingFlycloakId(avatar.getWings())
                .setCostumeId(avatar.getCostume())
                .setBornTime(avatar.getCreationTime());

        // Add the avatar's weapon.
        // TODO: Add the weapon ID.
         info.setWeapon(SceneWeaponInfo.newBuilder()
                 .setEntityId(100664575)
                 .setGuid(2785642601942876162L)
                 .setLevel(90)
                 .setItemId(11512)
                 .setPromoteLevel(6)
                 .setGadgetId(50011512));
//         info.setReliquaryList();
//         info.setEquipIdList();

        return info.build();
    }

    /**
     * Gets the ability control block of this avatar.
     *
     * @return The ability control block.
     */
    public AbilityControlBlock getAbilities() {
        var avatar = this.getAvatar();
        var data = avatar.getData();
        var abilities = AbilityControlBlock.newBuilder();
        var embryoId = 0;

        // Add avatar abilities.
        if (data.getAbilities() != null) {
            for (var id : data.getAbilities())
                abilities.addAbilityEmbryoList(AbilityEmbryo.newBuilder()
                        .setAbilityId(++embryoId)
                        .setAbilityNameHash(id)
                        .setAbilityOverrideNameHash(GameConstants.DEFAULT_ABILITY));
        }

        // Add default abilities.
        for (var id : GameConstants.DEFAULT_ABILITIES)
            abilities.addAbilityEmbryoList(AbilityEmbryo.newBuilder()
                    .setAbilityId(++embryoId)
                    .setAbilityNameHash(id)
                    .setAbilityOverrideNameHash(GameConstants.DEFAULT_ABILITY));

        // Add team resonance abilities.
        for (var id : this.getPlayer().getTeams().getResonancesConfig())
            abilities.addAbilityEmbryoList(AbilityEmbryo.newBuilder()
                    .setAbilityId(++embryoId)
                    .setAbilityNameHash(id)
                    .setAbilityOverrideNameHash(GameConstants.DEFAULT_ABILITY));

        // Add skill depot abilities.
        var skillDepot = GameData.getAvatarSkillDepotDataMap().get(
                avatar.getSkillDepotId());
        if (skillDepot != null && skillDepot.getAbilities() != null)
            for (var id : skillDepot.getAbilities())
                abilities.addAbilityEmbryoList(AbilityEmbryo.newBuilder()
                        .setAbilityId(++embryoId)
                        .setAbilityNameHash(id)
                        .setAbilityOverrideNameHash(GameConstants.DEFAULT_ABILITY));

        // Add extra abilities.
        // TODO: Add extra abilities.

        return abilities.build();
    }

    /*
     * Entity data.
     */

    @Override
    public Position getPosition() {
        return this.getPlayer().getPosition();
    }

    @Override
    public Position getRotation() {
        return this.getPlayer().getRotation();
    }

    @Override
    public SceneEntityInfo toProto() {
        // Create an entity authority object.
        var authority = EntityAuthorityInfo.newBuilder()
                .setAbilityInfo(AbilitySyncStateInfo.newBuilder())
                .setRendererChangedInfo(EntityRendererChangedInfo.newBuilder())
                .setAiInfo(GameConstants.DEFAULT_AI);

        // Create the entity info object.
        var info = SceneEntityInfo.newBuilder()
                .setEntityId(this.getId())
                .setEntityType(ProtEntityType.PROT_ENTITY_TYPE_AVATAR)
                .addAnimatorParaList(AnimatorParameterValueInfoPair.newBuilder())
                .setEntityClientData(EntityClientData.newBuilder())
                .setEntityAuthorityInfo(authority)
                .setLastMoveSceneTimeMs(this.getSceneLastMove())
                .setLastMoveReliableSeq(this.getReliableLastMove())
                .setLifeState(this.getLifeState().getValue());

        // Add the motion info.
        if (this.getScene() != null)
            info.setMotionInfo(this.toMotionInfo());
        // Add properties.
        this.propsToBuilder(info);
        info.addPropList(ServerUtils.pairProperty(
                PlayerProperty.PROP_LEVEL, this.getAvatar().getLevel()));
        // Set the avatar info.
        info.setAvatar(this.toSceneAvatarInfo());

        return info.build();
    }

    @Override
    public Int2FloatMap getCombatProperties() {
        return this.getAvatar().getCombatProperties();
    }

    @Override
    public boolean isAlive() {
        return this.get(FightProperty.FIGHT_PROP_CUR_HP) > 0f;
    }
}
