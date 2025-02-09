package csweetla.treasure_expansion.mixins;

import static csweetla.treasure_expansion.TreasureExpansion.*;
import net.minecraft.core.block.motion.CarriedBlock;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityMinecart.class, remap = false)
public abstract class EntityMinecartMixin extends Entity implements Container {

	@Shadow
	public abstract byte getType();

	public EntityMinecartMixin(@Nullable World world) {
		super(world);
	}

	/**
	 * Prevent the chests added by this mod being inserted into minecarts, because then they will be converted into oak chests
	 * TODO: Would be nice to allow these to work in minecarts
	 */
	@Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;isSneaking()Z", ordinal = 0), cancellable = true)
	public void interact(Player player, CallbackInfoReturnable<Boolean> cir) {
		if (this.getType() == 0 && player.getHeldObject() != null) {
			CarriedBlock held = (CarriedBlock) player.getHeldObject();
			if (held.block().equals(blockCobbleChest) || held.block().equals(blockIceChest) || held.block().equals(blockSandstoneChest))
				cir.setReturnValue(false);
		}

	}
}
