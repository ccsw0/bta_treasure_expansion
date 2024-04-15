package csweetla.treasure_expansion.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.monster.EntitySpider;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static csweetla.treasure_expansion.TreasureExpansion.itemSpiderSilk;

@Mixin(value = EntitySpider.class, remap = false)
public abstract class EntitySpiderMixin extends EntityMonster {
	public EntitySpiderMixin(World world) {
		super(world);
	}

	@Unique
	private boolean spider_silk_equipped(EntityPlayer p) {
		for(int i = 0; i < p.inventory.getSizeInventory(); ++i) {
			ItemStack is = p.inventory.getStackInSlot(i);
			if (is != null && is.itemID == itemSpiderSilk.id) {
				return true;
			}
		}
		return false;
	}

	// Make spider calculate the closest player taking into account spider charm
	@Redirect(method = "findPlayerToAttack", at=@At(value = "INVOKE",target = "Lnet/minecraft/core/world/World;getClosestPlayerToEntity(Lnet/minecraft/core/entity/Entity;D)Lnet/minecraft/core/entity/player/EntityPlayer;"))
	protected EntityPlayer findPlayerToAttack(World world, Entity entity, double radius) {
		double closestDistance = -1.0;
		EntityPlayer closest = null;
		for (int i = 0; i < world.players.size(); ++i) {
			EntityPlayer p = world.players.get(i);
			double currentDistance = p.distanceToSqr(entity.x, entity.y, entity.z);
			// important part here
			if (spider_silk_equipped(p))
				currentDistance /= 0.3;

			if (!(radius < 0.0) && !(currentDistance < radius * radius) || closestDistance != -1.0 && !(currentDistance < closestDistance)) continue;
			closestDistance = currentDistance;
			closest = p;
		}
		return closest;
	}}
