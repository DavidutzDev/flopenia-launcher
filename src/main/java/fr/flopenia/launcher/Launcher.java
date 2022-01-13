package fr.flopenia.launcher;

import fr.flopenia.launcher.ui.PanelManager;
import fr.flopenia.launcher.ui.panels.pages.App;
import fr.flopenia.launcher.ui.panels.pages.Login;
import fr.flopenia.launcher.utils.Constants;
import fr.flopenia.launcher.utils.Helpers;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthProfile;
import fr.litarvan.openauth.model.response.RefreshResponse;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


public class Launcher extends Application {
    private PanelManager panelManager;
    public static Launcher instance;
    private final ILogger logger;
    private final Path launcherDir = Paths.get(Helpers.generateGamePath(Constants.LAUNCHER_NAME).getPath());
    private final Saver saver;

    private AuthInfos authInfos = null;

    public Launcher() {
        instance = this;
        this.logger = new Logger("[FlopeniaLauncher]", Paths.get(this.launcherDir.toString(), "launcher.log"));
        if (!this.launcherDir.toFile().exists()) {
            if (!this.launcherDir.toFile().mkdir()) {
                this.logger.err("Unable to create launcher folder");
            }
        }

        saver = new Saver(Paths.get(launcherDir.toString(), "config.properties"));

        saver.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.logger.info("Starting Launcher");
        this.panelManager = new PanelManager(this, stage);
        this.panelManager.init();

        if (this.isUserAlreadyLoggedIn()) {
            logger.info("Hello" + " " + authInfos.getUsername() + " !");
            panelManager.showPanel(new App());
        }else {
            this.panelManager.showPanel(new Login());
        }
    }

    public boolean isUserAlreadyLoggedIn() {
        if (saver.get("accessToken") != null && saver.get("clientToken") != null) {
            Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);

            try {
                RefreshResponse response =  authenticator.refresh(saver.get("accessToken"), saver.get("clientToken"));
                saver.set("accessToken", response.getAccessToken());
                saver.set("clientToken", response.getClientToken());
                saver.save();
                this.setAuthInfos(new AuthInfos(
                        response.getSelectedProfile().getName(),
                        response.getAccessToken(),
                        response.getClientToken(),
                        response.getSelectedProfile().getId()
                ));

                return true;
            }catch (AuthenticationException ignored) {
                saver.remove("accessToken");
                saver.remove("clientToken");
                saver.save();
            }
        } else if (saver.get("msAccessToken") != null && saver.get("msRefreshToken") != null) {

            try {
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                MicrosoftAuthResult response = authenticator.loginWithRefreshToken(saver.get("msRefreshToken"));

                saver.set("msAccessToken", response.getAccessToken());
                saver.set("msRefreshToken", response.getRefreshToken());
                saver.save();

                this.setAuthInfos(new AuthInfos(
                        response.getProfile().getName(),
                        response.getAccessToken(),
                        response.getProfile().getId()
                ));


            } catch (MicrosoftAuthenticationException e) {
                saver.remove("msAccessToken");
                saver.remove("msRefreshToken");
                saver.save();
            }

        }else if (saver.get("offline-username") != null) {
            this.authInfos = new AuthInfos(saver.get("offline-username"), UUID.randomUUID().toString(), UUID.randomUUID().toString());
            return true;
        }

        return false;
    }

    public void setAuthInfos(AuthInfos authInfos) {
        this.authInfos = authInfos;
    }

    public AuthInfos getAuthInfos() {
        return authInfos;
    }

    public ILogger getLogger() {
        return logger;
    }

    public static Launcher getInstance()     {
        return instance;
    }

    public Saver getSaver() {
        return saver;
    }

    public Path getLauncherDir() {
        return launcherDir;
    }

    public void Stop() {
        Platform.exit();
        System.exit(0);
    }

    public void hideWindow() {
        this.panelManager.getStage().hide();
    }

}
