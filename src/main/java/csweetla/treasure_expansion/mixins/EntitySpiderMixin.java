package csweetla.treasure_expansion.mixins;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.entity.monster.MobSpider;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static csweetla.treasure_expansion.TreasureExpansion.itemSpiderSilk;

@Mixin(value = MobSpider.class, remap = false)
public abstract class EntitySpiderMixin extends MobMonster {
	public EntitySpiderMixin(World world) {
		super(world);
	}

	@Unique
	private boolean spider_silk_equipped(Player p) {
		for (int i = 0; i < ContainerInventory.playerMainInventorySize(); ++i) {
			ItemStack is = p.inventory.mainInventory[i];
			if (is != null && is.itemID == itemSpiderSilk.id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * For spiders, treat players holding a spider silk as physically further away when looking for a target
	 */
	@Redirect(method = "findPlayerToAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getClosestPlayerToEntity(Lnet/minecraft/core/entity/Entity;D)Lnet/minecraft/core/entity/player/Player;"))
	protected Player findPlayerToAttack(World world, Entity entity, double radius) {
		double closestDistance = -1.0;
		Player closest = null;
		for (int i = 0; i < world.players.size(); ++i) {
			Player p = world.players.get(i);
			double currentDistance = p.distanceToSqr(entity.x, entity.y, entity.z);
			// important part here
			if (spider_silk_equipped(p))
				currentDistance /= 0.3;

			if (!(radius < 0.0) && !(currentDistance < radius * radius) || closestDistance != -1.0 && !(currentDistance < closestDistance))
				continue;
			closestDistance = currentDistance;
			closest = p;
		}
		return closest;
	}
}
