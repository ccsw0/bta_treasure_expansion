package csweetla.treasure_expansion.item;

import net.minecraft.core.item.ItemQuiver;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class FireQuiverItem extends ItemQuiver {
	public FireQuiverItem(String name, int id, int durability) {
		super(name, MOD_ID + ":" + name, id);
		setMaxDamage(durability);
	}
}
