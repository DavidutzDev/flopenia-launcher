package fr.flopenia.launcher;

import fr.flopenia.launcher.utils.Helpers;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class Launcher extends Application {
    public static Launcher instance;
    private final ILogger logger;
    private final File launcherDir = Helpers.generateGamePath("FlopeniaLauncher");

    public Launcher() {
        instance = this;
        this.logger = new Logger("[FlopeniaLauncher]", new File(this.launcherDir, "launcher.log"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public ILogger getLogger() {
        return logger;
    }

    public static Launcher getInstance() {
        return instance;
    }
}
