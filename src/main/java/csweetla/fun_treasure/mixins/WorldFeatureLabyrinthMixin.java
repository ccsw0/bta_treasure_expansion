package csweetla.fun_treasure.mixins;

import csweetla.fun_treasure.FunTreasure;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.generate.feature.WorldFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(value = net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth.class, remap = false)
public abstract class WorldFeatureLabyrinthMixin extends WorldFeature {

	@Shadow
	boolean treasureGenerated;
	@Shadow
	int dungeonSize;
	@Shadow
	boolean isCold;

	@Unique
	ItemStack pick_treasure_item(Random random) {
		int r = random.nextInt(2);
		if (r == 0) {
			return this.isCold ? new ItemStack(Item.armorBootsIceskates) : new ItemStack(Item.armorQuiverGold);
		} else {
			return new ItemStack(FunTreasure.armorItemPistonBoots);
		}
	}

	/**
	 * @author Csweetla
	 * @reason generate new loot items  in labyrinth
	 */
	@Overwrite
	private ItemStack pickCheckLootItem(Random random) {
		if (!this.treasureGenerated && this.dungeonSize > 7) {
			this.treasureGenerated = true;
			return pick_treasure_item(random);
		} else {
			int i = random.nextInt(16);
			if (i == 1) {
				return new ItemStack(Item.ingotIron, random.nextInt(6) + 1);
			} else if (i == 2) {
				return new ItemStack(Item.ingotGold, random.nextInt(4) + 1);
			} else if (i == 3) {
				return new ItemStack(Item.sulphur, random.nextInt(6) + 3);
			} else if (i == 4 && random.nextInt(50) == 0) {
				return new ItemStack(Item.diamond, random.nextInt(3) + 1);
			} else if (i == 5 && random.nextInt(100) == 0) {
				return new ItemStack(Item.foodAppleGold);
			} else if (i == 6) {
				return new ItemStack(Item.dustRedstone, random.nextInt(4) + 1);
			} else if (i == 7 && random.nextInt(10) == 0) {
				return new ItemStack(Item.itemsList[Item.record13.id + random.nextInt(9)]);
			} else if (i == 8) {
				return new ItemStack(Item.foodApple);
			} else if (i == 9) {
				return new ItemStack(Item.itemsList[Block.spongeDry.id], random.nextInt(4) + 1);
			} else if (i == 10 && random.nextInt(20) == 0) {
				return random.nextInt(10) == 0 ? new ItemStack(Item.handcannonLoaded) : new ItemStack(Item.handcannonUnloaded);
			} else if (i == 11 && random.nextInt(5) == 0) {
				return new ItemStack(Item.armorHelmetChainmail, 1, Item.armorHelmetChainmail.getMaxDamage() - random.nextInt(Item.armorHelmetChainmail.getMaxDamage() / 2));
			} else if (i == 12 && random.nextInt(5) == 0) {
				return new ItemStack(Item.armorChestplateChainmail, 1, Item.armorChestplateChainmail.getMaxDamage() - random.nextInt(Item.armorChestplateChainmail.getMaxDamage() / 2));
			} else if (i == 13 && random.nextInt(5) == 0) {
				return new ItemStack(Item.armorBootsChainmail, 1, Item.armorBootsChainmail.getMaxDamage() - random.nextInt(Item.armorBootsChainmail.getMaxDamage() / 2));
			} else if (i == 14 && random.nextInt(5) == 0) {
				return new ItemStack(Item.armorLeggingsChainmail, 1, Item.armorLeggingsChainmail.getMaxDamage() - random.nextInt(Item.armorLeggingsChainmail.getMaxDamage() / 2));
			} else {
				return i == 15 && random.nextInt(10) == 0 ? new ItemStack(Item.ingotSteelCrude, random.nextInt(3) + 1) : null;
			}
		}
	}
}
