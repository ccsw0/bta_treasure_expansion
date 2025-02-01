package csweetla.treasure_expansion.mixins;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static csweetla.treasure_expansion.TreasureExpansion.armorItemDivingHelmet;

@Mixin(value = net.minecraft.client.gui.hud.component.HudComponentOxygenBar.class, remap = false)
public class HudComponentOxygenBarMixin {
	// don't draw oxygen bar when player wears a diving helmet
	@Inject(method = "isVisible",at = @At("HEAD"), cancellable = true)
	void isVisible(Minecraft mc, CallbackInfoReturnable<Boolean> cir) {
		if (mc.thePlayer.inventory.armorInventory[3] != null && mc.thePlayer.inventory.armorInventory[3].itemID == armorItemDivingHelmet.id) {
			cir.setReturnValue(false);
		}
	}
}
