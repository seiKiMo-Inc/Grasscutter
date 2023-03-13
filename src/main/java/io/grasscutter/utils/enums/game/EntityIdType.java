package io.grasscutter.utils.enums.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter public enum EntityIdType {
	AVATAR	(0x01),
	MONSTER	(0x02),
	NPC		(0x03),
	GADGET	(0x04),
    REGION	(0x05),
    WEAPON	(0x06),
	TEAM 	(0x09),
	MPLEVEL	(0x0b);

	private final int id;
}
