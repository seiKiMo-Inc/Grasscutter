package io.grasscutter.game.data.bin;

import lombok.AllArgsConstructor;
import lombok.Getter;

/* An ability embryo. */
@AllArgsConstructor
public final class AbilityEmbryoEntry {
	@Getter private String name;
	@Getter private String[] abilities;

	public AbilityEmbryoEntry() {

	}
}
