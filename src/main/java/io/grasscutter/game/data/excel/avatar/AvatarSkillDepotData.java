package io.grasscutter.game.data.excel.avatar;

import io.grasscutter.game.data.GameData;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.game.data.bin.AbilityEmbryoEntry;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.enums.Priority;
import io.grasscutter.utils.enums.game.ElementType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/* Game avatar depot data. */
@Resource(name = "AvatarSkillDepotExcelConfigData.json",
        priority = Priority.HIGH)
@Getter public final class AvatarSkillDepotData implements GameResource {
    @Getter public static class InherentProudSkillOpens {
        private int proudSkillGroupId;
        private int needAvatarPromoteLevel;
    }

    @Getter(onMethod = @__(@Override))
    private int id;
    private int energySkill;
    private int attackModeSkill;

    private List<Integer> skills;
    private List<Integer> subSkills;
    private List<String> extraAbilities;
    private List<Integer> talents;
    private List<InherentProudSkillOpens> inherentProudSkillOpens;

    private String talentStarName;
    private String skillDepotAbilityGroup;

    // Transient
    private AvatarSkillData energySkillData;
    private ElementType elementType;
    private IntList abilities;
    private int talentCostItemId;

    /**
     * Sets the abilities for this avatar depot.
     *
     * @param info The ability embryo entry.
     */
    public void setAbilities(AbilityEmbryoEntry info) {
        // Clear the abilities list.
        this.abilities = new IntArrayList(info.getAbilities().length);
        // Re-add hashed abilities.
        for (var ability : info.getAbilities()) {
            this.abilities.add(ServerUtils.hashAbility(ability));
        }
    }

    /**
     * Fetches the skill IDs for this avatar depot.
     *
     * @return The skill IDs.
     */
    public IntStream getSkills() {
        return IntStream.concat(this.skills.stream().mapToInt(i -> i),
                        IntStream.of(this.energySkill))
                .filter(skillId -> skillId > 0);
    }

    @Override
    public void onLoad() {
        // Set energy skill data
        this.energySkillData = GameData.getAvatarSkillDataMap().get(this.energySkill);
        if (this.energySkillData != null) {
            this.elementType = this.energySkillData.getElementType();
        } else {
            this.elementType = ElementType.None;
        }
        // Set embryo abilities (if player skill depot)
        if (this.getSkillDepotAbilityGroup() != null && this.getSkillDepotAbilityGroup().length() > 0) {
            var config = GameData.getPlayerAbilities().get(this.getSkillDepotAbilityGroup());

            if (config != null) {
                this.setAbilities(new AbilityEmbryoEntry(this.getSkillDepotAbilityGroup(),
                        config.abilities.stream().map(Object::toString).toArray(String[]::new)));
            }
        }

        // Get constellation item from GameData
        Optional.ofNullable(this.talents)
                .map(talents -> talents.get(0))
                .map(i -> GameData.getAvatarTalentDataMap().get((int) i))
                .map(AvatarTalentData::getMainCostItemId)
                .ifPresent(itemId -> this.talentCostItemId = itemId);
    }
}
