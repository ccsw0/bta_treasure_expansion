package csweetla.treasure_expansion.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static csweetla.treasure_expansion.TreasureExpansion.itemSpiderSilk;

@Mixin(value = Mob.class, remap = false)
public abstract class MobMixin extends Entity {

	public MobMixin(World world) {
		super(world);
	}

	@Unique
	private boolean isClimbingItem(ItemStack is) {
		return is != null && is.getItem().equals(itemSpiderSilk);
	}

	@Unique private int ValidClimbingItemSlot(Player p) {
		for(int i = 0; i < ContainerInventory.playerMainInventorySize(); ++i) {
			ItemStack is = p.inventory.mainInventory[i];
			if (is == null) continue;

			if (isClimbingItem(is) && is.getMetadata() < is.getItem().getMaxDamage()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Allow players to climb when they have the spider silk in their inventory
	 */
	@Inject(method="canClimb", at = @At("HEAD"), cancellable = true)
	public void canClimb(CallbackInfoReturnable<Boolean> cir) {
		Mob t = (Mob) (Object) this;
		if (t instanceof Player) {
			// should climb if has climbing item
			Player p = (Player) (Object) t;

			if (p.horizontalCollision) {
				int is = ValidClimbingItemSlot(p);
				if (is != -1) {
					p.inventory.mainInventory[is].damageItem(1,p);
					cir.setReturnValue(true);
				}
				// not climbing, heal climbing items
			} else {
				for (int i = 0; i < ContainerInventory.playerMainInventorySize(); i++) {
					ItemStack is = p.inventory.mainInventory[i];
					if (isClimbingItem(is) && is.getMetadata() > 0 && p.tickCount % 2 == 0) {
						is.repairItem(1);
						break;
					}
				}
			}
		}
	}

//	// climb faster when spider charm is equipped
//	@Redirect(method="moveEntityWithHeading", at = @At(value="FIELD",target="Lnet/minecraft/core/entity/Mob;yd:D",opcode = Opcodes.PUTFIELD))
//	public void moveEntityWithHeading(Mob instance, double value) {
//		if (value == 0.25 && instance instanceof EntityPlayer && ValidClimbingItemSlot((EntityPlayer) instance )) {
//			instance.yd = value * 1.75;
//		} else {
//			instance.yd = value;
//		}
//	}
}
