package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.TreasureExpansion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.camera.CameraUtil;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CameraUtil.class, remap = false)
public class CameraUtilMixin {
	@Inject(method = "isUnderLiquid", at = @At("HEAD"), cancellable = true)
	private static void isUnderLiquid(ICamera camera, World world, Material material, float partialTick, CallbackInfoReturnable<Boolean> cir) {
		ItemStack head_item = Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(3);
		if (head_item != null && head_item.getItem().id == TreasureExpansion.armorItemDivingHelmet.id) {
			cir.setReturnValue(false);
		}
	}
}
