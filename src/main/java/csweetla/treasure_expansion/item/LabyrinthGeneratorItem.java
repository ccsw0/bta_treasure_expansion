package csweetla.treasure_expansion.item;

import net.minecraft.core.entity.EntityLightning;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;

import java.util.Random;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class LabyrinthGeneratorItem extends Item {
	public LabyrinthGeneratorItem(String s, int i) {
		super(s, MOD_ID + ":" + s, i);
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		if (entityplayer.getGamemode() == Gamemode.creative) {
			WorldFeatureLabyrinth wfl = new WorldFeatureLabyrinth();
			boolean success = wfl.place(world, new Random(), (int) entityplayer.x, (int) entityplayer.y, (int) entityplayer.z);
			entityplayer.sendTranslatedChatMessage(success ? "Generated Labyrinth" : "Failed to generate labyrinth here!");

			return itemstack;
		} else {
			world.addWeatherEffect(new EntityLightning(world, entityplayer.x, entityplayer.y, entityplayer.z));
			return null;
		}
	}

}
