package kr.levelsync;

import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class LevelSyncConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("rct-level-sync.properties");
    private static final String COMMENT = String.join(System.lineSeparator() + "# ",
        "RCT Level Sync",
        "level cap       : the level cap of the opposing trainer (its ace level + relativeLevelCap), so rematches",
        "                  against already cleared trainers use their own cap",
        "scaleDown       : pokemon above the level cap are lowered to the cap for RCT trainer battles",
        "                  (also allows starting the battle while over the cap)",
        "scaleUp         : pokemon below (level cap + scaleUpOffset) are raised to that level for RCT trainer battles",
        "scaleUpOffset   : offset added to the level cap when scaling up (e.g. -5 = trainer's ace level with relativeLevelCap=5)",
        "keyTrainersOnly : only scale battles against key trainers (non optional trainers of a series, e.g. gym leaders,",
        "                  elite four, champions, rivals), field trainers are fought with the real levels",
        "othersIgnoreCap : if keyTrainersOnly is enabled, still allow battles against other trainers while over the level cap",
        "transferRewards : give the rewards of scaled battle clones (experience, EVs, evolution progress) to the real pokemon",
        "Levels are only changed on battle clones, the real pokemon are never modified.",
        "In game (op level 2): /levelsync, /levelsync <option> <value>, /levelsync reload");

    private static volatile boolean scaleDown = true;
    private static volatile boolean scaleUp = true;
    private static volatile int scaleUpOffset = 0;
    private static volatile boolean keyTrainersOnly = true;
    private static volatile boolean othersIgnoreCap = true;
    private static volatile boolean transferRewards = true;

    static {
        load();
    }

    private LevelSyncConfig() {
    }

    public static boolean scaleDown() {
        return scaleDown;
    }

    public static boolean scaleUp() {
        return scaleUp;
    }

    public static int scaleUpOffset() {
        return scaleUpOffset;
    }

    public static boolean keyTrainersOnly() {
        return keyTrainersOnly;
    }

    public static boolean othersIgnoreCap() {
        return othersIgnoreCap;
    }

    public static boolean transferRewards() {
        return transferRewards;
    }

    public static synchronized void setTransferRewards(boolean value) {
        transferRewards = value;
        save();
    }

    public static synchronized void setScaleDown(boolean value) {
        scaleDown = value;
        save();
    }

    public static synchronized void setScaleUp(boolean value) {
        scaleUp = value;
        save();
    }

    public static synchronized void setScaleUpOffset(int value) {
        scaleUpOffset = value;
        save();
    }

    public static synchronized void setKeyTrainersOnly(boolean value) {
        keyTrainersOnly = value;
        save();
    }

    public static synchronized void setOthersIgnoreCap(boolean value) {
        othersIgnoreCap = value;
        save();
    }

    public static synchronized void load() {
        Properties p = new Properties();

        try {
            if (Files.exists(FILE)) {
                try (Reader r = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
                    p.load(r);
                }
            }
        } catch (Exception e) {
            System.err.println("[rct-level-sync] failed to read config: " + e);
        }

        scaleDown = Boolean.parseBoolean(p.getProperty("scaleDown", "true").trim());
        scaleUp = Boolean.parseBoolean(p.getProperty("scaleUp", "true").trim());
        keyTrainersOnly = Boolean.parseBoolean(p.getProperty("keyTrainersOnly", "true").trim());
        othersIgnoreCap = Boolean.parseBoolean(p.getProperty("othersIgnoreCap", "true").trim());
        transferRewards = Boolean.parseBoolean(p.getProperty("transferRewards", "true").trim());

        try {
            scaleUpOffset = Integer.parseInt(p.getProperty("scaleUpOffset", "0").trim());
        } catch (NumberFormatException e) {
            scaleUpOffset = 0;
        }

        save();
    }

    private static void save() {
        Properties p = new Properties();
        p.setProperty("scaleDown", Boolean.toString(scaleDown));
        p.setProperty("scaleUp", Boolean.toString(scaleUp));
        p.setProperty("scaleUpOffset", Integer.toString(scaleUpOffset));
        p.setProperty("keyTrainersOnly", Boolean.toString(keyTrainersOnly));
        p.setProperty("othersIgnoreCap", Boolean.toString(othersIgnoreCap));
        p.setProperty("transferRewards", Boolean.toString(transferRewards));

        try (Writer w = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            p.store(w, COMMENT);
        } catch (Exception e) {
            System.err.println("[rct-level-sync] failed to write config: " + e);
        }
    }
}
