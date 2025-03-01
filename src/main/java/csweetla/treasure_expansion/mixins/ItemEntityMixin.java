package csweetla.treasure_expansion.mixins;

import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static csweetla.treasure_expansion.ModItemTags.fireImmuneAsEntity;

@Mixin(value = EntityItem.class, remap = false)
public abstract class ItemEntityMixin extends Entity {

	@Shadow
	public ItemStack item;

	public ItemEntityMixin(World world) {
		super(world);
	}

	@Shadow
	public abstract void tick();

	/**
	 * make item entities who's item has the fire immune tag actually fire immune
	 */
	@Inject(method = "<init>(Lnet/minecraft/core/world/World;DDDLnet/minecraft/core/item/ItemStack;)V", at = @At("TAIL"))
	protected void init(World world, double d, double d1, double d2, ItemStack itemstack, CallbackInfo ci) {
		if (itemstack != null && itemstack.getItem() != null && fireImmuneAsEntity.appliesTo(itemstack.getItem())) {
			EntityItem instance = (EntityItem) (Object) this;
			instance.fireImmune = true;
		}
	}

	/**
	 * don't "damage" item entities who are fire immune
	 */
	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	public void hurt(Entity entity, int i, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		if (type.equals(DamageType.FIRE) && fireImmuneAsEntity.appliesTo(item.getItem())) {
			cir.setReturnValue(false);
		}
	}

	/**
	 * fire immune item entities float on lava
	 */
	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		if (fireImmuneAsEntity.appliesTo(item.getItem()) && this.world.isMaterialInBB(this.bb.expand(+0.1, -0.25, +0.1), Material.lava)) {
			this.yd = 0.125f * random.nextFloat();
			this.xd *= 0.75f;
			this.zd *= 0.75f;
			this.remainingFireTicks = 100;
			this.maxFireTicks = 100;
		}

		/*

		fizzle in water is bugged and its kind of a cringe feature idk if i will fix it
		if (fizzleInWater.appliesTo(item.getItem()) && this.isInWaterOrRain() ) {
			if (this.tickCount % 10 == 0 && random.nextInt(2) == 0 && !Global.isServer)
				this.world.playSoundAtEntity(Minecraft.getMinecraft(this).thePlayer,this, "random.fizz", 0.4f, 2.0f + this.random.nextFloat() * 0.4f);
			if (random.nextInt(4) == 0) {
				world.spawnParticle("largesmoke", this.x - 0.5f + random.nextFloat(), this.y + 0.25f, this.z - 0.5f + random.nextFloat(), 0, 0, 0, 0, 0);
			}
			if (random.nextInt(2) == 0)
				world.spawnParticle("smoke",this.x - 0.5f + random.nextFloat(), this.y + 0.25f, this.z  - 0.5f + random.nextFloat(), 0, 0, 0, 0, 0);
			world.spawnParticle("bubble",this.x - 0.5f + random.nextFloat(), this.y + 0.25f, this.z - 0.5f + random.nextFloat(), 0, 0.25f + 0.45f * random.nextFloat(), 0, 0, 0);
		}

		 */
	}

	/**
	 * Prevent fire immune items from being burned again..
	 */
	@Inject(method = "burn", at = @At("HEAD"), cancellable = true)
	public void stop_getting_block_mat(int damage, CallbackInfo ci) {
		if (fireImmuneAsEntity.appliesTo(item.getItem())) {
			ci.cancel();
		}
	}
}
