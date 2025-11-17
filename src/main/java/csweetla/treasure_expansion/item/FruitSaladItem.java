package csweetla.treasure_expansion.item;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class FruitSaladItem extends ItemFood {

	public FruitSaladItem(String name, String namespaceId, int id, int healAmount, int ticksPerHeal, boolean favouriteWolfMeat, int uses) {
		super(name, namespaceId, id, healAmount, ticksPerHeal, favouriteWolfMeat, 1);
		this.setMaxDamage(uses);
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		if (entityplayer.getHealth() < entityplayer.getMaxHealth() && entityplayer.getHealth() + entityplayer.getTotalHealingRemaining() < entityplayer.getMaxHealth()) {
			entityplayer.heal(this.getHealAmount());
			itemstack.damageItem(1, entityplayer);
			world.playSoundAtEntity(entityplayer, entityplayer,"random.bite", 0.5F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.1F, 1.1F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.1F);
		}

		return itemstack;
	}

}
