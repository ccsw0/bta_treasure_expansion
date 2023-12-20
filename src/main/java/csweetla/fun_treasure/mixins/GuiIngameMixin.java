package csweetla.fun_treasure.mixins;


import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.item.ItemStack;

import net.minecraft.core.player.gamemode.Gamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static csweetla.fun_treasure.FunTreasure.ItemStrangeDevice;

@Mixin(value = GuiIngame.class, remap = false)
public abstract class GuiIngameMixin extends Gui {

	// lie about gamemode when we player has a strange device -- to allow information overlays
	@Redirect(method = "renderGameOverlay", at=@At(value = "INVOKE", target="Lnet/minecraft/client/entity/player/EntityPlayerSP;getGamemode()Lnet/minecraft/core/player/gamemode/Gamemode;", ordinal = 2 ))
	public Gamemode updateOverlayButtons(EntityPlayerSP entity_player) {
		Gamemode g = entity_player.getGamemode();

		if (g == Gamemode.survival) {
			for(int i = 0; i < entity_player.inventory.getSizeInventory(); ++i) {
				ItemStack is = entity_player.inventory.getStackInSlot(i);
				if (is != null && is.itemID == ItemStrangeDevice.id) {
					return Gamemode.creative;
				}
			}
		}
		return g;
	}

}
