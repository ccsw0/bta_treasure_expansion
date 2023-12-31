package csweetla.treasure_expansion.item;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.item.tool.ItemToolSword;

public class SilverSwordItem extends ItemToolSword {

	public SilverSwordItem(String name, int id, ToolMaterial enumtoolmaterial, int durability) {
		super(name, id, enumtoolmaterial);
		ItemTags.preventCreativeMining.tag(this);
		setMaxDamage(durability);
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLiving attacked, EntityLiving player) {
		if (attacked instanceof EntityMonster) {
			attacked.remainingFireTicks = 35 + player.world.rand.nextInt(150);
			attacked.maxFireTicks = attacked.remainingFireTicks;
		}
		itemstack.damageItem(1, player);
		return true;
	}

	@Override
	public int getDamageVsEntity(Entity attacked) {
		if (attacked instanceof EntityMonster) {
			return Item.toolSwordDiamond.getDamageVsEntity(attacked);
		}
		return Item.toolSwordStone.getDamageVsEntity(attacked);
	}
}
