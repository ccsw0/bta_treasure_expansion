package csweetla.treasure_expansion.mixins;

import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static csweetla.treasure_expansion.TreasureExpansion.itemStrangeDevice;

@Mixin(value = net.minecraft.client.gui.GuiInventory.class, remap = false)
public abstract class GuiInventoryMixin {
	// display buttons to toggle informational overlays when player has a strange device
	@Redirect(method = "updateOverlayButtons", at=@At(value = "INVOKE", target="Lnet/minecraft/client/entity/player/EntityPlayerSP;getGamemode()Lnet/minecraft/core/player/gamemode/Gamemode;"))
	public Gamemode updateOverlayButtons(EntityPlayerSP entity_player) {
		Gamemode g = entity_player.getGamemode();

		if (g == Gamemode.survival) {
			for(int i = 0; i < entity_player.inventory.getSizeInventory(); ++i) {
				ItemStack is = entity_player.inventory.getStackInSlot(i);
				if (is != null && is.itemID == itemStrangeDevice.id) {
					return Gamemode.creative;
				}
			}
		}
		return g;
	}
}
