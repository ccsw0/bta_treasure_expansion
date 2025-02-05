package csweetla.treasure_expansion;

import csweetla.treasure_expansion.item.*;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.*;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.material.ArmorMaterial;

import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.Properties;

import static csweetla.treasure_expansion.ModItemTags.fireImmuneAsEntity;
import static csweetla.treasure_expansion.ModItemTags.fizzleInWater;

@SuppressWarnings({"unused"})
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

	public static Block<BlockLogic> blockCobbleChest;
	public static Block<BlockLogic> blockSandstoneChest;
	public static Block<BlockLogic> blockIceChest;



	private void initializeArmorMaterials() {
		armorMaterialPistonBoots = ArmorHelper.createArmorMaterial(MOD_ID, "piston_boots", config.getInt("durability.diving_helmet"), 50.0F, 50.0F, 20.0F, 120.0F);
		armorMaterialDiving      = ArmorHelper.createArmorMaterial(MOD_ID, "diving", config.getInt("durability.diving_helmet"), 20.0F, 60.0F, 20.0F, 20.0F);
		armorMaterialFlippers    = ArmorHelper.createArmorMaterial(MOD_ID, "flippers", config.getInt("durability.flippers"), 0.0F, 0.0F, 0.0F, 0.0F);
	}

	private void initializeItems() {
		armorItemPistonBoots = new ItemBuilder(MOD_ID)
		    .build(new ItemArmor("piston_boots", MOD_ID + ":piston_boots", config.getInt("ids.piston_boots"), armorMaterialPistonBoots, 0));

		armorItemDivingHelmet = new ItemBuilder(MOD_ID)
		    .build(new ItemArmor("diving_helmet", MOD_ID + ":diving_helmet", config.getInt("ids.diving_helmet"), armorMaterialDiving, 3));

		toolItemSilverSword = new ItemBuilder(MOD_ID)
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
	};

	private void initializeBlocks() {
		blockCobbleChest = new BlockBuilder(MOD_ID)
			.setHardness(2.0f)
	        .setResistance(3.0F)
			.setBlockSound(BlockSounds.STONE)
	        .addTags(BlockTags.FENCES_CONNECT,BlockTags.MINEABLE_BY_PICKAXE)
			.build("dungeon_chest.cobble","dungeon_chest_cobble",config.getInt("ids.chest"), b -> new BlockLogicChestDoesntNeedProperTool(b, Material.stone)).withDisabledNeighborNotifyOnMetadataChange();

		blockSandstoneChest = new BlockBuilder(MOD_ID)
			.setHardness(0.6f)
			.setResistance(2.0F)
			.setBlockSound(BlockSounds.SAND)
			.addTags(BlockTags.FENCES_CONNECT,BlockTags.MINEABLE_BY_SHOVEL)
			.build("dungeon_chest.sandstone","dungeon_chest_sandstone",config.getInt("ids.chest") + 1, b -> new BlockLogicChestDoesntNeedProperTool(b, Material.sand)).withDisabledNeighborNotifyOnMetadataChange();

		blockIceChest = new BlockBuilder(MOD_ID)
			.setHardness(1.2f)
			.setResistance(2.5F)
			.setBlockSound(BlockSounds.GLASS)
			.addTags(BlockTags.FENCES_CONNECT,BlockTags.MINEABLE_BY_PICKAXE)
			.build("dungeon_chest.frost","dungeon_chest_frost",config.getInt("ids.chest") + 2, b -> new BlockLogicChestDoesntNeedProperTool(b, Material.stone)).withDisabledNeighborNotifyOnMetadataChange();

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
			.create("fruitSalad", foodItemFruitSalad.getDefaultStack());
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
