package csweetla.treasure_expansion.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static csweetla.treasure_expansion.TreasureExpansion.itemSpiderSilk;

@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin extends Entity {

	public EntityLivingMixin(World world) {
		super(world);
	}

	@Unique
	private boolean isClimbingItem(ItemStack is) {
		return is != null && is.getItem().equals(itemSpiderSilk);
	}

	@Unique private int ValidClimbingItemSlot(EntityPlayer p) {
		for(int i = 0; i < p.inventory.getSizeInventory(); ++i) {
			ItemStack is = p.inventory.getStackInSlot(i);
			if (is == null) continue;

			if (isClimbingItem(is) && is.getMetadata() < is.getItem().getMaxDamage()) {
				return i;
			}
		}
		return -1;
	}

	// allow climbing when spider charm is equipped
	@Inject(method="canClimb", at = @At("HEAD"), cancellable = true)
	public void canClimb(CallbackInfoReturnable<Boolean> cir) {
		EntityLiving t = (EntityLiving) (Object) this;
		if (t instanceof EntityPlayer) {
			// should climb if has climbing item
			EntityPlayer p = (EntityPlayer) (Object) t;

			if (p.horizontalCollision) {
				int is = ValidClimbingItemSlot(p);
				if (is != -1) {
					p.inventory.getStackInSlot(is).damageItem(1,p);
					cir.setReturnValue(true);
				}
				// not climbing, heal climbing items
			} else {
				for (int i = 0; i < p.inventory.getSizeInventory(); i++) {
					ItemStack is = p.inventory.getStackInSlot(i);
					if (isClimbingItem(is) && is.getMetadata() > 0 && p.tickCount % 2 == 0) {
						is.repairItem(1);
						break;
					}
				}
			}
		}
	}

//	// climb faster when spider charm is equipped
//	@Redirect(method="moveEntityWithHeading", at = @At(value="FIELD",target="Lnet/minecraft/core/entity/EntityLiving;yd:D",opcode = Opcodes.PUTFIELD))
//	public void moveEntityWithHeading(EntityLiving instance, double value) {
//		if (value == 0.25 && instance instanceof EntityPlayer && ValidClimbingItemSlot((EntityPlayer) instance )) {
//			instance.yd = value * 1.75;
//		} else {
//			instance.yd = value;
//		}
//	}
}
