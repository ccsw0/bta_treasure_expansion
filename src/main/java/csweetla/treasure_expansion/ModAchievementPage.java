package csweetla.treasure_expansion;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.achievements.ScreenAchievements;
import net.minecraft.client.gui.achievements.data.AchievementPage;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class ModAchievementPage extends AchievementPage {

	public static ModAchievementPage THE;

	@Override
	public @NotNull String getName() {
		return "Treasure Expansion";
	}

	@Override
	public @NotNull String getDescription() {
		return "Mod Achievements";
	}

	@Override
	public @NotNull AchievementEntry onOpenAchievement() {
		return Objects.requireNonNull(this.getEntry(ModAchievements.EAT_FRUIT_SALAD));
	}

	@Override
	public @Nullable IconCoordinate getBackgroundTile(ScreenAchievements screenAchievements, int i, Random random, int j, int k) {
		return getTextureFromBlock(Blocks.COBBLE_STONE_MOSSY);
	}

	@Override
	public void postProcessBackground(ScreenAchievements screenAchievements, Random random, ScreenAchievements.BGLayer bGLayer, int i, int j) {

	}

	@Override
	public @NotNull ItemStack getIcon() {
		return Blocks.CHEST_PLANKS_OAK.getDefaultStack();
	}

	@Override
	public int backgroundLayers() {
		return 1;
	}

	@Override
	public int backgroundColor() {
		return 0;
	}

	@Override
	public IconCoordinate getAchievementIcon(Achievement achievement) {
		return TextureRegistry.getTexture(achievement.getType().texture);
	}

	@Override
	public int lineColorLocked(boolean isHovered) {
		return 0;
	}

	@Override
	public int lineColorUnlocked(boolean isHovered) {
		return 7368816;
	}

	@Override
	public int lineColorCanUnlock(boolean isHovered) {
		return 65280;
	}

	static {
		THE = new ModAchievementPage();
	}
}
