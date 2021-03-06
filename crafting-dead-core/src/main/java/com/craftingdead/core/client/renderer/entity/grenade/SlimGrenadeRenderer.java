/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.client.renderer.entity.grenade;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.model.SlimGrenadeModel;
import com.craftingdead.core.world.entity.grenade.GrenadeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class SlimGrenadeRenderer extends EntityRenderer<GrenadeEntity> {

  private final SlimGrenadeModel model = new SlimGrenadeModel();

  public SlimGrenadeRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(GrenadeEntity entity, float entityYaw, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight) {

    matrixStack.translate(0D, 0.2D, 0D);
    matrixStack.scale(0.4f, -0.4f, 0.4f);

    float totalTicks = entity.getTotalTicksInAir();
    if (!entity.isStoppedInGround()) {
      totalTicks += partialTicks;
    }

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(totalTicks * 30F));

    IVertexBuilder vertexBuilder =
        renderTypeBuffer.getBuffer(model.renderType(this.getTextureLocation(entity)));
    this.model.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F,
        1.0F, 1.0F, 0.15F);
  }

  @Override
  public ResourceLocation getTextureLocation(GrenadeEntity entity) {
    return new ResourceLocation(CraftingDead.ID,
        "textures/entity/grenade/" + entity.getType().getRegistryName().getPath() + ".png");
  }
}
