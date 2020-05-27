package com.craftingdead.mod.item;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.craftingdead.mod.inventory.CraftingInventorySlotType;
import com.craftingdead.mod.util.Text;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class AttachmentItem extends Item {

  private final Map<MultiplierType, Float> multipliers;
  private final CraftingInventorySlotType inventorySlot;
  private final boolean hasLaser;
  private final boolean suppressesSounds;

  public AttachmentItem(Properties properties) {
    super(properties);
    this.multipliers = properties.multipliers;
    this.inventorySlot = properties.inventorySlot;
    this.hasLaser = properties.hasLaser;
    this.suppressesSounds = properties.suppressesSounds;
  }

  public Float getMultiplier(MultiplierType multiplierType) {
    return this.multipliers.getOrDefault(multiplierType, 1.0F);
  }

  public CraftingInventorySlotType getInventorySlot() {
    return this.inventorySlot;
  }

  public boolean hasLaser() {
    return this.hasLaser;
  }

  public boolean isSoundSuppressor() {
    return this.suppressesSounds;
  }

  @Override
  public void addInformation(ItemStack stack, World world,
      List<ITextComponent> lines, ITooltipFlag tooltipFlag) {
    super.addInformation(stack, world, lines, tooltipFlag);

    for (Entry<MultiplierType, Float> entry : this.multipliers.entrySet()) {
      lines.add(Text.translate(entry.getKey().getTranslationKey())
          .applyTextStyle(TextFormatting.GRAY)
          .appendSibling(Text.of(" " + entry.getValue() + "x").applyTextStyle(TextFormatting.RED)));
    }
  }

  public static enum MultiplierType {
    DAMAGE, ACCURACY, FOV;

    private final String translationKey = "attachment_multiplier_type." + this.name().toLowerCase();

    public String getTranslationKey() {
      return this.translationKey;
    }
  }

  public static class Properties extends Item.Properties {

    private final Map<MultiplierType, Float> multipliers = new EnumMap<>(MultiplierType.class);
    private CraftingInventorySlotType inventorySlot;
    private boolean hasLaser;
    private boolean suppressesSounds;

    public Properties addMultiplier(MultiplierType modifierType, float multiplier) {
      this.multipliers.put(modifierType, multiplier);
      return this;
    }

    public Properties setHasLaser(boolean hasLaser) {
      this.hasLaser = hasLaser;
      return this;
    }

    public Properties setSuppressesSounds(boolean suppressesSounds) {
      this.suppressesSounds = suppressesSounds;
      return this;
    }

    public Properties setInventorySlot(CraftingInventorySlotType inventorySlot) {
      this.inventorySlot = inventorySlot;
      return this;
    }
  }
}
