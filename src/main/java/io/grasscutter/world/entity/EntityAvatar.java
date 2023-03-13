package io.grasscutter.world.entity;

import io.grasscutter.player.Avatar;
import io.grasscutter.player.Player;
import io.grasscutter.proto.AbilityControlBlockOuterClass.AbilityControlBlock;
import io.grasscutter.proto.AbilitySyncStateInfoOuterClass.AbilitySyncStateInfo;
import io.grasscutter.proto.AnimatorParameterValueInfoPairOuterClass.AnimatorParameterValueInfoPair;
import io.grasscutter.proto.EntityAuthorityInfoOuterClass.EntityAuthorityInfo;
import io.grasscutter.proto.EntityClientDataOuterClass.EntityClientData;
import io.grasscutter.proto.EntityRendererChangedInfoOuterClass.EntityRendererChangedInfo;
import io.grasscutter.proto.ProtEntityTypeOuterClass.ProtEntityType;
import io.grasscutter.proto.SceneAvatarInfoOuterClass.SceneAvatarInfo;
import io.grasscutter.proto.SceneEntityInfoOuterClass.SceneEntityInfo;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.enums.game.EntityIdType;
import io.grasscutter.utils.enums.game.FightProperty;
import io.grasscutter.utils.enums.game.PlayerProperty;
import io.grasscutter.world.Position;
import io.grasscutter.world.Scene;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import lombok.Getter;

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
//                .setCoreProudSkillLevel(avatar.getSkillLevels())
                .setSkillDepotId(avatar.getSkillDepotId())
//                .addAllInherentProudSkillList(avatar.getSkillList())
//                .putAllProudSkillExtraLevelMap(avatar.getExtraSkills())
                .addAllTeamResonanceList(player.getTeams().getResonances())
//                .setWearingFlycloakId(avatar.getWings())
//                .setCostumeId(avatar.getCostume())
                .setBornTime(avatar.getCreationTime());

        // Add the avatar's weapon.
        // TODO: Add the weapon ID.
        // info.setWeapon(null);
        // info.setReliquaryList();
        // info.setEquipIdList();

        return info.build();
    }

    /**
     * Gets the ability control block of this avatar.
     *
     * @return The ability control block.
     */
    public AbilityControlBlock getAbilities() {
        var data = this.getAvatar().getData();
        var abilities = AbilityControlBlock.newBuilder();

        // TODO: Add abilities.

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
