package csweetla.treasure_expansion;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.HalpLibe;

import java.io.*;
import java.util.Map;
import java.util.Random;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;
import static csweetla.treasure_expansion.TreasureExpansion.config;

public class LootTables {
	private static final String config_location = FabricLoader.getInstance().getConfigDir().toString() + "/" + MOD_ID + ".loot_tables.json";
	private static LootTables instance = null;

	public enum LootTableType {
		DEFAULT,
		SNOW,
		DESERT,
		MINOR
	}

	@SerializedName("default")
	private Map<String, Map<String,Integer>> default_loot_map;
	private int default_cumsum = 0;

	@SerializedName("snow")
	private Map<String, Map<String,Integer>> snow_loot_map;
	private int snow_cumsum = 0;


	@SerializedName("desert")
	private Map<String, Map<String,Integer>> desert_loot_map;
	private int desert_cumsum = 0;

	@SerializedName("minor")
	private Map<String, Map<String,Integer>> minor_loot_map;
	private int minor_cumsum;

	public static void initialize() {
		if (instance == null) {
			instance = load_loot_tables();
			instance.calculate_cumsums();
		}
	}

	public static LootTables getInstance() {
		if (instance == null) {
			throw new RuntimeException("Loot Tables were never initialized!");
		}
		return instance;
	}

	public ItemStack randomLoot(LootTableType type, Random rand) {
		Map<String, Map<String,Integer>> table;
		int cumsum;

		switch (type){
			case DEFAULT:
				table = default_loot_map;
				cumsum = default_cumsum;
				break;
			case SNOW:
				table = snow_loot_map;
				cumsum = snow_cumsum;
				break;
			case DESERT:
				table = desert_loot_map;
				cumsum = desert_cumsum;
				break;
			case MINOR:
				table = minor_loot_map;
				cumsum = minor_cumsum;
				break;
			default :
				throw new RuntimeException("Should be unreachable 1");
		}

		int rndi = rand.nextInt(cumsum);

		for (Map.Entry<String, Map<String,Integer>> entry: table.entrySet()) {
			Map<String,Integer> v = entry.getValue();
			int w = v.get("weight");
			// we reached the chosen item
			if (rndi < w) {
				Item chosen = Item.itemsList[Item.nameToIdMap.get(entry.getKey())];
				int amount = v.get("amount") + rand.nextInt(v.get("amount-rand") + 1);
				return new ItemStack(chosen,amount);
			}
			rndi -= w;
		}
		throw new RuntimeException("Should be unreachable 2");
	}

	private void calculate_cumsums() {
		for (Map<String, Integer> v : default_loot_map.values()) {
			default_cumsum += v.get("weight");
		}
		for (Map<String, Integer> v : snow_loot_map.values()) {
			snow_cumsum += v.get("weight");
		}
		for (Map<String, Integer> v : desert_loot_map.values()) {
			desert_cumsum += v.get("weight");
		}
		for (Map<String, Integer> v : minor_loot_map.values()) {
			minor_cumsum += v.get("weight");
		}
	}

	private static LootTables load_loot_tables() {
		Gson GSON = (new GsonBuilder()).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
		// use custom config from config directory
		if (config.getBoolean("loot.use_custom_tables")) {
			File custom_config_file = new File(config_location);

			// if config doesn't exist, write default config (stored in jar) to config directory
			// The purpose of this is to give the user a template to start from.
			if (!custom_config_file.exists()) {
				write_custom_loot_table_template();
				TreasureExpansion.LOGGER.info("use_custom_loot_tables was set to true, but no custom json was found. Wrote the default to the config directory.");
				TreasureExpansion.LOGGER.info("If you wish to use custom loot tables, edit the file at " + config_location);
				custom_config_file = new File(config_location);
				assert custom_config_file.exists();
			}
			try {
				return GSON.fromJson(new JsonReader(new FileReader(custom_config_file)), LootTables.class);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}else {
			TreasureExpansion.LOGGER.info("use_custom_loot_tables was set to false, so using defaults.");
			// use the default, located in the mod jar
			return GSON.fromJson(new InputStreamReader(resourceAsStream("/assets/" + MOD_ID + "/default.loot_tables.json")), LootTables.class);
		}
	}

	private static void write_custom_loot_table_template() {
		String default_filepath = "/assets/" + MOD_ID + "/default.loot_tables.json";
		InputStream fileIn;
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(config_location);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		fileIn = resourceAsStream(default_filepath);

		try {
			while (fileIn.available() != 0)
				fileOut.write(fileIn.read());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	// credit to Useless for this
	private static InputStream resourceAsStream(String path) {
		InputStream in;
		in = LootTables.class.getResourceAsStream(path);
		if (in != null)
			return in;
		in = FabricLoader.getInstance().getClass().getResourceAsStream(path);
		if (in != null)
			return in;
		in = TreasureExpansion.class.getResourceAsStream(path);
		if (in != null)
			return in;
		in = DataLoader.class.getResourceAsStream(path);
		if (in != null)
			return in;
		in = HalpLibe.class.getResourceAsStream(path);
		if (in != null)
			return in;
		in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if (in != null)
			return in;
		in = Minecraft.getMinecraft(Minecraft.class).getClass().getResourceAsStream(path);
		if (in != null)
			return in;
		throw new RuntimeException("File at '"+ path + "' is seriously hard to find!");
	}

}

