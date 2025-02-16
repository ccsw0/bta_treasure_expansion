package csweetla.treasure_expansion.entity;

import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.entity.MobRendererBiped;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.Global;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;

public class EntityVampireRenderer extends MobRendererBiped<EntityVampire> {
	public EntityVampireRenderer(ModelBiped model, float shadowSize) {
		super(model, shadowSize);
	}

	public void render(Tessellator tessellator, EntityVampire vampire, double x, double y, double z, float yaw, float partialTick) {

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, vampire.getAlpha());

		GL11.glDisable(2884);

		this.mainModel.onGround = this.getSwingProgress(vampire, partialTick);
		if (this.armorModel != null) {
			this.armorModel.onGround = this.mainModel.onGround;
		}

		if (this.overlayModel != null) {
			this.overlayModel.onGround = this.mainModel.onGround;
		}

		this.mainModel.isRiding = vampire.isPassenger();
		if (this.armorModel != null) {
			this.armorModel.isRiding = this.mainModel.isRiding;
		}

		if (this.overlayModel != null) {
			this.overlayModel.isRiding = this.mainModel.isRiding;
		}

		try {
			float bodyYaw = vampire.yBodyRotO + (vampire.yBodyRot - vampire.yBodyRotO) * partialTick;
			float headYaw = vampire.yRotO + (vampire.yRot - vampire.yRotO) * partialTick;
			float headPitch = vampire.xRotO + (vampire.xRot - vampire.xRotO) * partialTick;
			this.translateModel(vampire, x, y, z);
			float limbSway = this.limbSway(vampire, partialTick);
			this.setupRotations(vampire, limbSway, bodyYaw, partialTick);
			float scale = 0.0625F;
			GL11.glEnable(32826);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.setupScale(vampire, partialTick);
			GL11.glTranslatef(0.0F, -24.0F * scale - 0.0078125F, 0.0F);
			float walkSpeed = vampire.walkAnimSpeedO + (vampire.walkAnimSpeed - vampire.walkAnimSpeedO) * partialTick;
			float walkProgress = vampire.walkAnimPos - vampire.walkAnimSpeed * (1.0F - partialTick);
			if (walkSpeed > 1.0F) {
				walkSpeed = 1.0F;
			}

			this.loadEntityTexture(vampire);
			GL11.glEnable(3008);
			this.mainModel.setLivingAnimations(vampire, walkProgress, walkSpeed, partialTick);
			this.mainModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
			if (this.overlayModel != null) {
				this.overlayModel.setLivingAnimations(vampire, walkProgress, walkSpeed, partialTick);
				this.bindTexture(this.overlayTexture);
				this.overlayModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
			}

			if (this.armorModel != null) {
				this.armorModel.setLivingAnimations(vampire, walkProgress, walkSpeed, partialTick);
			}

			for (int renderPass = 0; renderPass < 4; renderPass++) {
				if (this.prepareArmor(vampire, renderPass, partialTick)) {
					this.armorModel.setLivingAnimations(vampire, walkProgress, walkSpeed, partialTick);
					this.armorModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
					GL11.glDisable(3042);
					GL11.glEnable(3008);
				}
			}

			this.renderAdditional(vampire, partialTick);
			float brightness = vampire.getBrightness(partialTick);
			if (Global.accessor.isFullbrightEnabled() || LightmapHelper.isLightmapEnabled()) {
				brightness = 1.0F;
			}

			int argb = this.getOverlayColor(vampire, brightness, partialTick);
			if ((argb >> 24 & 0xFF) > 0 || vampire.hurtTime > 0 || vampire.deathTime > 0) {
				GL11.glDisable(3553);
				GL11.glDisable(3008);
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				GL11.glDepthFunc(514);
				if (vampire.hurtTime > 0 || vampire.deathTime > 0) {
					GL11.glColor4f(brightness, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
					if (this.overlayModel != null) {
						this.overlayModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
					}

					for (int k = 0; k < 4; k++) {
						if (this.prepareArmor(vampire, k, partialTick)) {
							GL11.glColor4f(brightness, 0.0F, 0.0F, 0.4F);
							this.armorModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
						}
					}
				}

				if ((argb >> 24 & 0xFF) > 0) {
					float r = (float)(argb >> 16 & 0xFF) / 255.0F;
					float g = (float)(argb >> 8 & 0xFF) / 255.0F;
					float b = (float)(argb & 0xFF) / 255.0F;
					float a = (float)(argb >> 24 & 0xFF) / 255.0F;
					GL11.glColor4f(r, g, b, a);
					this.mainModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
					if (this.overlayModel != null) {
						this.overlayModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
					}

					for (int l = 0; l < 4; l++) {
						if (this.prepareArmor(vampire, l, partialTick)) {
							GL11.glColor4f(r, g, b, a);
							this.armorModel.render(walkProgress, walkSpeed, limbSway, headYaw - bodyYaw, headPitch, scale);
						}
					}
				}

				GL11.glDepthFunc(515);
				GL11.glDisable(3042);
				GL11.glEnable(3008);
				GL11.glEnable(3553);
			}

			GL11.glDisable(32826);
		} catch (Exception var25) {
//			LOGGER.error("Render exception in class '{}'!", this.getClass().getSimpleName(), var25);
		}

		GL11.glEnable(2884);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
		this.renderSpecials(tessellator, vampire, x, y, z);
	}
}
