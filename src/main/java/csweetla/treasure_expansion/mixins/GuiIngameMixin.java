package csweetla.treasure_expansion.mixins;


import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static csweetla.treasure_expansion.TreasureExpansion.itemStrangeDevice;

@Mixin(value = HudIngame.class, remap = false)
public abstract class GuiIngameMixin extends Gui {

	/**
	 * Allow information overlays when holding the alien device (by 'lying' about our gamemode)
	 */
	@Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/PlayerLocal;getGamemode()Lnet/minecraft/core/player/gamemode/Gamemode;", ordinal = 0))
	public Gamemode updateOverlayButtons2(PlayerLocal entity_player) {
		Gamemode g = entity_player.getGamemode();

		if (g == Gamemode.survival) {
			for (int i = 0; i < ContainerInventory.playerMainInventorySize(); ++i) {
				ItemStack is = entity_player.inventory.mainInventory[i];
				if (is != null && is.itemID == itemStrangeDevice.id) {
					return Gamemode.creative;
				}
			}
		}
		return g;
	}

}
