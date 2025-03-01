package csweetla.treasure_expansion.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import csweetla.treasure_expansion.LootTables;
import net.minecraft.core.WeightedRandomBag;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static csweetla.treasure_expansion.LootTables.LootTableType.*;
import static csweetla.treasure_expansion.TreasureExpansion.*;

@Mixin(value = net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth.class, remap = false)
public abstract class WorldFeatureLabyrinthMixin extends WorldFeature {
	@Shadow
	public WeightedRandomBag<WeightedRandomLootObject> chestLoot;
	@Shadow
	boolean treasureGenerated;
	@Shadow
	int dungeonSize;
	@Shadow
	boolean isCold;
	@Shadow
	int wallBlockA;
	@Unique
	Item fruit_item = Items.FOOD_APPLE;

	@Unique
	boolean generate_minor_treasure = false;

	@Unique
	Item random_fruit_item(Random random) {
		Item[] choices = {Items.FOOD_APPLE, foodItemOrange, foodItemBananas, foodItemGrapes, foodItemPear};
		return choices[random.nextInt(choices.length)];
	}

	@Unique
	ItemStack pick_major_treasure_item(Random random) {
		ItemStack ret;
		if (this.isCold) {
			ret = LootTables.getInstance().randomLoot(SNOW, random);
		} else if (this.wallBlockA == Blocks.SANDSTONE.id()) {
			ret = LootTables.getInstance().randomLoot(DESERT, random);
		} else {
			ret = LootTables.getInstance().randomLoot(DEFAULT, random);
		}
//		TreasureExpansion.LOGGER.info("Generated major treasure: " + ret);
		return ret;
	}

	@Unique
	ItemStack pick_minor_treasure_item(Random random) {
		ItemStack ret = LootTables.getInstance().randomLoot(MINOR, random);
//		TreasureExpansion.LOGGER.info("Generated minor treasure: " + ret);
		return ret;
	}

	/**
	 * pick a random fruit item to generate in the labyrinth (if feature enabled) & decide if a minor treasure item
	 * will be generated, based on the chance in the config file
	 */
	@Inject(method = "place", at = @At("HEAD"))
	void generate(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		this.fruit_item = mod_fruit_enabled ? random_fruit_item(random) : Items.FOOD_APPLE;
		this.generate_minor_treasure = minor_treasure_enabled && random.nextInt(minor_treasure_rarity) == 0;
	}

	/**
	 * Add treasure scrap to the labyrinth loot table
	 */
	@Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/WeightedRandomBag;addEntry(Ljava/lang/Object;D)V", ordinal = 0))
	void addTreasureScrap(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		this.chestLoot.addEntry(new WeightedRandomLootObject(itemTreasureScrap.getDefaultStack(), 1, 6), 25.0);
	}

	/**
	 * generate major and minor treasures  (once per labyrinth)
	 */
	@Inject(method = "pickCheckLootItem", at = @At("HEAD"), cancellable = true)
	private void pickCheckLootItem(Random random, CallbackInfoReturnable<ItemStack> cir) {
		if (!this.treasureGenerated && this.dungeonSize > 7) {
			this.treasureGenerated = true;
			cir.setReturnValue(pick_major_treasure_item(random));
		} else if (this.generate_minor_treasure && this.dungeonSize > 4) {
			this.generate_minor_treasure = false;
			cir.setReturnValue(pick_minor_treasure_item(random));
		}
	}

	/**
	 * return our chosen fruit rather than apples
	 */
	@Inject(method = "pickCheckLootItem", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void pickCheckLootItem1(Random random, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack ret = cir.getReturnValue();
		if (ret != null && ret.getItem().equals(Items.FOOD_APPLE)) {
			cir.setReturnValue(new ItemStack(this.fruit_item));
		}
	}

	/**
	 * Use mod chests (based on type of labyrinth) instead of the regular oak chest
	 */
	@WrapOperation(method = "generateDungeon", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockWithNotify(IIII)Z"))
	private boolean generateDungeon1(World instance, int x, int y, int z, int id, Operation<Boolean> original) {
		if (id != Blocks.CHEST_PLANKS_OAK.id())
			return original.call(instance, x, y, z, id);

		int chest_id;
		if (this.isCold) {
			chest_id = blockIceChest.id();
		} else if (this.wallBlockA == Blocks.SANDSTONE.id()) {
			chest_id = blockSandstoneChest.id();
		} else {
			chest_id = blockCobbleChest.id();
		}

		return instance.setBlockWithNotify(x, y, z, chest_id);
	}

	/**
	 * Don't allow the mod chests to be replaced when generating the structure
	 */
	@Inject(method = "canReplace", at = @At(value = "HEAD"), cancellable = true)
	private void canReplace(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		int id = world.getBlockId(x, y, z);
		if (id == blockCobbleChest.id() || id == blockIceChest.id() || id == blockSandstoneChest.id())
			cir.setReturnValue(false);
	}
}
