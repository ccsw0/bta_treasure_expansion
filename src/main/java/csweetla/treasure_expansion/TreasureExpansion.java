package csweetla.treasure_expansion;

import csweetla.treasure_expansion.item.EscapeRopeItem;
import csweetla.treasure_expansion.item.LabyrinthGeneratorItem;

import csweetla.treasure_expansion.item.LavaCharmItem;
import csweetla.treasure_expansion.item.SilverSwordItem;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.helper.*;

import java.util.Properties;

public class TreasureExpansion implements ModInitializer {
    public static final String MOD_ID = "treasure_expansion";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ConfigHandler config;
	static {
		// Config
		Properties prop = new Properties();
		prop.setProperty("ids.piston_boots", "32200");
		prop.setProperty("ids.diving_helmet", "32201");
		prop.setProperty("ids.silver_sword", "32202");
		prop.setProperty("ids.escape_rope", "32203");
		prop.setProperty("ids.escape_rope_gold", "32204");
		prop.setProperty("ids.strange_device", "32205");
		prop.setProperty("ids.labyrinth_generator", "32206");
		prop.setProperty("ids.orange", "32207");
		prop.setProperty("ids.grapes", "32208");
		prop.setProperty("ids.bananas", "32209");
		prop.setProperty("ids.fruit_salad", "32210");
		prop.setProperty("ids.lava_charm", "32211");
		config = new ConfigHandler(MOD_ID, prop);
	}

	public static ArmorMaterial armorMaterialPistonBoots;
	public static ArmorMaterial armorMaterialDiving;
	public static Item armorItemPistonBoots;
	public static Item armorItemDivingHelmet;
	public static Item toolItemSilverSword;
	public static Item ItemEscapeRope;
	public static Item ItemEscapeRopeGold;
	public static Item ItemStrangeDevice;
	public static Item ItemLabyrinthGenerator;
	public static Item FoodItemOrange;
	public static Item FoodItemGrapes;
	public static Item FoodItemBananas;
	public static Item FoodItemFruitSalad;
	public static Item itemLavaCharm;

	public void initializeArmorMaterials() {
		armorMaterialPistonBoots = ArmorHelper.createArmorMaterial("piston_boots",220,50.0F,50.0F,20.0F,120.0F);
		armorMaterialDiving = ArmorHelper.createArmorMaterial("diving",220,20.0F,60.0F,20.0F,20.0F);
	}

	public void initializeItems() {
		int[] tex_coords;

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"piston_boots.png");
		armorItemPistonBoots = new ItemArmor(MOD_ID + ".piston_boots", config.getInt("ids.piston_boots"), armorMaterialPistonBoots, 3).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"diving_helmet.png");
		armorItemDivingHelmet = new ItemArmor(MOD_ID + ".diving_helmet", config.getInt("ids.diving_helmet"), armorMaterialDiving, 0).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"silver_sword.png");
		toolItemSilverSword = new SilverSwordItem(MOD_ID + ".silver_sword", config.getInt("ids.silver_sword"), ToolMaterial.iron).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"escape_rope_gold.png");
		ItemEscapeRopeGold = new EscapeRopeItem(MOD_ID + ".escape_rope_gold", config.getInt("ids.escape_rope_gold"), 6).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"escape_rope.png");
		ItemEscapeRope = new EscapeRopeItem(MOD_ID + ".escape_rope", config.getInt("ids.escape_rope"), 1).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"device.png");
		ItemStrangeDevice = new Item(MOD_ID + ".strange_device", config.getInt("ids.strange_device")).setIconCoord(tex_coords[0],tex_coords[1]).setMaxStackSize(1);

		ItemLabyrinthGenerator = new LabyrinthGeneratorItem(MOD_ID + ".labyrinth_generator", config.getInt("ids.labyrinth_generator")).setIconCoord(tex_coords[0],tex_coords[1]).setMaxStackSize(1);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"orange.png");
		FoodItemOrange = new ItemFood(MOD_ID + ".orange",config.getInt("ids.orange"),4,false).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"grapes.png");
		FoodItemGrapes = new ItemFood(MOD_ID + ".grapes",config.getInt("ids.grapes"),4,false).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"bananas.png");
		FoodItemBananas = new ItemFood(MOD_ID + ".bananas",config.getInt("ids.bananas"),4,false).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"fruit_salad.png");
		FoodItemFruitSalad = new ItemFood(MOD_ID + ".fruit_salad",config.getInt("ids.fruit_salad"),20,false).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"lava_charm.png");
		itemLavaCharm = new LavaCharmItem(MOD_ID + ".lava_charm",config.getInt("ids.lava_charm")).setIconCoord(tex_coords[0],tex_coords[1]);
	}

	private void initializeRecipes() {
		RecipeHelper.Crafting.createShapelessRecipe(FoodItemFruitSalad,1,
			new Object[]{
				Item.bowl,
				Item.foodApple,
				Item.cherry,
				FoodItemBananas,
				FoodItemGrapes,
				FoodItemOrange
			}
		);
	}

	@Override
    public void onInitialize() {
		LOGGER.info(MOD_ID + " initialized.");

		initializeArmorMaterials();
		initializeItems();
		initializeRecipes();

	}
}
