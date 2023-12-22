package csweetla.fun_treasure.item;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;

import java.util.Random;

public class LabyrinthGeneratorItem extends Item {
	public LabyrinthGeneratorItem(String s, int i) {
		super(s,i);
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		WorldFeatureLabyrinth wfl = new WorldFeatureLabyrinth();
		wfl.generate(world, new Random(), (int) entityplayer.x, (int) entityplayer.y, (int) entityplayer.z);
		return itemstack;
	}

}
