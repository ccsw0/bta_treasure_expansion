package csweetla.treasure_expansion.item;

import net.minecraft.core.entity.EntityLightningBolt;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;

import java.util.Random;

public class LabyrinthGeneratorItem extends Item {
	public LabyrinthGeneratorItem(String s, int i) {
		super(s,i);
	}

    @Override
	public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (entityplayer.getGamemode() == Gamemode.creative) {
			WorldFeatureLabyrinth wfl = new WorldFeatureLabyrinth();
			boolean success = wfl.generate(world, new Random(), (int) entityplayer.x, (int) entityplayer.y, (int) entityplayer.z);
			entityplayer.sendTranslatedChatMessage(success ? "Generated Labyrinth" : "Failed to generate labyrinth here!");

			return itemstack;
		}  else {
			world.addWeatherEffect(new EntityLightningBolt(world, entityplayer.x, entityplayer.y, entityplayer.z));
			return null;
		}
	}

}
