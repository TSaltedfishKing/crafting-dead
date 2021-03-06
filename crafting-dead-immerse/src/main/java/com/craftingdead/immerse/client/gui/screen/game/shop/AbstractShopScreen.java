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

package com.craftingdead.immerse.client.gui.screen.game.shop;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.craftingdead.immerse.game.module.shop.ClientShopModule;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class AbstractShopScreen extends Screen {

  private static final ITextComponent TITLE =
      new TranslationTextComponent("gui.screen.shop.title");

  private final List<GameButton> shopButtons = new ArrayList<>();

  private final Screen lastScreen;

  private final ClientShopModule shop;

  private final PlayerExtension<?> player;

  private int cachedBuyTime = -1;

  public AbstractShopScreen(Screen lastScreen, ClientShopModule shop, PlayerExtension<?> player) {
    super(TITLE);
    this.lastScreen = lastScreen;
    this.shop = shop;
    this.player = player;
  }

  protected void addShopButton(GameButton shopButton) {
    this.shopButtons.add(shopButton);
  }

  public ClientShopModule getShop() {
    return this.shop;
  }

  @Override
  public void init() {
    int mx = this.width / 2;
    int my = this.height / 2;

    int x = mx - 140;
    int y = my - 72;
    int w1 = 100;
    int h1 = 20;
    int ym = 22;

    for (int i = 0; i < this.shopButtons.size(); i++) {
      GameButton shopButton = this.shopButtons.get(i);
      shopButton.x = x;
      shopButton.y = y + (i * ym);
      shopButton.setWidth(w1);
      shopButton.setHeight(h1);
      this.addButton(shopButton);
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (super.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    } else if (keyCode == GLFW.GLFW_KEY_B) {
      this.minecraft.setScreen(this.lastScreen);
      return true;
    }
    return false;
  }

  @Override
  public void tick() {
    this.cachedBuyTime = this.shop.getBuyTimeSeconds();
    if (this.cachedBuyTime <= 0) {
      this.onClose();
    }
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);

    int mx = this.width / 2;
    int my = this.height / 2;

    // Render Top and Bottom margins
    RenderUtil.fillWithShadow(matrixStack, 0, 0, this.width, 30, 0x80000000);
    RenderUtil.fillWithShadow(matrixStack, 0, this.height - 30, this.width, 30, 0x80000000);

    drawCenteredString(matrixStack, font,
        new TranslationTextComponent("gui.screen.shop.back", "B").withStyle(TextFormatting.ITALIC),
        mx - 150, 18, 0xFFFFFFFF);

    drawCenteredString(matrixStack, this.font,
        this.getTitle().copy().withStyle(TextFormatting.BOLD),
        mx, 10, 0xFFFFFFFF);

    RenderUtil.renderTextRight(this.font, matrixStack, mx + 150, 18,
        new TranslationTextComponent("gui.screen.shop.buy_time",
            new StringTextComponent(String.valueOf(this.cachedBuyTime))
                .withStyle(TextFormatting.RED))
                    .withStyle(TextFormatting.ITALIC),
        0xFFFFFFFF, true);

    // render slots and background
    int bh = this.shopButtons.size() * 22;
    RenderUtil.fillWithShadow(matrixStack, mx - 150, my - 80, 120, bh + 15, 0x80000000);

    super.render(matrixStack, mouseX, mouseY, partialTicks);

    // render info of item over
    RenderUtil.fillWithShadow(matrixStack, mx - 25, my - 80, 115, 160, 0x80000000);
    this.font.drawShadow(matrixStack,
        new TranslationTextComponent("gui.screen.shop.selected").withStyle(TextFormatting.BOLD),
        mx - 20, my - 75, 0xFFFFFFFF);

    for (GameButton shopButton : this.shopButtons) {
      if (shopButton instanceof InfoPanel && shopButton.isHovered()) {
        ((InfoPanel) shopButton).renderInfo(mx, my, matrixStack, mouseX, mouseY, partialTicks);
      }
    }

    final int spacing = 5;
    final int moneyHeight = 15;

    boolean renderMoney = this.shop.getMoney() > -1;
    if (renderMoney) {
      RenderUtil.fillWithShadow(matrixStack, mx + 95, my - 80, 69, moneyHeight, 0x80000000);
      this.font.drawShadow(matrixStack,
          new StringTextComponent("$" + this.shop.getMoney())
              .withStyle(TextFormatting.BOLD, TextFormatting.GREEN),
          mx + 100, my - 76, 0);
    }

    int inventoryYOffset = renderMoney ? moneyHeight + spacing : 0;


    RenderUtil.fillWithShadow(matrixStack, mx + 95, my - 80 + inventoryYOffset, 69, 140,
        0x80000000);
    this.font.drawShadow(matrixStack,
        new TranslationTextComponent("gui.screen.shop.inventory").withStyle(TextFormatting.BOLD),
        mx + 100, my - 75 + inventoryYOffset, 0xFFFFFFFF);


    PlayerInventory inventory = this.player.getEntity().inventory;
    for (int i = 0; i < 7; i++) {
      ItemStack itemStack = inventory.getItem(i);
      com.craftingdead.core.client.util.RenderUtil.renderGuiItem(itemStack, mx + 120,
          my - 60 + inventoryYOffset + (i * 21), 0xFFFFFFFF,
          ItemCameraTransforms.TransformType.FIXED);
    }
  }
}
