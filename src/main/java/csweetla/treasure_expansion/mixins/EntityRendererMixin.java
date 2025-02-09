package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.item.FireQuiverItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, remap = false)
public class EntityRendererMixin {
	@Shadow
	public EntityRenderDispatcher renderDispatcher;
	@Unique
	String quiver_full_texture_path = "/assets/minecraft/textures/armor/quiver.png";

	@Unique
	String fire_quiver_full_texture_path = "/assets/treasure_expansion/textures/armor/fire_quiver_1.png";

	/**
	 * Render the player with the fire quiver on their back when equipped
	 */
	@Inject(method = "bindTexture", at = @At("HEAD"), cancellable = true)
	protected void loadTexture(String texturePath, CallbackInfo ci) {
		if (texturePath.equals(quiver_full_texture_path)) {
			ItemStack chest_item = Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(2);
			if (chest_item != null && chest_item.getItem() instanceof FireQuiverItem) {
				EntityRenderer<?> thisAs = (EntityRenderer<?>) (Object) this;
				thisAs.renderDispatcher.textureManager.bindTexture(this.renderDispatcher.textureManager.loadTexture(fire_quiver_full_texture_path));
//				renderEngine.bindTexture(renderEngine.getTexture(fire_quiver_full_texture_path));
				ci.cancel();
			}
		}
	}
}
