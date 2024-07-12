package csweetla.treasure_expansion.item;

import net.minecraft.core.item.ItemQuiver;

public class FireQuiverItem extends ItemQuiver {
	public FireQuiverItem(String name, int id, int durability) {
		super(name, id);
		setMaxDamage(durability);
	}
}
