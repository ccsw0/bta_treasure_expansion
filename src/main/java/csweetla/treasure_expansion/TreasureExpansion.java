package csweetla.treasure_expansion;

import csweetla.treasure_expansion.item.*;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockChest;
import net.minecraft.client.render.block.model.BlockModelChest;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryRepairable;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.Properties;

import static csweetla.treasure_expansion.ModItemTags.fireImmuneAsEntity;
import static csweetla.treasure_expansion.ModItemTags.fizzleInWater;

import luke.bonusblocks.item.BonusItems;

@SuppressWarnings({"unchecked", "unused"})
public class TreasureExpansion implements ModInitializer, RecipeEntrypoint {
    public static final String MOD_ID = "treasure_expansion";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ConfigHandler config;
	public static boolean renderingArrow = false;
	public static boolean mod_fruit_enabled;
	public static int minor_treasure_rarity;
	public static boolean minor_treasure_enabled;

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
		prop.setProperty("ids.spider_silk", "32212");
		prop.setProperty("ids.fire_quiver", "32213");
		prop.setProperty("ids.flippers", "32214");
		prop.setProperty("ids.chest", "1930");
		prop.setProperty("loot.use_custom_tables", "false");
		prop.setProperty("loot.mod_fruit_enabled", "true");
		prop.setProperty("loot.minor_treasure_enabled", "true");
		prop.setProperty("loot.minor_treasure_rarity", "3");
		prop.setProperty("durability.escape_rope_gold", "6");
		prop.setProperty("durability.escape_rope", "1");
		prop.setProperty("durability.piston_boots", "220");
		prop.setProperty("durability.diving_helmet", "220");
		prop.setProperty("durability.lava_charm", "120");
		prop.setProperty("durability.silver_sword", "512");
		prop.setProperty("durability.fire_quiver", "192");
		prop.setProperty("durability.flippers", "192");
		prop.setProperty("durability.spider_silk", "192");


