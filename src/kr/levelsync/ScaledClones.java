package kr.levelsync;

import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Battle clones created for level scaled RCT battles, mapped to the real party pokemon.
 * Cobblemon gives battle rewards (experience, EVs, evolution progress) to the clone or drops
 * them for clones, so these are forwarded to the original. Pokemon does not override
 * equals/hashCode, so the weak map is identity based and entries vanish with the clone.
 */
public final class ScaledClones {
    private static final Map<Pokemon, Pokemon> ORIGINALS = Collections.synchronizedMap(new WeakHashMap<>());

    private ScaledClones() {
    }

    public static void register(Pokemon clone, Pokemon original) {
        ORIGINALS.put(clone, original);
    }

    /** The real pokemon of a scaled battle clone, or null if rewards should not be forwarded. */
    public static Pokemon originalOf(Pokemon pokemon) {
        if (pokemon == null || !LevelSyncConfig.transferRewards()) {
            return null;
        }

        return ORIGINALS.get(pokemon);
    }
}
