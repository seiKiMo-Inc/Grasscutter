package io.grasscutter.game.data.excel.avatar;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameData;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.game.data.common.ExponentialCurve;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.enums.Priority;
import io.grasscutter.utils.enums.game.FightProperty;
import io.grasscutter.utils.enums.game.WeaponType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;

import java.util.List;

import static io.grasscutter.utils.enums.game.FightProperty.*;

/* Game avatar data. */
@Resource(name = "AvatarExcelConfigData.json",
        priority = Priority.LOW)
public final class AvatarData implements GameResource {
    @Getter(onMethod = @__(@Override))
    private int id;

    private String iconName;
    @Getter private String bodyType;
    @Getter private String qualityType;
    @Getter private int chargeEfficiency;
    @Getter private int initialWeapon;
    @Getter private WeaponType weaponType;
    @Getter private String imageName;
    @Getter private int avatarPromoteId;
    @Getter private String cutsceneShow;
    @Getter private int skillDepotId;
    @Getter private int staminaRecoverSpeed;
    @Getter private List<String> candSkillDepotIds;
    @Getter private String avatarIdentityType;
    @Getter private List<Integer> avatarPromoteRewardLevelList;
    @Getter private List<Integer> avatarPromoteRewardIdList;

    @Getter private long nameTextMapHash;

    private float hpBase;
    private float attackBase;
    private float defenseBase;
    @SerializedName("critical")
    @Getter private float baseCritChance;
    @SerializedName("criticalHurt")
    @Getter private float baseCritDamage;

    private List<ExponentialCurve> propGrowCurves;

    // Transient
    @Getter private String name;

    private Int2ObjectMap<String> growthCurveMap;
    private float[] hpGrowthCurve;
    private float[] attackGrowthCurve;
    private float[] defenseGrowthCurve;
    @Getter private AvatarSkillDepotData skillDepot;
    @Getter private IntList abilities;

    @Getter private List<Integer> fetters;
    @Getter private int nameCardRewardId;
    @Getter private int nameCardId;

    @Override
    public void onLoad() {
        this.skillDepot = GameData.getAvatarSkillDepotDataMap().get(this.skillDepotId);

//         // Get fetters from GameData
//         this.fetters = GameData.getFetterDataEntries().get(this.id);
//
//         if (GameData.getFetterCharacterCardDataMap().get(this.id) != null) {
//             this.nameCardRewardId = GameData.getFetterCharacterCardDataMap().get(this.id).getRewardId();
//         }
//
//         if (GameData.getRewardDataMap().get(this.nameCardRewardId) != null) {
//             this.nameCardId = GameData.getRewardDataMap().get(this.nameCardRewardId).getRewardItemList().get(0).getItemId();
//         }

        var size = GameData.getAvatarCurveDataMap().size();
        this.hpGrowthCurve = new float[size];
        this.attackGrowthCurve = new float[size];
        this.defenseGrowthCurve = new float[size];

        for (var curveData : GameData.getAvatarCurveDataMap().values()) {
            var level = curveData.getLevel() - 1;
            for (var growCurve : this.propGrowCurves) {
                var prop = FightProperty.fetch(growCurve.getType());
                switch (prop) {
                    case FIGHT_PROP_BASE_HP ->
                            this.hpGrowthCurve[level] = curveData.getCurveInfo().get(growCurve.getGrowCurve());
                    case FIGHT_PROP_BASE_ATTACK ->
                            this.attackGrowthCurve[level] = curveData.getCurveInfo().get(growCurve.getGrowCurve());
                    case FIGHT_PROP_BASE_DEFENSE ->
                            this.defenseGrowthCurve[level] = curveData.getCurveInfo().get(growCurve.getGrowCurve());
                    default -> {}
                }
            }
        }

//        // Cache the abilities.
//        var split = this.iconName.split("_");
//        if (split.length > 0) {
//            this.name = split[split.length - 1];
//
//            var info = GameData.getAbilityEmbryoInfo().get(this.name);
//            if (info != null) {
//                this.abilities = new IntArrayList(info.getAbilities().length);
//                for (var ability : info.getAbilities()) {
//                    this.abilities.add(ServerUtils.hashAbility(ability));
//                }
//            }
//        }
    }

    /*
     * Calculate statistics.
     */

    /**
     * Get the base health of the avatar.
     *
     * @param level The level of the avatar.
     * @return The base health of the avatar.
     */
    public float getBaseHealth(int level) {
        try {
            return this.hpBase * this.hpGrowthCurve[level - 1];
        } catch (Exception ignored) {
            return this.hpBase;
        }
    }

    /**
     * Get the base attack of the avatar.
     *
     * @param level The level of the avatar.
     * @return The base attack of the avatar.
     */
    public float getBaseAttack(int level) {
        try {
            return this.attackBase * this.attackGrowthCurve[level - 1];
        } catch (Exception ignored) {
            return this.attackBase;
        }
    }

    /**
     * Get the base defense of the avatar.
     *
     * @param level The level of the avatar.
     * @return The base defense of the avatar.
     */
    public float getBaseDefense(int level) {
        try {
            return this.defenseBase * this.defenseGrowthCurve[level - 1];
        } catch (Exception ignored) {
            return this.defenseBase;
        }
    }
}
