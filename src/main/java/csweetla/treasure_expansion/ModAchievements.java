package csweetla.treasure_expansion;

import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.collection.NamespaceID;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class ModAchievements {

	public static Achievement EAT_FRUIT_SALAD;
	public static Achievement FIND_TREASURE;
	public static Achievement KILL_VAMPIRE;
	public static Achievement FIND_DISCO;
	public static Achievement GO_DIVING;



	static {

		FIND_TREASURE = (new Achievement(NamespaceID.getPermanent(MOD_ID, "find_treasure"), MOD_ID + ".findTreasure", TreasureExpansion.itemFireQuiver,  null)).registerAchievement();
		EAT_FRUIT_SALAD = (new Achievement(NamespaceID.getPermanent(MOD_ID, "eat_fruit_salad"), MOD_ID + ".eatFruitSalad", TreasureExpansion.foodItemFruitSalad, FIND_TREASURE)).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		KILL_VAMPIRE = (new Achievement(NamespaceID.getPermanent(MOD_ID, "kill_vampire"), MOD_ID + ".killVampire", TreasureExpansion.toolItemSilverSword, FIND_TREASURE)).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		GO_DIVING = (new Achievement(NamespaceID.getPermanent(MOD_ID, "go_diving"), MOD_ID + ".goDiving", TreasureExpansion.armorItemDivingHelmet, FIND_TREASURE)).registerAchievement();
		FIND_DISCO = (new Achievement(NamespaceID.getPermanent(MOD_ID, "find_disco"), MOD_ID + ".findDisco", Blocks.JUKEBOX.asItem(),  null)).registerAchievement();

		ModAchievementPage.THE.addAchievement(FIND_TREASURE,0,0);
		ModAchievementPage.THE.addAchievement(EAT_FRUIT_SALAD,0,-2);
		ModAchievementPage.THE.addAchievement(KILL_VAMPIRE,2,-1);
		ModAchievementPage.THE.addAchievement(FIND_DISCO,0,4);
		ModAchievementPage.THE.addAchievement(GO_DIVING,-2,-1);

	}
}
