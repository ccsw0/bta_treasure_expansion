package csweetla.treasure_expansion.item;

import csweetla.treasure_expansion.TreasureExpansion;
import net.minecraft.core.item.ItemQuiver;
import turniplabs.halplibe.helper.TextureHelper;

public class FireQuiverItem extends ItemQuiver {
	int[] tex_coords;
	int[] empty_tex_coords;

	public FireQuiverItem(String name, int id, int durability) {
		super(name, id);
		setMaxDamage(durability);
		tex_coords = TextureHelper.getOrCreateItemTexture(TreasureExpansion.MOD_ID,"fire_quiver.png");
		empty_tex_coords = TextureHelper.getOrCreateItemTexture(TreasureExpansion.MOD_ID,"fire_quiver_empty.png");
	}

	@Override
	public int getIconFromDamage(int id) {
		return id >= this.getMaxDamage() ? iconCoordToIndex(empty_tex_coords[0], empty_tex_coords[1]) : iconCoordToIndex(tex_coords[0], tex_coords[1]);
	}
}
