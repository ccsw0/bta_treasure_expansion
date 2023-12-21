package csweetla.fun_treasure.mixins;


import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.entity.player.EntityPlayer;

import csweetla.fun_treasure.FunTreasure;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static csweetla.fun_treasure.FunTreasure.armorItemDivingHelmet;

@Mixin(value = net.minecraft.core.entity.player.EntityPlayer.class, remap = false)
public abstract class EntityPlayerMixin extends net.minecraft.core.entity.EntityLiving {
	@Shadow
	public InventoryPlayer inventory;

	@Shadow
	public Gamemode gamemode;

	public EntityPlayerMixin(World world) {super(world);}

	@Unique
	public boolean piston_boots_equipped()
	{
		return this.inventory.armorInventory[0] != null && this.inventory.armorInventory[0].itemID == FunTreasure.armorItemPistonBoots.id;
	}

	@Inject(method = "jump()V", at = @At("TAIL"))
	protected void jump(CallbackInfo ci) {
		if (piston_boots_equipped()) {
			this.yd = 0.42 * 1.75;
			if (this.gamemode == Gamemode.survival) {
				this.world.playSoundAtEntity(((EntityPlayer) (Object) this), "tile.piston.out", 0.03F, world.rand.nextFloat() * 0.25F + 0.6F);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityLiving;causeFallDamage(F)V"), method = "causeFallDamage(F)V", cancellable = true)
	protected void causeFallDamage(float f, CallbackInfo ci) {
		if (piston_boots_equipped()) {
			if (f < 6.3F){
				ci.cancel();
			}
			else {
				f -= 4.0F;
			}
		}
	}

	// allow player not to lose breathe if underwater and wearing diving helmet
	@Inject(method="canBreatheUnderwater",at=@At("HEAD"), cancellable = true)
	void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
		if (this.inventory.armorInventory[3] != null && this.inventory.armorInventory[3].itemID == armorItemDivingHelmet.id) {
			cir.setReturnValue(true);
		}
	}
}
