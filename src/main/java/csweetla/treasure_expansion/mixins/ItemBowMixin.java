package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.TreasureExpansion;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBow;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemBow.class, remap = false)
public class ItemBowMixin {

	/**
	 * Allow shooting a fire arrow from a bow when the fire quiver is equipped
	 */
	@Inject(method = "onUseItem", at = @At("HEAD"), cancellable = true)
	public void onUseItem(ItemStack itemstack, World world, Player entityplayer, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack quiverSlot = entityplayer.inventory.armorItemInSlot(2);
		if (quiverSlot != null && quiverSlot.getItem().equals(TreasureExpansion.itemFireQuiver) && quiverSlot.getMetadata() < quiverSlot.getMaxDamage()) {
			entityplayer.inventory.armorItemInSlot(2).damageItem(1, entityplayer);
			itemstack.damageItem(1, entityplayer);
			world.playSoundAtEntity(entityplayer, entityplayer, "random.bow", 0.3f, 1.0f / (Item.itemRand.nextFloat() * 0.4f + 0.8f));
			if (!world.isClientSide) {
				ProjectileArrow arrow = new ProjectileArrow(world, entityplayer, true, 0);
				world.entityJoinedWorld(arrow);
				arrow.remainingFireTicks = 300;
				arrow.maxFireTicks = arrow.remainingFireTicks;
			}
			cir.setReturnValue(itemstack);
		}
	}
}
