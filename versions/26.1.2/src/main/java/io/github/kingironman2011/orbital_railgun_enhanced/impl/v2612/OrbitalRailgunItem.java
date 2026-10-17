package io.github.kingironman2011.orbital_railgun_enhanced.impl.v2612;

import io.github.kingironman2011.orbital_railgun_enhanced.config.ServerConfig;
import io.github.kingironman2011.orbital_railgun_enhanced.item.IOrbitalRailgunItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

/** Vanilla-rendered 26.1.x railgun item while the GeckoLib renderer is ported. */
public final class OrbitalRailgunItem extends Item implements IOrbitalRailgunItem {
    public OrbitalRailgunItem() {
        super(new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.EPIC));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 24000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public void shoot(Player player) {
        player.getCooldowns().addCooldown(player.getMainHandItem(), ServerConfig.INSTANCE.getCooldownTicks());
    }
}
