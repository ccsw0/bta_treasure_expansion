package csweetla.fun_treasure.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;

import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.DamageType;

import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static csweetla.fun_treasure.FunTreasure.toolItemSilverSword;

@Mixin(value = net.minecraft.core.entity.EntityLiving.class, remap = false)
public abstract class EntityLivingMixin extends Entity {

	@Unique
	Random rand = new Random();

	public EntityLivingMixin(World world) {
		super(world);
	}

	// make the silver sword light monsters on fire and do some bonus damage
	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/EntityLiving;damageEntity(ILnet/minecraft/core/util/helper/DamageType;)V"))
	public void hurt(Entity attacker, int damage, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		EntityLiving t = (EntityLiving) (Object) this;
		if (type == DamageType.COMBAT && attacker instanceof EntityPlayer && t instanceof EntityMonster)
		{
			EntityPlayer player = (EntityPlayer) attacker;
			if (player.getHeldItem().itemID == toolItemSilverSword.id ) {
				this.hurt(attacker, 8, DamageType.FIRE);
				this.remainingFireTicks = 50 + rand.nextInt(150);
				this.maxFireTicks = this.remainingFireTicks;
			}
		}
	}
}
