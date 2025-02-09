package csweetla.treasure_expansion;

import csweetla.treasure_expansion.item.FireQuiverItemModel;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelChest;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;
import static csweetla.treasure_expansion.TreasureExpansion.*;

public class TreasureExpansionModelEntrypoint implements ModelEntrypoint {

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		ModelHelper.setBlockModel(blockCobbleChest, () -> {
			BlockModelChest<?> bmc = (BlockModelChest<?>) new BlockModelChest<>(blockCobbleChest, MOD_ID + ":block/dungeon_chest/cobble/");
			bmc.setAllTextures(0,MOD_ID + ":block/dungeon_chest/cobble/top");
			return bmc;
		});

		ModelHelper.setBlockModel(blockSandstoneChest, () -> {
			BlockModelChest<?> bmc = (BlockModelChest<?>) new BlockModelChest<>(blockSandstoneChest, MOD_ID + ":block/dungeon_chest/sandstone/");
			bmc.setAllTextures(0,MOD_ID + ":block/dungeon_chest/sandstone/top");
			return bmc;
		});

		ModelHelper.setBlockModel(blockIceChest, () -> {
			BlockModelChest<?> bmc = (BlockModelChest<?>) new BlockModelChest<>(blockIceChest, MOD_ID + ":block/dungeon_chest/frost/");
			bmc.setAllTextures(0,MOD_ID + ":block/dungeon_chest/frost/top");
			return bmc;
		});
	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {

		ModelHelper.setItemModel(armorItemPistonBoots, () -> {
			ItemModelStandard im = new ItemModelStandard(armorItemPistonBoots, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/piston_boots"));
			return im;
		});

		ModelHelper.setItemModel(armorItemDivingHelmet, () -> {
			ItemModelStandard im = new ItemModelStandard(armorItemDivingHelmet, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/diving_helmet"));
			return im;
		});

		ModelHelper.setItemModel(toolItemSilverSword, () -> {
			ItemModelStandard im = new ItemModelStandard(toolItemSilverSword, MOD_ID);
			im.setFull3D();
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/silver_sword"));
			return im;
		});

		ModelHelper.setItemModel(itemEscapeRopeGold, () -> {
			ItemModelStandard im = new ItemModelStandard(itemEscapeRopeGold, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/escape_rope_gold"));
			return im;
		});

		ModelHelper.setItemModel(itemEscapeRope, () -> {
			ItemModelStandard im = new ItemModelStandard(itemEscapeRope, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/escape_rope"));
			return im;
		});

		ModelHelper.setItemModel(itemStrangeDevice, () -> {
			ItemModelStandard im = new ItemModelStandard(itemStrangeDevice, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/device"));
			return im;
		});

		ModelHelper.setItemModel(itemLabyrinthGenerator, () -> {
			ItemModelStandard im = new ItemModelStandard(itemLabyrinthGenerator, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/device"));
			return im;
		});

		ModelHelper.setItemModel(itemSpiderSilk, () -> {
			ItemModelStandard im = new ItemModelStandard(itemSpiderSilk, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/spider_silk"));
			return im;
		});

		ModelHelper.setItemModel(foodItemOrange, () -> {
			ItemModelStandard im = new ItemModelStandard(foodItemOrange, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/orange"));
			return im;
		});

		ModelHelper.setItemModel(foodItemGrapes, () -> {
			ItemModelStandard im = new ItemModelStandard(foodItemGrapes, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/grapes"));
			return im;
		});

		ModelHelper.setItemModel(foodItemBananas, () -> {
			ItemModelStandard im = new ItemModelStandard(foodItemBananas, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/bananas"));
			return im;
		});

		ModelHelper.setItemModel(foodItemFruitSalad, () -> {
			ItemModelStandard im = new ItemModelStandard(foodItemFruitSalad, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/fruit_salad"));
			return im;
		});

		ModelHelper.setItemModel(itemLavaCharm, () -> {
			ItemModelStandard im = new ItemModelStandard(itemLavaCharm, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/lava_charm"));
			return im;
		});

		ModelHelper.setItemModel(itemFireQuiver, () -> {
			FireQuiverItemModel im = new FireQuiverItemModel(itemFireQuiver, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/fire_quiver"));
			return im;
		});

		ModelHelper.setItemModel(itemFlippers, () -> {
			ItemModelStandard im = new ItemModelStandard(itemFlippers, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/flippers"));
			return im;
		});

	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
