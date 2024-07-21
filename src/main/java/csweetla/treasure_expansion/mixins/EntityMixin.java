package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.TreasureExpansion;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class, remap = false)
public class EntityMixin {

	@Unique
	public boolean piston_boots_equipped(EntityPlayer player)
	{
		return player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].itemID == TreasureExpansion.armorItemPistonBoots.id;
	}

	@Inject(method = "move", at=@At("HEAD"))
	public void move_head(double xd, double yd, double zd, CallbackInfo ci) {
		Entity thisAs = (Entity) (Object) this;
		if (thisAs instanceof EntityPlayer) {
			thisAs.footSize += piston_boots_equipped((EntityPlayer) thisAs) ? 0.5F : 0.0F;
		}
	}

	@Inject(method = "move", at=@At("TAIL"))
	public void move_tail(double xd, double yd, double zd, CallbackInfo ci) {
		Entity thisAs = (Entity) (Object) this;
		if (thisAs instanceof EntityPlayer) {
			thisAs.footSize = 0.5F;
		}
	}

	@ModifyVariable(method = "moveRelative", at = @At("HEAD"), ordinal = 2, argsOnly = true)
	public float move_relative(float f2) {
		Entity thisAs = (Entity) (Object) this;
		if (thisAs instanceof EntityPlayer && thisAs.wasInWater) {
			ItemStack boots_slot = ((EntityPlayer) thisAs).inventory.armorInventory[0];
			if (boots_slot != null && boots_slot.getItem().equals(TreasureExpansion.itemFlippers))
				return f2 * 3F;
		}
		return f2;
	}
}
