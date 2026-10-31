package kr.levelsync.mixin;

import com.cobblemon.mod.common.api.pokemon.evolution.EvolutionProxy;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kr.levelsync.ScaledClones;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Evolution progress made in battle (defeating certain pokemon, using moves, taking damage, ...)
 * is recorded through the evolution proxy, so a scaled clone uses the real pokemon's proxy.
 */
@Mixin(value = Pokemon.class, remap = false)
public abstract class PokemonMixin {
    @Inject(method = "getEvolutionProxy", at = @At("HEAD"), cancellable = true, remap = false)
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void levelsync$proxyOfOriginal(CallbackInfoReturnable<EvolutionProxy> cir) {
        Pokemon original = ScaledClones.originalOf((Pokemon) (Object) this);

        if (original != null) {
            cir.setReturnValue(original.getEvolutionProxy());
        }
    }
}
