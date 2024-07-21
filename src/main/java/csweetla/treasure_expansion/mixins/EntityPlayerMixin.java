package csweetla.treasure_expansion.mixins;

import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.Item;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import net.minecraft.core.entity.player.EntityPlayer;

import csweetla.treasure_expansion.TreasureExpansion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static csweetla.treasure_expansion.TreasureExpansion.*;

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
		return this.inventory.armorInventory[0] != null && this.inventory.armorInventory[0].itemID == TreasureExpansion.armorItemPistonBoots.id;
	}

	@Unique
	boolean diving_helmet_equipped()
	{
		return this.inventory.armorInventory[3] != null && this.inventory.armorInventory[3].itemID == armorItemDivingHelmet.id;
	}

	@Inject(method = "jump()V", at = @At("TAIL"))
	protected void jump(CallbackInfo ci) {
		if (piston_boots_equipped()) {
			this.yd = 0.42 * 1.75;
			if (this.gamemode == Gamemode.survival) {
				EntityPlayer thisAs = ((EntityPlayer) (Object) this);
				this.world.playSoundAtEntity(thisAs, thisAs, "tile.piston.out", 0.03F, world.rand.nextFloat() * 0.25F + 0.6F);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityLiving;causeFallDamage(F)V"), method = "causeFallDamage(F)V", cancellable = true)
	protected void causeFallDamage(float f, CallbackInfo ci) {
		if (f < 6.3F && piston_boots_equipped()) {
				ci.cancel();
		}
	}

	// allow player not to lose breathe if underwater and wearing diving helmet
	@Inject(method="canBreatheUnderwater",at=@At("HEAD"), cancellable = true)
	void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
		if (diving_helmet_equipped()) {
			cir.setReturnValue(true);
		}
	}

	// allow player to break blocks fast underwater when wearing a diving helmet
	@Redirect(method ="getCurrentPlayerStrVsBlock",at=@At(value = "INVOKE",target = "Lnet/minecraft/core/entity/player/EntityPlayer;isUnderLiquid(Lnet/minecraft/core/block/material/Material;)Z"))
	boolean getCurrentPlayerStrVsBlock(EntityPlayer instance, Material material) {
		boolean ret = isUnderLiquid(material);
		if (ret && diving_helmet_equipped())
			return false;
		return ret;
	}

	// if the player has a damageable lava charm, then they won't get hurt by fire damage.
	@Inject(method="hurt",at=@At("HEAD"), cancellable = true)
	public void hurt1(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		if(type == DamageType.FIRE) {
			for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
				ItemStack is = this.inventory.getStackInSlot(i);
				if (is != null && is.itemID == itemLavaCharm.id && is.getMetadata() < is.getMaxDamage()) {
						cir.setReturnValue(false);
						break;
				}
			}
		}
	}

    @Inject(method="getNextArrow", at=@At("HEAD"), cancellable = true)
	public void getNextArrow(CallbackInfoReturnable<Item> cir) {
	    ItemStack quiverSlot = this.inventory.armorItemInSlot(2);
	    if (quiverSlot != null && quiverSlot.itemID == itemFireQuiver.id) {
	        // Evil hack, but getNextArrow is only used directly before rendering
	        TreasureExpansion.renderingArrow = true;
            cir.setReturnValue(itemFireQuiver);
	    }
	}
}
