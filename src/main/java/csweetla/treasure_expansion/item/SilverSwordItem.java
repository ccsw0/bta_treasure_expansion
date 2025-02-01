package csweetla.treasure_expansion.item;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.item.tool.ItemToolSword;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class SilverSwordItem extends ItemToolSword {

	public SilverSwordItem(String name, int id, ToolMaterial enumtoolmaterial, int durability) {
		super(name, MOD_ID + ":" + name, id, enumtoolmaterial);
		ItemTags.PREVENT_CREATIVE_MINING.tag(this);
		setMaxDamage(durability);
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, Mob attacked, Mob player) {
		if (attacked instanceof MobMonster) {
			attacked.remainingFireTicks = 35 + player.world.rand.nextInt(150);
			attacked.maxFireTicks = attacked.remainingFireTicks;
		}
		itemstack.damageItem(1, player);
		return true;
	}

	@Override
	public int getDamageVsEntity(Entity attacked, ItemStack s) {
	    if (attacked instanceof MobMonster) {
            return ((ItemToolSword) Items.TOOL_SWORD_IRON).getDamageVsEntity(attacked, new ItemStack(Items.TOOL_SWORD_IRON));
        }
		return ((ItemToolSword) Items.TOOL_SWORD_STONE).getDamageVsEntity(attacked, new ItemStack(Items.TOOL_SWORD_STONE));
	}
}
