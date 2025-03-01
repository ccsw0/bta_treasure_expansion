package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.ModItemTags;
import csweetla.treasure_expansion.entity.EntityVampire;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemToolSword.class, remap = false)
public class ItemToolSwordMixin {

	@Shadow
	private int weaponDamage;

	@Inject(method = "getDamageVsEntity", at = @At("HEAD"), cancellable = true)
	public void getDamageVsEntity(Entity entity, ItemStack is, CallbackInfoReturnable<Integer> cir) {
		if (entity instanceof EntityVampire) {
			if (!ModItemTags.noVampireDamagePenalty.appliesTo(is.getItem()))
				cir.setReturnValue(this.weaponDamage / 2);
			else if (Items.TOOL_SWORD_WOOD.equals(is.getItem())) {
				cir.setReturnValue(4 + ToolMaterial.iron.getDamage() * 2);
			}
		}
	}
}
