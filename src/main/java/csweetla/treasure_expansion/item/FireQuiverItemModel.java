package csweetla.treasure_expansion.item;

import csweetla.treasure_expansion.TreasureExpansion;

import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FireQuiverItemModel extends ItemModelStandard {
    IconCoordinate fireQuiverFull = TextureRegistry.getTexture("treasure_expansion:item/fire_quiver_empty");
    IconCoordinate flamingArrow = TextureRegistry.getTexture("treasure_expansion:item/flaming_arrow");
    boolean direct = false;

    public FireQuiverItemModel(Item item, String namespace) {
        super(item, namespace);
    }

    public void renderItem(Tessellator tessellator, ItemRenderer renderer, ItemStack itemstack, Entity entity, float brightness, boolean handheldTransform) {
        direct = true;
        super.renderItem(tessellator, renderer, itemstack, entity, brightness, handheldTransform);
        direct = false;
    }

    @Override
    public @NotNull IconCoordinate getIcon(Entity entity, ItemStack itemStack) {
        if (TreasureExpansion.renderingArrow) {
            if (direct) return flamingArrow;
            // Evil hack, but it works!
            String caller = new Exception().getStackTrace()[1].getClassName();
            if (caller.equals("net.minecraft.client.render.item.model.ItemModelBow")) return flamingArrow;
        }
        if (itemStack.getMetadata() >= itemStack.getItem().getMaxDamage()) return fireQuiverFull;
        return super.getIcon(entity, itemStack);
    }
}
