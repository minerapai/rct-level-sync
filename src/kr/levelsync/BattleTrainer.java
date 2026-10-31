package kr.levelsync;

import com.gitlab.srcmc.rctmod.api.RCTMod;
import com.gitlab.srcmc.rctmod.api.service.TrainerManager;
import com.gitlab.srcmc.rctmod.world.entities.TrainerMob;
import net.minecraft.class_1657;

/**
 * Remembers which RCT trainer is starting a battle. RCT starts its battles synchronously
 * (TrainerMob.startBattleWith -> RCTMod.makeBattle -> BattleManager.startBattle), so the
 * battle setup can look up its opponent here.
 */
public final class BattleTrainer {
    private static final ThreadLocal<TrainerMob> CURRENT = new ThreadLocal<>();

    private BattleTrainer() {
    }

    public static void set(TrainerMob mob) {
        CURRENT.set(mob);
    }

    public static void clear() {
        CURRENT.remove();
    }

    /** Whether the battle that is currently being set up should be level scaled. */
    public static boolean shouldScale() {
        TrainerMob mob = CURRENT.get();

        if (mob == null) {
            return false; // not started by an RCT trainer
        }

        return !LevelSyncConfig.keyTrainersOnly() || isKeyTrainer(mob);
    }

    /**
     * The level cap to scale the player's pokemon to: the level cap that belongs to the opposing
     * trainer (its ace level + relativeLevelCap, as RCT computes it when that trainer is the next
     * one in the series), so rematches against already cleared trainers use their own cap instead
     * of the player's current one. Falls back to the player's current level cap.
     */
    public static int levelCap(class_1657 player) {
        TrainerManager tm = RCTMod.getInstance().getTrainerManager();
        TrainerMob mob = CURRENT.get();

        if (mob != null) {
            return tm.getData(mob).getRequiredLevelCap(player);
        }

        return tm.getData(player).getLevelCap();
    }

    /** Key trainers are the non optional trainers of a series (gym leaders, elite four, champions, rivals, ...). */
    public static boolean isKeyTrainer(TrainerMob mob) {
        return !RCTMod.getInstance().getTrainerManager().getData(mob).isOptional();
    }
}
