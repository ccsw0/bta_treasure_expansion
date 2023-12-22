package csweetla.fun_treasure;

import csweetla.fun_treasure.item.EscapeRopeItem;
import csweetla.fun_treasure.item.LabyrinthGeneratorItem;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.item.tool.ItemToolSword;
import turniplabs.halplibe.helper.*;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunTreasure implements ModInitializer {
    public static final String MOD_ID = "fun_treasure";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ArmorMaterial armorMaterialPistonBoots;
	public static ArmorMaterial armorMaterialDiving;
	public static Item armorItemPistonBoots;
	public static Item armorItemDivingHelmet;
	public static Item toolItemSilverSword;
	public static Item ItemEscapeRope;
	public static Item ItemEscapeRopeGold;
	public static Item ItemStrangeDevice;
	public static Item ItemLabyrinthGenerator;
	public static ItemFood FoodItemOrange;
	public static ItemFood FoodItemGrapes;
	public static ItemFood FoodItemBananas;
	public static ItemFood FoodItemFruitSalad;

	public void initializeSounds() {
		SoundHelper.addSound(MOD_ID,"rope_whoosh.ogg");
	}

	public void initializeArmorMaterials() {
		armorMaterialPistonBoots = ArmorHelper.createArmorMaterial("piston_boots",220,50.0F,50.0F,20.0F,120.0F);
		armorMaterialDiving = ArmorHelper.createArmorMaterial("diving",220,20.0F,60.0F,20.0F,20.0F);
	}

	public void initializeItems() {
		int[] tex_coords;
		int curr_id = ItemHelper.findOpenIds(10);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"piston_boots.png");
		armorItemPistonBoots = new ItemArmor(MOD_ID + ".piston_boots", curr_id++, armorMaterialPistonBoots, 3).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"diving_helmet.png");
		armorItemDivingHelmet = new ItemArmor(MOD_ID + ".diving_helmet", curr_id++, armorMaterialDiving, 0).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"silver_sword.png");
		//noinspection unchecked
		toolItemSilverSword =  new ItemToolSword(MOD_ID + ".sword_silver", curr_id++, ToolMaterial.iron).setIconCoord(tex_coords[0],tex_coords[1]).withTags(ItemTags.preventCreativeMining);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"escape_rope_gold.png");
		ItemEscapeRopeGold = new EscapeRopeItem(MOD_ID + ".escape_rope_gold", curr_id++, 6).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"escape_rope.png");
		ItemEscapeRope = new EscapeRopeItem(MOD_ID + ".escape_rope", curr_id++, 1).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"device.png");
		ItemStrangeDevice = new Item(MOD_ID + ".strange_device", curr_id++).setIconCoord(tex_coords[0],tex_coords[1]).setMaxStackSize(1);

		ItemLabyrinthGenerator = new LabyrinthGeneratorItem(MOD_ID + ".labyrinth_generator", curr_id++).setIconCoord(tex_coords[0],tex_coords[1]).setMaxStackSize(1);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"orange.png");
		FoodItemOrange = (ItemFood) new ItemFood(MOD_ID + ".orange",curr_id++,4,false).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"grapes.png");
		FoodItemGrapes = (ItemFood) new ItemFood(MOD_ID + ".grapes",curr_id++,4,false).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"bananas.png");
		FoodItemBananas = (ItemFood) new ItemFood(MOD_ID + ".bananas",curr_id++,4,false).setIconCoord(tex_coords[0],tex_coords[1]);

		tex_coords = TextureHelper.getOrCreateItemTexture(MOD_ID,"fruit_salad.png");
		FoodItemFruitSalad = (ItemFood) new ItemFood(MOD_ID + ".fruit_salad",curr_id++,20,false).setIconCoord(tex_coords[0],tex_coords[1]);
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

		initializeSounds();
		initializeArmorMaterials();
		initializeItems();
		initializeRecipes();

	}
}
