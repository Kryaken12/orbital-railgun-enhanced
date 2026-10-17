package io.github.kingironman2011.orbital_railgun_enhanced.client.impl.v2612;

import io.github.kingironman2011.orbital_railgun_enhanced.client.compat.ClientVersionAdapter;
import io.github.kingironman2011.orbital_railgun_enhanced.impl.v2612.SoundsRegistry;
import io.github.kingironman2011.orbital_railgun_enhanced.network.ShootPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

/**
 * 26.1.x client bridge while the old Satin fullscreen effects are unavailable.
 * Targeting uses Minecraft's own crosshair result; firing and networking stay
 * fully functional without changing a player's Iris configuration.
 */
public final class ClientVersionAdapterImpl implements ClientVersionAdapter {
    @Override
    public void initialize() {
        // Shader callbacks are intentionally disabled for this port target.
    }

    @Override
    public boolean isAimActive() {
        return Minecraft.getInstance().hitResult != null;
    }

    @Override
    public HitResult getGuiHitResult() {
        return Minecraft.getInstance().hitResult;
    }

    @Override
    public void onShootFired(BlockPos blockPos, LocalPlayer player) {
        // No animation state is needed while fullscreen effects are disabled.
    }

    @Override
    public void sendShootPacket(ItemStack stack, BlockPos blockPos, LocalPlayer player) {
        player.playSound(SoundsRegistry.RAILGUN_SHOOT, 1.0f, 1.0f);
        ClientPlayNetworking.send(new ShootPayload(stack, blockPos));
    }
}
