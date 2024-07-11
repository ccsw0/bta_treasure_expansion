package csweetla.treasure_expansion.item;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;

public class FireQuiverItemModel extends ItemModelStandard {
    IconCoordinate fireQuiverFull = TextureRegistry.getTexture("treasure_expansion:item/fire_quiver_empty");

    public FireQuiverItemModel(Item item, String namespace) {
        super(item, namespace);
    }

    @Override
    public IconCoordinate getIcon(Entity entity, ItemStack itemStack) {
        if (itemStack.getMetadata() >= itemStack.getItem().getMaxDamage()) return fireQuiverFull;
        return super.getIcon(entity, itemStack);
    }
}
