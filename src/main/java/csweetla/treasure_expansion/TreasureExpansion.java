package csweetla.treasure_expansion;

import csweetla.treasure_expansion.block.BlockLogicDiscoJukebox;
import csweetla.treasure_expansion.block.TileEntityJukeboxDisco;
import csweetla.treasure_expansion.entity.EntityVampire;
import csweetla.treasure_expansion.item.*;

import csweetla.treasure_expansion.item.recipes.RecipeEntryTreasureScrap;
import net.fabricmc.api.ModInitializer;

import net.minecraft.client.gui.guidebook.crafting.RecipePageCrafting;
import net.minecraft.client.gui.guidebook.crafting.displays.DisplayAdapterShapeless;
import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryRepairable;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.*;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.material.ArmorMaterial;

import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.*;

import static csweetla.treasure_expansion.ModItemTags.fireImmuneAsEntity;
import static csweetla.treasure_expansion.ModItemTags.fizzleInWater;

@SuppressWarnings({"unused"})
public class TreasureExpansion implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
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
		prop.setProperty("ids.treasure_scrap", "32215");
		prop.setProperty("ids.pear", "32216");

		prop.setProperty("ids.chest_cobble", "1930");
		prop.setProperty("ids.chest_sand", "1931");
		prop.setProperty("ids.chest_frost", "1932");

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
	public static Item foodItemPear;
	public static Item foodItemFruitSalad;
	public static Item itemLavaCharm;
	public static Item itemSpiderSilk;
	public static Item itemFireQuiver;
	public static Item itemFlippers;
	public static Item itemTreasureScrap;

	public static Block<BlockLogic> blockCobbleChest;
	public static Block<BlockLogic> blockSandstoneChest;
	public static Block<BlockLogic> blockIceChest;
	public static Block<BlockLogicDiscoJukebox> blockDiscoJukebox;



	private void initializeArmorMaterials() {
		armorMaterialPistonBoots = ArmorHelper.createArmorMaterial(MOD_ID, "piston_boots", config.getInt("durability.piston_boots"), 50.0F, 50.0F, 20.0F, 120.0F);
		armorMaterialDiving      = ArmorHelper.createArmorMaterial(MOD_ID, "diving", config.getInt("durability.diving_helmet"), 20.0F, 60.0F, 20.0F, 20.0F);
		armorMaterialFlippers    = ArmorHelper.createArmorMaterial(MOD_ID, "flippers", config.getInt("durability.flippers"), 0.0F, 0.0F, 0.0F, 100.0F);
	}

	private void initializeItems() {
		armorItemPistonBoots = new ItemBuilder(MOD_ID)
		    .build(new ItemArmor("piston_boots", MOD_ID + ":piston_boots", config.getInt("ids.piston_boots"), armorMaterialPistonBoots, 0));

		armorItemDivingHelmet = new ItemBuilder(MOD_ID)
		    .build(new ItemArmor("diving_helmet", MOD_ID + ":diving_helmet", config.getInt("ids.diving_helmet"), armorMaterialDiving, 3));

		toolItemSilverSword = new ItemBuilder(MOD_ID)
			.addTags(ModItemTags.noVampireDamagePenalty)
		    .build(new SilverSwordItem("silver_sword", config.getInt("ids.silver_sword"), ToolMaterial.iron, config.getInt("durability.silver_sword")));

		itemEscapeRopeGold = new ItemBuilder(MOD_ID)
		    .build(new EscapeRopeItem("escape_rope_gold", config.getInt("ids.escape_rope_gold"), config.getInt("durability.escape_rope_gold")));

		itemEscapeRope = new ItemBuilder(MOD_ID)
		    .build(new EscapeRopeItem("escape_rope", config.getInt("ids.escape_rope"), config.getInt("durability.escape_rope")));

		itemStrangeDevice = new ItemBuilder(MOD_ID)
    		.setStackSize(1)
		    .build(new Item("strange_device", MOD_ID + ":strange_device", config.getInt("ids.strange_device")));

		itemLabyrinthGenerator = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.build(new LabyrinthGeneratorItem("labyrinth_generator", config.getInt("ids.labyrinth_generator")));

		itemSpiderSilk = new ItemBuilder(MOD_ID)
			.setStackSize(1)
			.setMaxDamage(config.getInt("durability.spider_silk"))
		    .build(new Item("spider_silk", MOD_ID + ":spider_silk", config.getInt("ids.spider_silk")));

		foodItemOrange = new ItemBuilder(MOD_ID)
		    .build(new ItemFood("orange", MOD_ID + ":orange", config.getInt("ids.orange"), 4, 8, false, 8));

		foodItemGrapes = new ItemBuilder(MOD_ID)
		    .build(new ItemFood("grapes", MOD_ID + ":grapes", config.getInt("ids.grapes"), 4, 3, false, 8));

		foodItemPear = new ItemBuilder(MOD_ID)
			.build(new ItemFood("pear", MOD_ID + ":pear", config.getInt("ids.pear"), 4, 3, false, 8));

		foodItemBananas = new ItemBuilder(MOD_ID)
		    .build(new ItemFood("bananas", MOD_ID + ":bananas", config.getInt("ids.bananas"), 4, 6, false, 8));

		foodItemFruitSalad = new ItemBuilder(MOD_ID)
		    .build(new ItemFood("fruit_salad", MOD_ID + ":fruit_salad", config.getInt("ids.fruit_salad"), 20, 5, false, 8));

		itemLavaCharm = new ItemBuilder(MOD_ID)
			.setTags(fireImmuneAsEntity, fizzleInWater)
		    .build(new LavaCharmItem("lava_charm", config.getInt("ids.lava_charm"), config.getInt("durability.lava_charm")));

		itemFireQuiver = new ItemBuilder(MOD_ID)
			.setTags(fireImmuneAsEntity, fizzleInWater)
		    .build(new FireQuiverItem("fire_quiver", config.getInt("ids.fire_quiver"), config.getInt("durability.fire_quiver")));

		itemFlippers = new ItemBuilder(MOD_ID)
		    .build(new ItemArmor("flippers", MOD_ID + ":flippers", config.getInt("ids.flippers"), armorMaterialFlippers, 0));

		itemTreasureScrap = new ItemBuilder(MOD_ID)
			.build(new Item("treasure_scrap", MOD_ID + ":treasure_scrap", config.getInt("ids.treasure_scrap")));
	}

	@Override
	public void beforeGameStart() {
		EntityHelper.createTileEntity(TileEntityJukeboxDisco.class, NamespaceID.getPermanent(MOD_ID,"disco_jukebox"));

		EntityHelper.createEntity(EntityVampire.class, NamespaceID.getPermanent(MOD_ID,"vampire"),"mob." + MOD_ID + ".vampire.name","Vampire", 312);
	}

	@Override
	public void afterGameStart() {
		MobInfoRegistry.register(EntityVampire.class, "mob." + MOD_ID + ".vampire.name", "mob." + MOD_ID + ".vampire.desc", 25, 1200,
			new MobInfoRegistry.MobDrop[]{new MobInfoRegistry.MobDrop(Items.QUARTZ.getDefaultStack(), 1.0F, 3, 4)}
		);

		ModItemTags.noVampireDamagePenalty.tag(Items.TOOL_SWORD_WOOD);
	}

	private static class BlockLogicChestDoesntNeedProperTool extends BlockLogicChest {
		public BlockLogicChestDoesntNeedProperTool(Block<?> block, Material material) {
			super(block, material);
		}

		@Nullable
		@Override
		public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
			return new ItemStack[]{new ItemStack(this.block)};
		}
	}

	private void initializeBlocks() {
		blockCobbleChest = new BlockBuilder(MOD_ID)
			.setHardness(Blocks.COBBLE_STONE.getHardness())
	        .setResistance(Blocks.COBBLE_STONE.blastResistance / 3.0f)
			.setBlockSound(BlockSounds.STONE)
	        .addTags(BlockTags.FENCES_CONNECT,BlockTags.MINEABLE_BY_PICKAXE)
			.build("dungeon_chest.cobble","dungeon_chest_cobble",config.getInt("ids.chest_cobble"), b -> new BlockLogicChestDoesntNeedProperTool(b, Material.stone)).withDisabledNeighborNotifyOnMetadataChange();

		blockSandstoneChest = new BlockBuilder(MOD_ID)
			.setHardness(Blocks.SAND.getHardness())
			.setResistance(Blocks.SAND.blastResistance / 3.0f)
			.setBlockSound(BlockSounds.SAND)
			.addTags(BlockTags.FENCES_CONNECT,BlockTags.MINEABLE_BY_SHOVEL)
			.build("dungeon_chest.sandstone","dungeon_chest_sandstone",config.getInt("ids.chest_sand"), b -> new BlockLogicChestDoesntNeedProperTool(b, Material.sand)).withDisabledNeighborNotifyOnMetadataChange();

		blockIceChest = new BlockBuilder(MOD_ID)
			.setHardness(Blocks.PERMAFROST.getHardness())
			.setResistance(Blocks.PERMAFROST.blastResistance / 3.0f)
			.setBlockSound(BlockSounds.GLASS)
			.addTags(BlockTags.FENCES_CONNECT,BlockTags.MINEABLE_BY_PICKAXE)
			.build("dungeon_chest.frost","dungeon_chest_frost",config.getInt("ids.chest_frost") + 2, b -> new BlockLogicChestDoesntNeedProperTool(b, Material.stone)).withDisabledNeighborNotifyOnMetadataChange();

		blockDiscoJukebox = new BlockBuilder(MOD_ID)
			.setHardness(Blocks.JUKEBOX.getHardness())
			.setResistance(Blocks.JUKEBOX.blastResistance / 3.0f)
			.setTags(BlockTags.MINEABLE_BY_AXE)
			.build("jukebox.disco","disco_jukebox",config.getInt("ids.chest_frost"), BlockLogicDiscoJukebox::new);

	}

	@Override
	public void onRecipesReady() {
		RecipeBuilder.Shapeless(MOD_ID)
			.addInput(Items.BOWL)
			.addInput(Items.FOOD_APPLE)
			.addInput(Items.FOOD_CHERRY)
			.addInput(foodItemBananas)
			.addInput(foodItemGrapes)
			.addInput(foodItemOrange)
			.addInput(foodItemPear)
			.create("fruitSalad", foodItemFruitSalad.getDefaultStack());

		Registries.RECIPES.addCustomRecipe(
			MOD_ID + ":workbench/repair_silver_sword",
			new RecipeEntryRepairable(toolItemSilverSword.getDefaultStack(),new RecipeSymbol(itemTreasureScrap.getDefaultStack()))
		);

		Registries.RECIPES.addCustomRecipe(
			MOD_ID + ":workbench/repair_piston_boots",
			new RecipeEntryRepairable(armorItemPistonBoots.getDefaultStack(),new RecipeSymbol(itemTreasureScrap.getDefaultStack()))
		);

		Registries.RECIPES.addCustomRecipe(
			MOD_ID + ":workbench/repair_diving_helmet",
			new RecipeEntryRepairable(armorItemDivingHelmet.getDefaultStack(),new RecipeSymbol(itemTreasureScrap.getDefaultStack()))
		);

		Registries.RECIPES.addCustomRecipe(
			MOD_ID + ":workbench/repair_flippers",
			new RecipeEntryRepairable(itemFlippers.getDefaultStack(),new RecipeSymbol(itemTreasureScrap.getDefaultStack()))
		);

		Registries.RECIPE_TYPES.register(MOD_ID + ":crafting/treasure_scrap", RecipeEntryTreasureScrap.class);
		RecipePageCrafting.recipeToDisplayAdapterMap.put(RecipeEntryTreasureScrap.class, new DisplayAdapterShapeless() );

		Map<Item,Integer> scrap_items = new HashMap<>();

		scrap_items.put(itemFireQuiver,6);
		scrap_items.put(Items.ARMOR_QUIVER_GOLD, 6);
		scrap_items.put(armorItemPistonBoots, 6);
		scrap_items.put(armorItemDivingHelmet, 6);
		scrap_items.put(itemStrangeDevice, 6);
		scrap_items.put(itemEscapeRopeGold, 6);
		scrap_items.put(itemLavaCharm, 6);
		scrap_items.put(itemSpiderSilk, 6);

		scrap_items.put(toolItemSilverSword, 3);
		scrap_items.put(itemFlippers, 4);
		scrap_items.put(itemEscapeRope, 2);


		for (Map.Entry<Item, Integer> entry : scrap_items.entrySet()) {

			// get key without prefixes and such
			String item_name = entry.getKey().getKey()
				.replace('.', '_')
				.replaceFirst("^item_","")
				.replaceFirst(MOD_ID + "_","");

			Registries.RECIPES.addCustomRecipe(MOD_ID + ":workbench/scrap_" + item_name,
				new RecipeEntryTreasureScrap(entry.getKey(),itemTreasureScrap,entry.getValue())
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
