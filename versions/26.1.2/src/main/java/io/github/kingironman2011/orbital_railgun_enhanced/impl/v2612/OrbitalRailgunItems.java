package io.github.kingironman2011.orbital_railgun_enhanced.impl.v2612;

import io.github.kingironman2011.orbital_railgun_enhanced.OrbitalRailgun;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

/** Registers the playable item without the removed Fabric item-group API. */
public final class OrbitalRailgunItems {
    public static final OrbitalRailgunItem ORBITAL_RAILGUN =
            (OrbitalRailgunItem) register(new OrbitalRailgunItem(), "orbital_railgun");

    private OrbitalRailgunItems() { }

    private static Item register(Item item, String path) {
        return Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, path),
                item);
    }

    public static void initialize() {
        // Creative-tab registration is restored with the Fabric 26.1 replacement API.
    }
}
