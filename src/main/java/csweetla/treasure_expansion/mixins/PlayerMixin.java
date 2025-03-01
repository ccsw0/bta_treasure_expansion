package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.TreasureExpansion;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static csweetla.treasure_expansion.TreasureExpansion.*;

@Mixin(value = net.minecraft.core.entity.player.Player.class, remap = false)
public abstract class PlayerMixin extends net.minecraft.core.entity.Mob {
	@Shadow
	public ContainerInventory inventory;

	@Shadow
	public Gamemode gamemode;

	public PlayerMixin(World world) {
		super(world);
	}

	@Unique
	public boolean piston_boots_equipped() {
		return this.inventory.armorInventory[0] != null && this.inventory.armorInventory[0].itemID == TreasureExpansion.armorItemPistonBoots.id;
	}

	@Unique
	boolean diving_helmet_equipped() {
		return this.inventory.armorInventory[3] != null && this.inventory.armorInventory[3].itemID == armorItemDivingHelmet.id;
	}

	/**
	 * Play piston sound when a player wearing piston boots jumps
	 */
	@Inject(method = "jump()V", at = @At("TAIL"))
	protected void jump(CallbackInfo ci) {
		if (piston_boots_equipped()) {
			this.yd = 0.42 * 1.75;
			if (this.gamemode == Gamemode.survival && this.world != null) {
				Player thisAs = ((Player) (Object) this);
				this.world.playSoundAtEntity(thisAs, thisAs, "tile.piston.out", 0.1F, world.rand.nextFloat() * 0.25F + 0.6F);
			}
		}
	}

	/**
	 * Stop fall damage below a certain height when wearing piston boots
	 */
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/Mob;causeFallDamage(F)V"), method = "causeFallDamage(F)V", cancellable = true)
	protected void causeFallDamage(float f, CallbackInfo ci) {
		if (f < 6.3F && piston_boots_equipped()) {
			ci.cancel();
		}
	}

	/**
	 * Player who is wearing diving helmet can breathe underwater
	 */
	@Inject(method = "canBreatheUnderwater", at = @At("HEAD"), cancellable = true)
	void canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
		if (diving_helmet_equipped()) {
			cir.setReturnValue(true);
		}
	}

	/**
	 * Player who is wearing a diving helmet has no speed penalty for breaking blocks underwater
	 */
	@Redirect(method = "getCurrentPlayerStrVsBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;isUnderLiquid(Lnet/minecraft/core/block/material/Material;)Z"))
	boolean getCurrentPlayerStrVsBlock(Player instance, Material material) {
		boolean ret = isUnderLiquid(material);
		if (ret && diving_helmet_equipped())
			return false;
		return ret;
	}

	/**
	 * Player who has a damageable fire charm in their inventory won't take any fire damage
	 */
	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	public void hurt1(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		if (type == DamageType.FIRE) {
			for (int i = 0; i < ContainerInventory.playerMainInventorySize(); ++i) {
				ItemStack is = this.inventory.mainInventory[i];
				if (is != null && is.itemID == itemLavaCharm.id && is.getMetadata() < is.getMaxDamage()) {
					cir.setReturnValue(false);
					break;
				}
			}
		}
	}

	/**
	 * Hacky mixin which lets us know if we are going to be rendering the arrow for the bow ->
	 * so we can draw the fire arrow later
	 */
	@Inject(method = "getNextArrow", at = @At("HEAD"), cancellable = true)
	public void getNextArrow(CallbackInfoReturnable<Item> cir) {
		ItemStack quiverSlot = this.inventory.armorItemInSlot(2);
		if (quiverSlot != null && quiverSlot.itemID == itemFireQuiver.id) {
			// Evil hack, but getNextArrow is only used directly before rendering
			TreasureExpansion.renderingArrow = true;
			cir.setReturnValue(itemFireQuiver);
		}
	}
}
