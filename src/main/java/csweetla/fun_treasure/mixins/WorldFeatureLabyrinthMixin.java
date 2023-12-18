package csweetla.fun_treasure.mixins;

import csweetla.fun_treasure.FunTreasure;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
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

	@Unique
	ItemStack pick_treasure_item(Random random) {
		int r = random.nextInt(2);
		if (r == 0) {
			return this.isCold ? new ItemStack(Item.armorBootsIceskates) : new ItemStack(Item.armorQuiverGold);
		} else {
			return new ItemStack(FunTreasure.armorItemPistonBoots);
		}
	}

	@Inject(method = "pickCheckLootItem", at = @At("HEAD"), cancellable = true)
	private void pickCheckLootItem(Random random, CallbackInfoReturnable<ItemStack> cir) {
		if (!this.treasureGenerated && this.dungeonSize > 7) {
			this.treasureGenerated = true;
			cir.setReturnValue(pick_treasure_item(random));
		}
	}
}
