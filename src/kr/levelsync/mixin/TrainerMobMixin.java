package kr.levelsync.mixin;

import com.gitlab.srcmc.rctmod.api.RCTMod;
import com.gitlab.srcmc.rctmod.api.service.TrainerManager;
import com.gitlab.srcmc.rctmod.world.entities.TrainerMob;
import kr.levelsync.BattleTrainer;
import kr.levelsync.LevelSyncConfig;
import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * RCT refuses a battle when the strongest party pokemon is above the player's level cap.
 * Since those pokemon get scaled down to the cap for the battle (see BattleManagerMixin),
 * report the player level as at most the cap for these checks.
 */
@Mixin(value = TrainerMob.class, remap = false)
public abstract class TrainerMobMixin {
    private static final String GET_PLAYER_LEVEL =
        "Lcom/gitlab/srcmc/rctmod/api/service/TrainerManager;getPlayerLevel(Lnet/minecraft/class_1657;)I";

    @Redirect(method = "canBattleAgainst", at = @At(value = "INVOKE", target = GET_PLAYER_LEVEL), remap = false)
    private int levelsync$capLevelForBattle(TrainerManager tm, class_1657 player) {
        return levelsync$cappedLevel(tm, player);
    }

    @Redirect(method = "replyTo", at = @At(value = "INVOKE", target = GET_PLAYER_LEVEL), remap = false)
    private int levelsync$capLevelForReply(TrainerManager tm, class_1657 player) {
        return levelsync$cappedLevel(tm, player);
    }

    @Redirect(method = "startBattleWith", at = @At(value = "INVOKE",
        target = "Lcom/gitlab/srcmc/rctmod/api/RCTMod;makeBattle(Lcom/gitlab/srcmc/rctmod/world/entities/TrainerMob;Lnet/minecraft/class_1657;)Z"),
        remap = false)
    private boolean levelsync$trackBattleTrainer(RCTMod rct, TrainerMob mob, class_1657 player) {
        BattleTrainer.set(mob);

        try {
            return rct.makeBattle(mob, player);
        } finally {
            BattleTrainer.clear();
        }
    }

    private int levelsync$cappedLevel(TrainerManager tm, class_1657 player) {
        int level = tm.getPlayerLevel(player);

        if (!LevelSyncConfig.scaleDown()) {
            return level;
        }

        // with keyTrainersOnly, other trainers are fought with the real levels, so only
        // ignore the level cap for them if configured
        if (LevelSyncConfig.keyTrainersOnly() && !LevelSyncConfig.othersIgnoreCap()
            && !BattleTrainer.isKeyTrainer((TrainerMob) (Object) this)) {
            return level;
        }

        return Math.min(level, tm.getData(player).getLevelCap());
    }
}
