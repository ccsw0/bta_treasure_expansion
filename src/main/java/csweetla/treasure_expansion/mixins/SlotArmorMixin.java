package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.TreasureExpansion;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Slot.class, remap = false)
public class SlotArmorMixin {
	/**
	 * Allow the fire quiver to be placed in the chestplate slot in the player inventory
	 */
	@Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
	public void mayPlace(ItemStack itemstack, CallbackInfoReturnable<Boolean> cir) {
		Slot thisAs = (Slot) (Object) this;
		if (thisAs instanceof SlotArmor && itemstack != null && itemstack.getItem().equals(TreasureExpansion.itemFireQuiver)) {
			cir.setReturnValue(((SlotArmor) thisAs).armorType == 1);
		}
	}
}
