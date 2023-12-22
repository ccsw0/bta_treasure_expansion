package csweetla.fun_treasure.mixins;

import csweetla.fun_treasure.FunTreasure;

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

@Mixin(value = net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth.class, remap = false)
public abstract class WorldFeatureLabyrinthMixin extends WorldFeature {

	@Shadow
	boolean treasureGenerated;
	@Shadow
	int dungeonSize;
	@Shadow
	boolean isCold;

	// if we see a stick something isn't working
	@Unique
	Item fruit_item = Item.stick;

	@Unique
	boolean generate_minor_treasure = false;

	@Unique
	ItemStack pick_major_treasure_item(Random random) {

		// if its cold, 75% chance of ice skates
		if (this.isCold && random.nextInt(4) != 0) {
			return new ItemStack(Item.armorBootsIceskates);
		}

		int r = random.nextInt(5);
		switch (r) {
			case 0:
				return new ItemStack(FunTreasure.ItemEscapeRopeGold);
			case 1:
				return new ItemStack(FunTreasure.armorItemPistonBoots);
			case 2:
				return new ItemStack(FunTreasure.armorItemDivingHelmet);
			case 3:
				return new ItemStack(FunTreasure.ItemStrangeDevice);
			default:
				return new ItemStack(Item.armorQuiverGold);
		}
	}

	@Unique
	ItemStack pick_minor_treasure_item(Random random) {
		int r = random.nextInt(3);
		if (r == 0) {
			return new ItemStack(FunTreasure.ItemEscapeRope);
		} else if (r == 1) {
			return new ItemStack(FunTreasure.toolItemSilverSword);
		} else {
			return new ItemStack(Item.diamond, 6 + random.nextInt(6));
		}
	}
	// pick a random fruit item to generate in the labyrinth
	@Inject(method = "generate",at = @At("HEAD"))
	void generate(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		int r = random.nextInt(4);
		if (r == 0) {
			this.fruit_item = Item.foodApple;
		} else if (r == 1) {
			this.fruit_item = FunTreasure.FoodItemBananas;
		} else if (r == 2) {
			this.fruit_item = FunTreasure.FoodItemOrange;
		} else {
			this.fruit_item = FunTreasure.FoodItemGrapes;
		}
		this.generate_minor_treasure = random.nextInt(3) == 0;
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

	// return our chosen fruit than apples
	@Inject(method = "pickCheckLootItem", at = @At(value = "RETURN", ordinal = 9), cancellable = true)
	private void pickCheckLootItem1(Random random, CallbackInfoReturnable<ItemStack> cir) {
		cir.setReturnValue(new ItemStack(this.fruit_item));
	}
}
