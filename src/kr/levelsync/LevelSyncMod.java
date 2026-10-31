package kr.levelsync;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2561;

public class LevelSyncMod implements ModInitializer {
    @Override
    public void onInitialize() {
        LevelSyncConfig.scaleDown(); // create/normalize the config file on startup
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<class_2168> dispatcher) {
        dispatcher.register(class_2170.method_9247("levelsync")
            .requires(source -> source.method_9259(2))
            .executes(LevelSyncMod::show)
            .then(class_2170.method_9247("scaleDown")
                .then(class_2170.method_9244("enabled", BoolArgumentType.bool())
                    .executes(ctx -> {
                        LevelSyncConfig.setScaleDown(BoolArgumentType.getBool(ctx, "enabled"));
                        return show(ctx);
                    })))
            .then(class_2170.method_9247("scaleUp")
                .then(class_2170.method_9244("enabled", BoolArgumentType.bool())
                    .executes(ctx -> {
                        LevelSyncConfig.setScaleUp(BoolArgumentType.getBool(ctx, "enabled"));
                        return show(ctx);
                    })))
            .then(class_2170.method_9247("offset")
                .then(class_2170.method_9244("value", IntegerArgumentType.integer(-100, 100))
                    .executes(ctx -> {
                        LevelSyncConfig.setScaleUpOffset(IntegerArgumentType.getInteger(ctx, "value"));
                        return show(ctx);
                    })))
            .then(class_2170.method_9247("keyTrainersOnly")
                .then(class_2170.method_9244("enabled", BoolArgumentType.bool())
                    .executes(ctx -> {
                        LevelSyncConfig.setKeyTrainersOnly(BoolArgumentType.getBool(ctx, "enabled"));
                        return show(ctx);
                    })))
            .then(class_2170.method_9247("othersIgnoreCap")
                .then(class_2170.method_9244("enabled", BoolArgumentType.bool())
                    .executes(ctx -> {
                        LevelSyncConfig.setOthersIgnoreCap(BoolArgumentType.getBool(ctx, "enabled"));
                        return show(ctx);
                    })))
            .then(class_2170.method_9247("transferRewards")
                .then(class_2170.method_9244("enabled", BoolArgumentType.bool())
                    .executes(ctx -> {
                        LevelSyncConfig.setTransferRewards(BoolArgumentType.getBool(ctx, "enabled"));
                        return show(ctx);
                    })))
            .then(class_2170.method_9247("reload")
                .executes(ctx -> {
                    LevelSyncConfig.load();
                    return show(ctx);
                })));
    }

    private static int show(CommandContext<class_2168> ctx) {
        int offset = LevelSyncConfig.scaleUpOffset();
        String text = "[RCT Level Sync] scaleDown=" + LevelSyncConfig.scaleDown()
            + ", scaleUp=" + LevelSyncConfig.scaleUp()
            + ", offset=" + offset
            + " (scale up target = level cap" + (offset >= 0 ? " + " + offset : " - " + (-offset)) + ")"
            + ", keyTrainersOnly=" + LevelSyncConfig.keyTrainersOnly()
            + ", othersIgnoreCap=" + LevelSyncConfig.othersIgnoreCap()
            + ", transferRewards=" + LevelSyncConfig.transferRewards();
        ctx.getSource().method_9226(() -> class_2561.method_43470(text), true);
        return 1;
    }
}
