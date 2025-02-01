package csweetla.treasure_expansion.item;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class LavaCharmItem extends Item {

	public LavaCharmItem(String name, int id, int durability) {
		super(name, MOD_ID + ":" + name, id);
		this.setMaxStackSize(1);
		setMaxDamage(durability);
	}

	private static boolean isFirstDamageableInInventory(ItemStack is, int slot, Player player) {
		// meta data == health -> would break if damaged
		if (is.getMetadata() >= is.getItem().getMaxDamage())
			return false;
		for(int pslot = 0; pslot < player.inventory.getContainerSize(); ++pslot) {
			ItemStack s = player.inventory.mainInventory[pslot];
			if (s != null && s.itemID == is.getItem().id && s.getMetadata() < is.getItem().getMaxDamage()) {
				return pslot == slot;
			}
		}
		return false;
	}

	@Override
	public void inventoryTick(ItemStack itemstack, World world, Entity entity, int slot, boolean flag) {
		if (entity instanceof Player) {
				Player player = (Player) entity;
				if ((player.isOnFire() || player.isInLava())) {
					if (isFirstDamageableInInventory(itemstack,slot, player)) {
						itemstack.damageItem(1, player);
					}
				} else if(player.tickCount % 10 == 0) {
					itemstack.repairItem(1);
				}
		}
		super.inventoryTick(itemstack, world, entity, slot, flag);
	}
}

