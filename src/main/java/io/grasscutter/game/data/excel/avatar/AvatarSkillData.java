package io.grasscutter.game.data.excel.avatar;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.utils.enums.Priority;
import io.grasscutter.utils.enums.game.ElementType;
import lombok.Getter;

/* Avatar skill data. */
@Resource(name = "AvatarSkillExcelConfigData.json", priority = Priority.HIGHEST)
@Getter
public final class AvatarSkillData implements GameResource {
    @Getter(onMethod = @__(@Override))
    private int id;

    @SerializedName("cdTime")
    private float cooldown;

    @SerializedName("costElemVal")
    private int elementalCost;

    @SerializedName("maxChargeNum")
    private int maxCharges;

    @SerializedName("triggerID")
    private int triggerId;

    @SerializedName("isAttackCameraLock")
    private boolean lockToCamera;

    @SerializedName("proudSkillGroupId")
    private int groupId;

    @SerializedName("costElemType")
    private ElementType elementType;

    private long nameTextMapHash;
    private long descTextMapHash;
    private String abilityName;

    @Override
    public void onLoad() {}
}
