package csweetla.fun_treasure;

import csweetla.fun_treasure.item.EscapeRopeItem;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.item.ItemFood;
import turniplabs.halplibe.helper.ItemHelper;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.TextureHelper;


public class FunTreasure implements ModInitializer {
    public static final String MOD_ID = "fun_treasure";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Item armorItemPistonBoots;
	public static Item ItemEscapeRope;
	public static ItemFood FoodItemOrange;

	@Override
    public void onInitialize() {
		int[] tex_coords;
		LOGGER.info(MOD_ID + " initialized.");
		int curr_id = ItemHelper.findOpenIds(2);

		armorItemPistonBoots = new ItemArmor(MOD_ID + ".piston_boots", curr_id++, ArmorMaterial.iron, 3).setIconCoord(2, 3);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"escape_rope_gold.png");
		ItemEscapeRope = new EscapeRopeItem(MOD_ID + ".escape_rope", curr_id++).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"orange.png");
		FoodItemOrange = (ItemFood) new ItemFood(MOD_ID + ".orange",curr_id++,4,false).setIconCoord(tex_coords[0],tex_coords[1]);
	}
}
