package com.craftingdead.mod.potion;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.util.ModDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BleedingEffect extends Effect {

  private static final int MAXIMUM_DELAY = 20 * 6;
  private static final int MINIMUM_DELAY = 10;
  private static final int DELAY_REDUCTION_PER_LEVEL = 20;

  protected BleedingEffect() {
    super(EffectType.HARMFUL, 0x8B0000);
  }

  @Override
  public void performEffect(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.getHealth() > 1.0F) {
      livingEntity.attackEntityFrom(ModDamageSource.BLEEDING, 1.0F);
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    int secondsBetweenHits = (MAXIMUM_DELAY - (amplifier * DELAY_REDUCTION_PER_LEVEL));
    return duration % Math.max(secondsBetweenHits, MINIMUM_DELAY) == 0;
  }

  @Override
  public List<ItemStack> getCurativeItems() {
    List<ItemStack> items = new ArrayList<ItemStack>();
    items.add(new ItemStack(ModItems.BANDAGE::get));
    items.add(new ItemStack(ModItems.FIRST_AID_KIT::get));
    return items;
  }
}
