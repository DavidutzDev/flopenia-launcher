package fr.flopenia.launcher.game;

import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VersionType;
import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.theshark34.openlauncherlib.minecraft.GameType;

public class MinecraftInfos {

    public static final String GAME_VERSION = "1.12.2";
    public static final VersionType VERSION_TYPE = VersionType.FORGE;
    public static final ForgeVersionBuilder.ForgeVersionType FORGE_VERSION_TYPE = ForgeVersionBuilder.ForgeVersionType.NEW;
    public static final String FORGE_VERSION = "1.12.2-14.23.5.2859";
    public static final String OPTIFINE_VERSION = "1.12.2_HD_U_G5";

    public static final GameType OLL_GAME_TYPE = GameType.V1_8_HIGHER;
    public static final NewForgeVersionDiscriminator OLL_FORGE_DISCRIMINATOR = new NewForgeVersionDiscriminator(
            "14.23.5.2859",
            MinecraftInfos.GAME_VERSION,
            "0"
    );

    public static final String CURSE_MODS_LIST_URL = "https://flopenia.000webhostapp.com/mods_list.json";
    public static final String MODS_LIST_URL = "https://flopenia.000webhostapp.com/mods_list.json";

}
