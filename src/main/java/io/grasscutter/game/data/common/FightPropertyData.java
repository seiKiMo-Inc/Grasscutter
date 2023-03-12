package io.grasscutter.game.data.common;

import io.grasscutter.utils.enums.game.FightProperty;
import lombok.Getter;

/* Data about a fight property. */
@Getter public final class FightPropertyData {
	private String propType;
	private FightProperty prop;
    private float value;

	public void onLoad() {
		this.prop = FightProperty.fetch(propType);
	}
}
