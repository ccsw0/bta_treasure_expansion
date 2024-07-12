package csweetla.treasure_expansion.item;


import net.minecraft.core.item.Item;

public class SpiderSilkItem extends Item {
	public SpiderSilkItem(String name, int id, int durability) {
		super(name, id);
		setMaxDamage(durability);
	}
}
