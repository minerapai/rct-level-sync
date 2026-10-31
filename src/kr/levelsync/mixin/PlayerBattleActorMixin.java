package kr.levelsync.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.pokemon.experience.BattleExperienceSource;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import kr.levelsync.ScaledClones;
import net.minecraft.class_3222;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Cobblemon skips experience for battle clones. For scaled clones the experience is
 * given to the real pokemon instead (level ups, move learning and evolutions included).
 */
@Mixin(value = PlayerBattleActor.class, remap = false)
public abstract class PlayerBattleActorMixin {
    @Inject(method = "awardExperience", at = @At("HEAD"), cancellable = true, remap = false)
    private void levelsync$awardToOriginal(BattlePokemon battlePokemon, int experience, CallbackInfo ci) {
        Pokemon original = ScaledClones.originalOf(battlePokemon.getEffectedPokemon());

        if (original == null) {
            return;
        }

        ci.cancel();
        PlayerBattleActor actor = (PlayerBattleActor) (Object) this;
        PokemonBattle battle = actor.getBattle();

        if (experience <= 0 || (battle.isPvP() && !Cobblemon.INSTANCE.getConfig().getAllowExperienceFromPvP())) {
            return;
        }

        BattleExperienceSource source = new BattleExperienceSource(battle, List.copyOf(battlePokemon.getFacedOpponents()));
        class_3222 player = PlayerExtensionsKt.getPlayer(actor.getUuid());

        if (player != null) {
            original.addExperienceWithPlayer(player, source, experience);
        } else {
            original.addExperience(source, experience);
        }
    }
}
