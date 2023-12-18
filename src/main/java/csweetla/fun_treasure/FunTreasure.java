package csweetla.fun_treasure;

import net.fabricmc.api.ModInitializer;

import turniplabs.halplibe.helper.ItemHelper;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FunTreasure implements ModInitializer {
    public static final String MOD_ID = "fun_treasure";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Item armorItemPistonBoots;

	@Override
    public void onInitialize() {
		LOGGER.info(MOD_ID + " initialized.");
		int curr_id = ItemHelper.findOpenIds(1);

		armorItemPistonBoots = (new ItemArmor(MOD_ID + ".piston_boots", curr_id++, ArmorMaterial.iron, 3)).setIconCoord(2, 3);
	}
}
