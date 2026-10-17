package io.github.kingironman2011.orbital_railgun_enhanced.impl.v1204;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Хранит момент готовности рейлгана для каждого игрока в данных мира.
 * В отличие от ванильного ItemCooldownManager (он живёт внутри объекта игрока
 * и теряется при смерти/перезаходе), это значение переживает респаун и рестарт сервера.
 */
public class RailgunCooldownState extends PersistentState {
    private static final String ID = "orbital_railgun_enhanced_cooldowns";

    /** UUID игрока -> игровое время мира (тики), когда кулдаун закончится. */
    private final Map<UUID, Long> readyAt = new HashMap<>();

    private static RailgunCooldownState get(MinecraftServer server) {
        return server.getOverworld()
                .getPersistentStateManager()
                .getOrCreate(RailgunCooldownState::fromNbt, RailgunCooldownState::new, ID);
    }

    /** Сколько тиков осталось до готовности (0 — можно стрелять). */
    public static long remaining(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return 0;
        }
        long now = server.getOverworld().getTime();
        return Math.max(0, get(server).readyAt.getOrDefault(player.getUuid(), 0L) - now);
    }

    /** Запускает кулдаун на указанное число тиков. */
    public static void start(ServerPlayerEntity player, int ticks) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        long now = server.getOverworld().getTime();
        RailgunCooldownState state = get(server);
        state.readyAt.values().removeIf(t -> t <= now); // чистим истёкшие записи
        state.readyAt.put(player.getUuid(), now + ticks);
        state.markDirty();
    }

    /** Заново показывает игроку оставшийся кулдаун (анимация на предмете). */
    public static void reapply(ServerPlayerEntity player) {
        long rem = remaining(player);
        if (rem > 0) {
            player.getItemCooldownManager()
                    .set(OrbitalRailgunItems.ORBITAL_RAILGUN, (int) Math.min(rem, Integer.MAX_VALUE));
        }
    }

    /** Регистрирует восстановление кулдауна после респауна и входа на сервер. */
    public static void registerEvents() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> reapply(newPlayer));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> reapply(handler.getPlayer()));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound map = new NbtCompound();
        readyAt.forEach((id, time) -> map.putLong(id.toString(), time));
        nbt.put("ready_at", map);
        return nbt;
    }

    public static RailgunCooldownState fromNbt(NbtCompound nbt) {
        RailgunCooldownState state = new RailgunCooldownState();
        NbtCompound map = nbt.getCompound("ready_at");
        for (String key : map.getKeys()) {
            state.readyAt.put(UUID.fromString(key), map.getLong(key));
        }
        return state;
    }
}
