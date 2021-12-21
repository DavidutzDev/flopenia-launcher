package fr.flopenia.launcher;

import fr.flopenia.launcher.ui.PanelManager;
import fr.flopenia.launcher.ui.panels.pages.Login;
import fr.flopenia.launcher.ui.panels.partials.TopBar;
import fr.flopenia.launcher.utils.Constants;
import fr.flopenia.launcher.utils.Helpers;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

public class Launcher extends Application {
    private PanelManager panelManager;
    public static Launcher instance;
    private final ILogger logger;
    private final File launcherDir = Helpers.generateGamePath(Constants.LAUNCHER_NAME);
    private final Saver saver;

    public Launcher() {
        instance = this;
        this.logger = new Logger("[FlopeniaLauncher]", new File(this.launcherDir, "launcher.log"));
        if (!this.launcherDir.exists()) {
            if (!this.launcherDir.mkdir()) {
                this.logger.err("Unable to create launcher folder");
            }
        }

        saver = new Saver(new File(launcherDir, "config.properties"));

        saver.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.logger.info("Starting Launcher");
        this.panelManager = new PanelManager(this, stage);
        this.panelManager.init();

        this.panelManager.showPanel(new Login());
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

}
