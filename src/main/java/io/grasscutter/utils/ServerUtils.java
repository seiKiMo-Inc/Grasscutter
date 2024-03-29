package io.grasscutter.utils;

import io.grasscutter.player.Player;
import io.grasscutter.proto.AvatarInfoOuterClass.AvatarInfo;
import io.grasscutter.proto.FightPropPairOuterClass.FightPropPair;
import io.grasscutter.proto.ProfilePictureOuterClass.ProfilePicture;
import io.grasscutter.proto.PropPairOuterClass.PropPair;
import io.grasscutter.proto.PropValueOuterClass.PropValue;
import io.grasscutter.proto.QueryCurrRegionHttpRspOuterClass.QueryCurrRegionHttpRsp;
import io.grasscutter.utils.enums.game.PlayerProperty;
import java.util.concurrent.atomic.AtomicReference;

/* Utility methods for accessing server info. */
public interface ServerUtils {
    // This server region.
    AtomicReference<QueryCurrRegionHttpRsp> CURRENT_REGION = new AtomicReference<>();

    /**
     * Performs a hash code operation on an ability hash.
     *
     * @param hash The hash to perform the operation on.
     * @return The hash code.
     */
    static int hashAbility(String hash) {
        var v7 = 0;
        var v8 = 0;
        while (v8 < hash.length()) {
            v7 = hash.charAt(v8++) + 131 * v7;
        }

        return v7;
    }

    /**
     * Creates a game property value.
     *
     * @param type The property type.
     * @param value The property value.
     * @return The property object.
     */
    static PropValue property(int type, int value) {
        return PropValue.newBuilder().setType(type).setIval(value).setVal(value).build();
    }

    /**
     * Creates a game property value.
     *
     * @param type The property type.
     * @param value The property value.
     * @return The property object.
     */
    static PropValue property(PlayerProperty type, int value) {
        return PropValue.newBuilder().setType(type.getId()).setIval(value).setVal(value).build();
    }

    /**
     * Sets the property of an avatar.
     *
     * @param infoBuilder The avatar info builder.
     * @param type The property type.
     * @param value The property value.
     */
    static void property(AvatarInfo.Builder infoBuilder, PlayerProperty type, int value) {
        infoBuilder.putPropMap(type.getId(), ServerUtils.property(type, value));
    }

    /**
     * Creates a combat property value.
     *
     * @param id The property ID.
     * @param value The property value.
     */
    static FightPropPair combatProperty(int id, float value) {
        return FightPropPair.newBuilder().setPropType(id).setPropValue(value).build();
    }

    /**
     * Creates a basic player property pair.
     *
     * @param type The property type.
     * @param value The property value.
     * @return The property pair.
     */
    static PropPair pairProperty(PlayerProperty type, int value) {
        return PropPair.newBuilder()
                .setType(type.getId())
                .setPropValue(ServerUtils.property(type, value))
                .build();
    }

    /**
     * Creates a scene transaction ID.
     *
     * @param player The player.
     * @return The scene transaction ID.
     */
    static String sceneTransaction(Player player) {
        return "3-"
                + player.getUserId()
                + "-"
                + (int) (System.currentTimeMillis() / 1000)
                + "-"
                + 18402;
    }

    /**
     * Creates a profile picture object. Uses an avatar ID for the profile picture.
     *
     * @param avatarId The avatar ID.
     * @return The profile picture object.
     */
    static ProfilePicture profilePicture(int avatarId) {
        return ProfilePicture.newBuilder().setAvatarId(avatarId).build();
    }
}
