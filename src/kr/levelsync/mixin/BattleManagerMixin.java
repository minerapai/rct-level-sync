package kr.levelsync.mixin;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.gitlab.srcmc.rctapi.api.battle.BattleManager;
import com.gitlab.srcmc.rctapi.api.trainer.TrainerPlayer;
import kr.levelsync.BattleTrainer;
import kr.levelsync.LevelSyncConfig;
import kr.levelsync.ScaledClones;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Scales the player's pokemon to their RCT level cap for trainer battles. The levels are
 * only applied to battle clones, so the real party pokemon keep their level (and HP).
 */
@Mixin(value = BattleManager.class, remap = false)
public abstract class BattleManagerMixin {
    private static final String TO_BATTLE_ACTOR =
        "toBattleActor(Lcom/gitlab/srcmc/rctapi/api/trainer/TrainerPlayer;Z)Lcom/cobblemon/mod/common/api/battles/model/actor/BattleActor;";

    @ModifyVariable(method = TO_BATTLE_ACTOR, at = @At("HEAD"), argsOnly = true, remap = false)
    private static boolean levelsync$cloneIfScaling(boolean clone, TrainerPlayer trainer) {
        if (clone) {
            return true;
        }

        if (!BattleTrainer.shouldScale()) {
            return false;
        }

        for (Pokemon pokemon : trainer.getTeam()) {
            if (pokemon != null && levelsync$targetLevel(trainer, pokemon.getLevel()) != pokemon.getLevel()) {
                return true;
            }
        }

        return false;
    }

    @Inject(method = TO_BATTLE_ACTOR, at = @At("RETURN"), remap = false)
    private static void levelsync$scaleClones(TrainerPlayer trainer, boolean clone, CallbackInfoReturnable<BattleActor> cir) {
        if (!BattleTrainer.shouldScale()) {
            return;
        }

        for (BattlePokemon bp : cir.getReturnValue().getPokemonList()) {
            Pokemon pokemon = bp.getEffectedPokemon();

            if (pokemon == bp.getOriginalPokemon()) {
                continue; // not a clone, never touch the real pokemon
            }

            int target = levelsync$targetLevel(trainer, pokemon.getLevel());

            if (target != pokemon.getLevel()) {
                double hpRatio = pokemon.getMaxHealth() > 0 ? (double) pokemon.getCurrentHealth() / pokemon.getMaxHealth() : 1.0;
                pokemon.setLevel(target);
                pokemon.setCurrentHealth(Math.max(1, (int) Math.round(hpRatio * pokemon.getMaxHealth())));
            }

            // registered after scaling, so the level change itself never touches the original
            ScaledClones.register(pokemon, bp.getOriginalPokemon());
        }
    }

    private static int levelsync$targetLevel(TrainerPlayer trainer, int level) {
        int cap = BattleTrainer.levelCap(trainer.getPlayer());

        if (LevelSyncConfig.scaleDown() && level > cap) {
            return cap;
        }

        int upTarget = Math.max(1, Math.min(100, cap + LevelSyncConfig.scaleUpOffset()));

        if (LevelSyncConfig.scaleUp() && level < upTarget) {
            return upTarget;
        }

        return level;
    }
}
