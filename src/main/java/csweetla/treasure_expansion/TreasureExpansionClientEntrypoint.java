package csweetla.treasure_expansion;

import csweetla.treasure_expansion.item.recipes.RecipeEntryTreasureScrap;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.guidebook.crafting.RecipePageCrafting;
import net.minecraft.client.gui.guidebook.crafting.displays.DisplayAdapterShapeless;
import net.minecraft.client.sound.SoundRepository;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class TreasureExpansionClientEntrypoint implements ClientModInitializer, ClientStartEntrypoint {

	@Override
	public void onInitializeClient() {
	}

	@Override
	public void beforeClientStart() {
	}

	@Override
	public void afterClientStart() {
		RecipePageCrafting.recipeToDisplayAdapterMap.put(RecipeEntryTreasureScrap.class, new DisplayAdapterShapeless() );
		SoundRepository.registerNamespace(MOD_ID);
	}
}
