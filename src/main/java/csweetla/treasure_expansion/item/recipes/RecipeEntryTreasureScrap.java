package csweetla.treasure_expansion.item.recipes;

import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

import java.util.*;

/**
 *  The class for recipes allowing scrapping treasures into treasure scrap
 *  Reason for this not being RecipeEntryScrap is because I want it to always give the same scrap, whereas
 *  RecipeEntryScrap determines amount of scrap from metadata
 *  Reason for not just using shapeless is because it doesn't accept damaged items
 *  There is probably a better way to do this
 */
public class RecipeEntryTreasureScrap extends RecipeEntryCraftingShapeless {

	public RecipeEntryTreasureScrap(Item scrap_item, Item scrap_mat, Integer amount) {
		super(Collections.singletonList(new RecipeSymbol(scrap_item.getDefaultStack())), new ItemStack(scrap_mat, amount));
	}

	@Override
	public boolean matches(ContainerCrafting containerCrafting) {
		int itemToScrapCount = 0;
		Item scrap_item = this.getInput().get(0).getStack().getItem();

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				ItemStack stack = containerCrafting.getItemStackAt(x, y);
				if (stack != null) {
					if (stack.itemID != scrap_item.id) {
						return false;
					}

					itemToScrapCount++;
				}
			}
		}
		return itemToScrapCount == 1;
	}
}
