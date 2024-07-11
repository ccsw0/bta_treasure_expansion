package csweetla.treasure_expansion.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
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
import static csweetla.treasure_expansion.ModItemTags.fizzleInWater;

@Mixin(value = EntityItem.class, remap = false)
public abstract class ItemEntityMixin extends Entity {

	@Shadow
	public ItemStack item;

	@Shadow
	public abstract void tick();

	public ItemEntityMixin(World world) {
		super(world);
	}

	@Inject(method = "<init>(Lnet/minecraft/core/world/World;DDDLnet/minecraft/core/item/ItemStack;)V", at=@At("TAIL"))
	protected void init(World world, double d, double d1, double d2, ItemStack itemstack, CallbackInfo ci) {
		if (itemstack != null && itemstack.getItem() != null && fireImmuneAsEntity.appliesTo(itemstack.getItem())) {
			EntityItem instance = (EntityItem)(Object) this;
			instance.fireImmune = true;
		}
	}

	@Inject(method = "hurt", at=@At("HEAD"), cancellable = true)
	public void hurt(Entity entity, int i, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		if (type.equals(DamageType.FIRE) && fireImmuneAsEntity.appliesTo(item.getItem())) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "tick", at=@At("TAIL"))
	public void tick(CallbackInfo ci) {
		if (fireImmuneAsEntity.appliesTo(item.getItem()) && this.world.isMaterialInBB(this.bb.expand(+0.1, -0.25, +0.1), Material.lava)) {
			this.yd = 0.125f * random.nextFloat();
			this.xd *= 0.75f;
			this.zd *= 0.75f;
			this.remainingFireTicks = 100;
			this.maxFireTicks = 100;
		}

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
	}


	@WrapOperation(method = "tick", at=@At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockMaterial(III)Lnet/minecraft/core/block/material/Material;"))
	public Material stop_getting_block_mat(World instance, int x, int y, int z, Operation<Material> original) {
		return fireImmuneAsEntity.appliesTo(item.getItem()) ? null : original.call(instance, x, y, z);
	}
}
