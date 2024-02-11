package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.item.FireQuiverItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, remap = false)
public class EntityRendererMixin {
	@Unique
	String quiver_texture_path = "/armor/quiver.png";

	@Unique
	String fire_quiver_texture_path = "/armor/fire_quiver_1.png";

	@Inject(method = "loadTexture", at = @At("HEAD"), cancellable = true)
	protected void loadTexture(String texturePath, CallbackInfo ci) {
		if (texturePath.equals(quiver_texture_path)) {
			ItemStack chest_item = Minecraft.getMinecraft(this).thePlayer.inventory.armorItemInSlot(2);
			if (chest_item != null && chest_item.getItem() instanceof FireQuiverItem) {
				EntityRenderer<?> thisAs = (EntityRenderer<?>) (Object) this;
				RenderEngine renderEngine = thisAs.renderDispatcher.renderEngine;
				renderEngine.bindTexture(renderEngine.getTexture(fire_quiver_texture_path));
				ci.cancel();
			}
		}
	}
}
