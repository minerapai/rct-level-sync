package kr.levelsync.mixin;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.pokemon.EVs;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kr.levelsync.ScaledClones;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** EV yields at the end of a battle go to the real pokemon instead of a scaled clone. */
@Mixin(value = PokemonBattle.class, remap = false)
public abstract class PokemonBattleMixin {
    @Redirect(method = "end", at = @At(value = "INVOKE",
        target = "Lcom/cobblemon/mod/common/pokemon/Pokemon;getEvs()Lcom/cobblemon/mod/common/pokemon/EVs;"),
        remap = false)
    private EVs levelsync$evsOfOriginal(Pokemon pokemon) {
        Pokemon original = ScaledClones.originalOf(pokemon);
        return (original != null ? original : pokemon).getEvs();
    }
}