		config = new ConfigHandler(MOD_ID, prop);
	}

	public static ArmorMaterial armorMaterialPistonBoots;
	public static ArmorMaterial armorMaterialDiving;
	public static ArmorMaterial armorMaterialFlippers;
	public static Item armorItemPistonBoots;
	public static Item armorItemDivingHelmet;
	public static Item toolItemSilverSword;
	public static Item itemEscapeRope;
	public static Item itemEscapeRopeGold;
	public static Item itemStrangeDevice;
	public static Item itemLabyrinthGenerator;
	public static Item foodItemOrange;
	public static Item foodItemGrapes;
	public static Item foodItemBananas;
	public static Item foodItemFruitSalad;
	public static Item itemLavaCharm;
	public static Item itemSpiderSilk;
	public static Item itemFireQuiver;
	public static Item itemFlippers;

	public static Block blockCobbleChest;


	private void initializeArmorMaterials() {
		armorMaterialPistonBoots = ArmorHelper.createArmorMaterial(MOD_ID, "piston_boots", config.getInt("durability.diving_helmet"), 50.0F, 50.0F, 20.0F, 120.0F);
		armorMaterialDiving      = ArmorHelper.createArmorMaterial(MOD_ID, "diving", config.getInt("durability.diving_helmet"), 20.0F, 60.0F, 20.0F, 20.0F);
		armorMaterialFlippers    = ArmorHelper.createArmorMaterial(MOD_ID, "flippers", -1, 0.0F, 0.0F, 0.0F, 0.0F);
	}

	private void initializeItems() {
		armorItemPistonBoots = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/piston_boots")
		    .build(new ItemArmor("piston_boots", config.getInt("ids.piston_boots"), armorMaterialPistonBoots, 3));

		armorItemDivingHelmet = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/diving_helmet")
		    .build(new ItemArmor("diving_helmet", config.getInt("ids.diving_helmet"), armorMaterialDiving, 0));

		toolItemSilverSword = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/silver_sword")
		    .setItemModel(item -> new ItemModelStandard(item, null).setFull3D())
		    .build(new SilverSwordItem("silver_sword", config.getInt("ids.silver_sword"), ToolMaterial.iron, config.getInt("durability.silver_sword")));

		itemEscapeRopeGold = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/escape_rope_gold")
		    .build(new EscapeRopeItem("escape_rope_gold", config.getInt("ids.escape_rope_gold"), config.getInt("durability.escape_rope_gold")));

		itemEscapeRope = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/escape_rope")
		    .build(new EscapeRopeItem("escape_rope", config.getInt("ids.escape_rope"), config.getInt("durability.escape_rope")));

		itemStrangeDevice = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/device")
    		.setStackSize(1)
		    .build(new Item("strange_device", config.getInt("ids.strange_device")));

		itemLabyrinthGenerator = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.setIcon(MOD_ID + ":item/device")
		    .build(new LabyrinthGeneratorItem("labyrinth_generator", config.getInt("ids.labyrinth_generator")));

		itemSpiderSilk = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/spider_silk")
			.setStackSize(1)
		    .build(new SpiderSilkItem("spider_silk", config.getInt("ids.spider_silk"), config.getInt("durability.spider_silk")));

		foodItemOrange = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/orange")
		    .build(new ItemFood("orange", config.getInt("ids.orange"), 4, 8, false, 8));

		foodItemGrapes = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/grapes")
		    .build(new ItemFood("grapes", config.getInt("ids.grapes"), 4, 3, false, 16));

		foodItemBananas = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/bananas")
		    .build(new ItemFood("bananas", config.getInt("ids.bananas"), 4, 6, false, 8));

		foodItemFruitSalad = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/fruit_salad")
		    .build(new ItemFood("fruit_salad", config.getInt("ids.fruit_salad"), 20, 5, false, 8));

		itemLavaCharm = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/lava_charm")
			.setTags(fireImmuneAsEntity, fizzleInWater)
		    .build(new LavaCharmItem("lava_charm", config.getInt("ids.lava_charm"), config.getInt("durability.lava_charm")));

		itemFireQuiver = new ItemBuilder(MOD_ID)
			.setTags(fireImmuneAsEntity, fizzleInWater)
			.setItemModel(item -> new FireQuiverItemModel(item, MOD_ID))
			.setIcon("treasure_expansion:item/fire_quiver")
		    .build(new FireQuiverItem("fire_quiver", config.getInt("ids.fire_quiver"), config.getInt("durability.fire_quiver")));

		itemFlippers = new ItemBuilder(MOD_ID)
		    .setIcon(MOD_ID + ":item/flippers")
		    .build(new ItemArmor("flippers", config.getInt("ids.flippers"), armorMaterialFlippers, 3));
	}

	private void initializeBlocks() {
		Block blockCobbleChest = new BlockBuilder(MOD_ID)
		    .setHardness(2.0f)
	        .setResistance(10.0F)
	        .addTags(BlockTags.MINEABLE_BY_PICKAXE)
	        .setBlockModel(block -> new BlockModelChest(block, "treasure_expansion:block/dungeon_chest_cobble"))
	        .build(new BlockChest("dungeon_chest.cobble", config.getInt("ids.chest"), Material.stone));
	}

	@Override
	public void onRecipesReady() {
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(Item.bowl)
			.addInput(Item.foodApple)
			.addInput(Item.foodCherry)
			.addInput(foodItemBananas)
			.addInput(foodItemGrapes)
			.addInput(foodItemOrange)
			.create("fruitSalad", foodItemFruitSalad.getDefaultStack());

		Registries.RECIPES.addCustomRecipe(
		    "treasure_expansion:workbench/repair_piston_boots",
		    new RecipeEntryRepairable(armorItemPistonBoots, Item.itemsList[Block.pistonBase.id])
	    );
	    // BonusBlocks compat
	    if (ModVersionHelper.isModPresent("bonusblocks")) {
        	Registries.RECIPES.addCustomRecipe(
        	    "treasure_expansion:workbench/repair_helm",
        	    new RecipeEntryRepairable(armorItemDivingHelmet, BonusItems.ingotCopper)
            );
        	Registries.RECIPES.addCustomRecipe(
        	    "treasure_expansion:workbench/repair_silver_sword",
        	    new RecipeEntryRepairable(toolItemSilverSword, BonusItems.ingotSilver)
            );
	    }
	}

	@Override
    public void initNamespaces() {
        RecipeBuilder.initNameSpace(MOD_ID);
    }

	@Override
    public void onInitialize() {
		mod_fruit_enabled = config.getBoolean("loot.mod_fruit_enabled");
		minor_treasure_enabled = config.getBoolean("loot.minor_treasure_enabled");
		minor_treasure_rarity = config.getInt("loot.minor_treasure_rarity");
		LootTables.initialize();
		LOGGER.info(MOD_ID + " initialized.");

		initializeArmorMaterials();
		initializeBlocks();
		initializeItems();
	}

}
