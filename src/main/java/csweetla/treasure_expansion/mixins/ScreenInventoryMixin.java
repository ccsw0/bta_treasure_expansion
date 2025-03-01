package csweetla.treasure_expansion.mixins;

import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static csweetla.treasure_expansion.TreasureExpansion.itemStrangeDevice;

@Mixin(value = ScreenInventory.class, remap = false)
public abstract class ScreenInventoryMixin {
	/**
	 * display buttons to toggle informational overlays when player has a strange device
	 */
	@Redirect(method = "updateOverlayButtons", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/PlayerLocal;getGamemode()Lnet/minecraft/core/player/gamemode/Gamemode;"))
	public Gamemode updateOverlayButtons(PlayerLocal instance) {
		Gamemode g = instance.getGamemode();

		if (g == Gamemode.survival) {
			for (int i = 0; i < ContainerInventory.playerMainInventorySize(); ++i) {
				ItemStack is = instance.inventory.mainInventory[i];
				if (is != null && is.itemID == itemStrangeDevice.id) {
					return Gamemode.creative;
				}
			}
		}
		return g;
	}
}
