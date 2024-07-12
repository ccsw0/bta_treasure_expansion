package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.LootTables;
import csweetla.treasure_expansion.TreasureExpansion;


import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
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
	boolean treasureGenerated;
	@Shadow
	int dungeonSize;
	@Shadow
	boolean isCold;

	@Shadow
	int wallBlockA;

	@Unique
	Item fruit_item = Item.foodApple;

	@Unique
	boolean generate_minor_treasure = false;

	@Unique
	Item random_fruit_item(Random random) {
		Item[] choices = {Item.foodApple, TreasureExpansion.foodItemOrange, TreasureExpansion.foodItemBananas, TreasureExpansion.foodItemGrapes};
		return choices[random.nextInt(choices.length)];
	}

	@Unique
	ItemStack pick_major_treasure_item(Random random) {
		ItemStack ret;
		if (this.isCold) {
			ret = LootTables.getInstance().randomLoot(SNOW, random);
		} else if (this.wallBlockA == Block.sandstone.id) {
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
	// pick a random fruit item to generate in the labyrinth
	@Inject(method = "generate",at = @At("HEAD"))
	void generate(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		this.fruit_item = mod_fruit_enabled ? random_fruit_item(random) : Item.foodApple;
		this.generate_minor_treasure = minor_treasure_enabled && random.nextInt(minor_treasure_rarity) == 0;
	}

	// generate major and minor treasures
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

	// return our chosen fruit rather than apples
	@Inject(method = "pickCheckLootItem", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void pickCheckLootItem1(Random random, CallbackInfoReturnable<ItemStack> cir) {
	    ItemStack ret = cir.getReturnValue();
	    if (ret != null && ret.getItem() == Item.foodApple) {
            cir.setReturnValue(new ItemStack(this.fruit_item));
        }
	}
}
