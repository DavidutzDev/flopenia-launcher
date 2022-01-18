package fr.flopenia.launcher.ui.panels.pages;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import fr.flopenia.launcher.Launcher;
import fr.flopenia.launcher.game.MinecraftInfos;
import fr.flopenia.launcher.ui.PanelManager;
import fr.flopenia.launcher.ui.panel.Panel;
import fr.flopenia.launcher.utils.Constants;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.CurseFileInfo;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.download.json.OptiFineInfo;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;

public class App extends Panel {

    private GridPane centerPane = new GridPane();
    private GridPane appPane = new GridPane();
    private GridPane topPanel = new GridPane();
    private String showedMenu;

    Saver saver = Launcher.instance.getSaver();

    ProgressBar progressBar = new ProgressBar();
    Label stepLabel = new Label();
    Label fileLabel = new Label();
    boolean isDownloading = false;

    MinecraftInfos minecraftInfos = new MinecraftInfos();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStyleSheetPath() {
        return "css/app.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        GridPane backgroundImage = new GridPane();

        setCanTakeAllSize(backgroundImage);
        backgroundImage.getStyleClass().add("background-image");
        backgroundImage.setStyle("-fx-background-image: url(images/background.jpg);");
        this.layout.add(backgroundImage, 0, 0);

        ColumnConstraints menuPainContraint = new ColumnConstraints();
        menuPainContraint.setHalignment(HPos.LEFT);
        menuPainContraint.setMinWidth(270);
        menuPainContraint.setMaxWidth(270);
        this.appPane.getColumnConstraints().addAll(menuPainContraint, new ColumnConstraints());


        GridPane leftBarPane = new GridPane();
        GridPane.setVgrow(leftBarPane, Priority.ALWAYS);
        GridPane.setHgrow(leftBarPane, Priority.ALWAYS);

        Button logoutButton = new Button();
        GridPane.setVgrow(logoutButton, Priority.ALWAYS);
        GridPane.setHgrow(logoutButton, Priority.ALWAYS);
        GridPane.setValignment(logoutButton, VPos.TOP);
        GridPane.setHalignment(logoutButton, HPos.LEFT);
        MaterialDesignIconView logoutIcon = new MaterialDesignIconView(MaterialDesignIcon.LOGOUT);
        logoutIcon.setSize("18px");
        logoutIcon.setFill(Color.rgb(17, 95, 170));
        logoutButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.0); -fx-border-color: #155faa; -fx-border-radius: 2px;");
        logoutButton.setTranslateX(260);
        logoutButton.setTranslateY(650);
        logoutButton.setMinWidth(26);
        logoutButton.setMaxWidth(26);
        logoutButton.setMinHeight(26);
        logoutButton.setMaxHeight(26);
        logoutButton.setGraphic(logoutIcon);
        logoutButton.setOnMouseEntered(e -> this.appPane.setCursor(Cursor.HAND));
        logoutButton.setOnMouseExited(e -> this.appPane.setCursor(Cursor.DEFAULT));
        logoutButton.setOnMouseClicked(e -> {
            if (showedMenu == "homeMenu" && this.isDownloading) {
                return;
            }

            saver.remove("accesToken");
            saver.remove("clientToken");
            saver.remove("msAccessToken");
            saver.remove("msRefreshToken");
            saver.remove("offline-username");
            saver.save();
            Launcher.getInstance().setAuthInfos(null);
            this.panelManager.showPanel(new Login());
        });

        Separator rightSeparator = new Separator();
        GridPane.setVgrow(rightSeparator, Priority.ALWAYS);
        GridPane.setHgrow(rightSeparator, Priority.ALWAYS);
        GridPane.setHalignment(rightSeparator, HPos.RIGHT);
        rightSeparator.setOrientation(Orientation.VERTICAL);
        rightSeparator.setTranslateY(1);
        rightSeparator.setTranslateX(4);
        rightSeparator.setMinWidth(2);
        rightSeparator.setMaxWidth(2);
        rightSeparator.setOpacity(0.30D);

        GridPane bottomGridPane = new GridPane();
        GridPane.setVgrow(bottomGridPane, Priority.ALWAYS);
        GridPane.setHgrow(bottomGridPane, Priority.ALWAYS);
        GridPane.setHalignment(bottomGridPane, HPos.LEFT);
        GridPane.setValignment(bottomGridPane, VPos.TOP);
        bottomGridPane.setTranslateY(30);
        bottomGridPane.setMinHeight(40);
        bottomGridPane.setMaxHeight(40);
        bottomGridPane.setMinWidth(300);
        bottomGridPane.setMaxWidth(300);
        showLeftBar(bottomGridPane);
        leftBarPane.getChildren().addAll(rightSeparator, bottomGridPane);

        this.appPane.add(leftBarPane, 0, 0);
        this.appPane.add(this.centerPane, 1, 0);
        GridPane.setVgrow(centerPane, Priority.ALWAYS);
        GridPane.setHgrow(centerPane, Priority.ALWAYS);

        GridPane.setVgrow(topPanel, Priority.ALWAYS);
        GridPane.setHgrow(topPanel, Priority.ALWAYS);
        GridPane.setValignment(topPanel, VPos.TOP);
        topPanel.setMinWidth(880);
        topPanel.setMaxWidth(880);
        topPanel.setMinHeight(340);
        topPanel.setMaxHeight(340);
        topPanel.setTranslateX(50);
        showHomeMenu(topPanel);

        appPane.getChildren().add(logoutButton);

        centerPane.getChildren().add(topPanel);

        this.layout.getChildren().add(appPane);
    }

    private void showHomeMenu(GridPane pane) {
        showedMenu = "homeMenu";

        Label Title = new Label("Flopenia");
        GridPane.setVgrow(Title, Priority.ALWAYS);
        GridPane.setHgrow(Title, Priority.ALWAYS);
        GridPane.setValignment(Title, VPos.TOP);
        Title.setStyle("-fx-font-size: 26px; -fx-text-fill: #fff; -fx-font-weight: bold;");
        Title.setTranslateY(20);

        Label adj1 = new Label("Survie");
        GridPane.setVgrow(adj1, Priority.ALWAYS);
        GridPane.setHgrow(adj1, Priority.ALWAYS);
        GridPane.setValignment(adj1, VPos.TOP);
        adj1.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-opacity: 70%;");
        adj1.setTranslateY(70);

        Label adj2 = new Label("modée");
        GridPane.setVgrow(adj2, Priority.ALWAYS);
        GridPane.setHgrow(adj2, Priority.ALWAYS);
        GridPane.setValignment(adj2, VPos.TOP);
        adj2.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-opacity: 70%;");
        adj2.setTranslateY(70);
        adj2.setTranslateX(45);

        Label desc = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do \n" +
                "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut\n" +
                "enim ad minim veniam, quis nostrud exercitation ullamco\n" +
                "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure\n" +
                "dolor in reprehenderit in voluptate velit esse cillum dolore eu\n" +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non\n" +
                "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        GridPane.setVgrow(desc, Priority.ALWAYS);
        GridPane.setHgrow(desc, Priority.ALWAYS);
        GridPane.setValignment(desc, VPos.TOP);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #bcc6e7; -fx-opacity: 90%;");
        desc.setTranslateY(110);

        Button playButton = new Button("Jouer");
        GridPane.setVgrow(playButton, Priority.ALWAYS);
        GridPane.setHgrow(playButton, Priority.ALWAYS);
        GridPane.setValignment(playButton, VPos.TOP);
        GridPane.setHalignment(playButton, HPos.LEFT);
        playButton.setTranslateY(260);
        playButton.setMinWidth(140);
        playButton.setMaxHeight(40);
        playButton.setStyle("-fx-background-color: #116ffa; -fx-border-radius: 0; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: #fff");
        playButton.setOnMouseEntered(e -> this.appPane.setCursor(Cursor.HAND));
        playButton.setOnMouseExited(e -> this.appPane.setCursor(Cursor.DEFAULT));
        playButton.setOnMouseClicked(e -> {
            playButton.setDisable(true);
            this.play(pane);
        });

        Button settingsButton = new Button();
        GridPane.setVgrow(settingsButton, Priority.ALWAYS);
        GridPane.setHgrow(settingsButton, Priority.ALWAYS);
        GridPane.setValignment(settingsButton, VPos.TOP);
        GridPane.setHalignment(settingsButton, HPos.LEFT);
        MaterialDesignIconView settingsIcon = new MaterialDesignIconView(MaterialDesignIcon.SETTINGS);
        settingsIcon.setSize("18px");
        settingsIcon.setFill(Color.rgb(17, 95, 170));
        settingsButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.0); -fx-border-color: #155faa; -fx-border-radius: 2px;");
        settingsButton.setTranslateX(150);
        settingsButton.setTranslateY(266);
        settingsButton.setMinWidth(26);
        settingsButton.setMaxWidth(26);
        settingsButton.setMinHeight(26);
        settingsButton.setMaxHeight(26);
        settingsButton.setGraphic(settingsIcon);
        settingsButton.setOnMouseEntered(e -> this.appPane.setCursor(Cursor.HAND));
        settingsButton.setOnMouseExited(e -> this.appPane.setCursor(Cursor.DEFAULT));
        settingsButton.setOnMouseClicked(e -> {
            if (showedMenu == "homeMenu" && this.isDownloading) {
                return;
            }
            topPanel.getChildren().clear();
            showSettingsMenu(pane);
        });

        progressBar.setStyle("-fx-background-color: rgba(3, 48, 90);");
        progressBar.setMinWidth(450);
        progressBar.setTranslateY(150);

        stepLabel.setStyle("-fx-text-fill: rgba(255, 255, 255);");
        setCenterH(stepLabel);
        stepLabel.setTranslateX(-80);
        stepLabel.setMinWidth(450);
        stepLabel.setTranslateY(170);

        fileLabel.setStyle("-fx-text-fill: rgba(255, 255, 255);");
        setCenterH(fileLabel);
        fileLabel.setTranslateX(-100);
        fileLabel.setMinWidth(450);
        fileLabel.setTranslateY(185);
        fileLabel.setDisable(true);

        pane.getChildren().addAll(Title, adj1, adj2, desc, playButton, settingsButton);
    }

    private void showSettingsMenu(GridPane pane) {
        showedMenu = "settingsMenu";

        minecraftInfos.setOPTIONAL_MOD_SCHEMATICA(Boolean.parseBoolean(saver.get("optionalModSchematica")));

        Label Title = new Label("Paramètre du Launcher");
        GridPane.setVgrow(Title, Priority.ALWAYS);
        GridPane.setHgrow(Title, Priority.ALWAYS);
        GridPane.setValignment(Title, VPos.TOP);
        Title.setStyle("-fx-font-size: 26px; -fx-text-fill: #fff; -fx-font-weight: bold;");
        Title.setTranslateY(20);

        Label ramLabel = new Label("Mémoire ram");
        GridPane.setVgrow(ramLabel, Priority.ALWAYS);
        GridPane.setHgrow(ramLabel, Priority.ALWAYS);
        GridPane.setValignment(ramLabel, VPos.TOP);
        ramLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff;");
        ramLabel.setTranslateY(70);

        //RAM SELECTIOR
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getStyleClass().add("ram-selector");
        for (int i = 512; i <= Math.ceil(memory.getTotal() / Math.pow(1024, 2)); i += 512) {
            comboBox.getItems().add(i / 1024.0 + " Go");
        }

        int val = 1024;
        try {
            if (saver.get("maxRam") != null) {
                val = Integer.parseInt(saver.get("maxRam"));
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException error) {
            saver.set("maxRam", String.valueOf(val));
            saver.save();
        }

        if (comboBox.getItems().contains(val / 1024.0 + " Go")) {
            comboBox.setValue(val / 1024.0 + " Go");
        } else {
            comboBox.setValue("1.0 Go");
        }

        GridPane.setVgrow(comboBox, Priority.ALWAYS);
        GridPane.setHgrow(comboBox, Priority.ALWAYS);
        GridPane.setValignment(comboBox, VPos.TOP);
        comboBox.getStyleClass().add("ramComboBox");
        comboBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.0); -fx-border-color: #155faa; -fx-border-radius: 2px;");
        comboBox.setTranslateY(100);

        //Save Button
        Button saveButton = new Button();
        GridPane.setVgrow(saveButton, Priority.ALWAYS);
        GridPane.setHgrow(saveButton, Priority.ALWAYS);
        GridPane.setValignment(saveButton, VPos.TOP);
        GridPane.setHalignment(saveButton, HPos.LEFT);
        MaterialDesignIconView saveIconView = new MaterialDesignIconView(MaterialDesignIcon.CONTENT_SAVE);
        saveIconView.setFill(Color.rgb(17, 95, 170));
        saveIconView.setSize("15");
        saveButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.0); -fx-border-color: #155faa; -fx-border-radius: 2px;");
        saveButton.setTranslateX(280);
        saveButton.setTranslateY(650);
        saveButton.setMinWidth(29);
        saveButton.setMaxWidth(29);
        saveButton.setMinHeight(29);
        saveButton.setMaxHeight(29);
        saveButton.setGraphic(saveIconView);
        saveButton.setOnMouseEntered(e -> this.appPane.setCursor(Cursor.HAND));
        saveButton.setOnMouseExited(e -> this.appPane.setCursor(Cursor.DEFAULT));
        saveButton.setOnMouseClicked(e -> {
            double _val = Double.parseDouble(comboBox.getValue().replace(" Go", ""));
            _val *= 1024;
            saver.set("maxRam", String.valueOf((int) _val));
            saver.save();
            topPanel.getChildren().clear();
            showHomeMenu(pane);
        });
        //Optional mods
        Label optionalModsLabel = new Label("Mods optionnel");
        GridPane.setVgrow(optionalModsLabel, Priority.ALWAYS);
        GridPane.setHgrow(optionalModsLabel, Priority.ALWAYS);
        optionalModsLabel.setTranslateY(0);
        optionalModsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff;");

        //Schematica
        CheckBox optionalModSchematicaChbox = new CheckBox("Schematica");
        optionalModSchematicaChbox.setSelected(minecraftInfos.OPTIONAL_MOD_SCHEMATICA);
        GridPane.setVgrow(optionalModSchematicaChbox, Priority.ALWAYS);
        GridPane.setHgrow(optionalModSchematicaChbox, Priority.ALWAYS);
        optionalModSchematicaChbox.setTranslateY(30);
        optionalModSchematicaChbox.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff;");
        optionalModSchematicaChbox.getStyleClass().add("optionalModSchematicaChbox");

        optionalModSchematicaChbox.setOnMouseClicked(e -> {
            minecraftInfos.setOPTIONAL_MOD_SCHEMATICA(optionalModSchematicaChbox.isSelected());
            saver.set("optionalModSchematica", String.valueOf(minecraftInfos.OPTIONAL_MOD_SCHEMATICA));
        });


        pane.getChildren().addAll(Title, ramLabel, comboBox, saveButton, optionalModsLabel, optionalModSchematicaChbox);
    }

    private void showLeftBar(GridPane pane) {

        Separator blueLeftSeparator = new Separator();
        GridPane.setVgrow(blueLeftSeparator, Priority.ALWAYS);
        GridPane.setHgrow(blueLeftSeparator, Priority.ALWAYS);
        blueLeftSeparator.setOrientation(Orientation.VERTICAL);
        blueLeftSeparator.setMinWidth(3);
        blueLeftSeparator.setMaxWidth(3);
        blueLeftSeparator.setMinHeight(40);
        blueLeftSeparator.setMaxHeight(40);
        blueLeftSeparator.setStyle("-fx-background-color: rgb(5, 179, 242); -fx-border-width: 3 3 3 0; -fx-border-color: rgb(5, 179, 242);");

        String avatarUrl = avatarUrl(Constants.AVATAR_URL);

        ImageView avatarView = new ImageView();
        Image avatarImg = new Image(avatarUrl);
        avatarView.setImage(avatarImg);
        avatarView.setPreserveRatio(true);
        avatarView.setFitHeight(50d);

        GridPane.setHgrow(avatarView, Priority.ALWAYS);
        GridPane.setVgrow(avatarView, Priority.ALWAYS);
        GridPane.setValignment(avatarView, VPos.CENTER);
        avatarView.setTranslateX(34);
        avatarView.setPreserveRatio(true);
        avatarView.setFitWidth(28);

        Label playerNameLabel = new Label(Launcher.getInstance().getAuthInfos().getUsername());
        GridPane.setHgrow(playerNameLabel, Priority.ALWAYS);
        GridPane.setVgrow(playerNameLabel, Priority.ALWAYS);
        GridPane.setValignment(playerNameLabel, VPos.CENTER);
        playerNameLabel.setTranslateX(90);
        playerNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff;");

        pane.getChildren().addAll(blueLeftSeparator, avatarView, playerNameLabel);
    }

    // GAME UPDATER AND STARTER

    public void play(GridPane pane) {
        isDownloading = true;
        setProgress(0, 0);
        pane.getChildren().addAll(progressBar, stepLabel, fileLabel);

        Platform.runLater(() -> new Thread(this::update).start());
    }

    public void update() {

        IProgressCallback callback = new IProgressCallback() {
            private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
            private String stepTxt = "";
            private String percentTxt = "0.0%";

            @Override
            public void step(Step step) {
                Platform.runLater(() -> {
                    stepTxt = StepInfo.  valueOf(step.name()).getDetails();
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                });
            }

            @Override
            public void update(long downloaded, long max) {
                Platform.runLater(() -> {
                    percentTxt = decimalFormat.format(downloaded * 100d / max) + "%";
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                    setProgress(downloaded, max);
                });

            }

            @Override
            public void onFileDownloaded(Path path) {
                Platform.runLater(() -> {
                    String p = path.toString();
                    fileLabel.setText("..." + p.replace(Launcher.getInstance().getLauncherDir().toFile().getAbsolutePath(), ""));
                });
            }
        };
        try {
            final VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder()
                    .withName(MinecraftInfos.GAME_VERSION)
                    .withVersionType(MinecraftInfos.VERSION_TYPE)
                    .build();
            final UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
                    .build();

            List<CurseFileInfo> curseMods = CurseFileInfo.getFilesFromJson(MinecraftInfos.CURSE_MODS_LIST_URL);
            List<Mod> mods = Mod.getModsFromJson(MinecraftInfos.MODS_LIST_URL);

            if (minecraftInfos.OPTIONAL_MOD_SCHEMATICA) {
                curseMods.add(new CurseFileInfo(225603,2554100));
                curseMods.add(new CurseFileInfo(225605, 2489549));
            }

            final AbstractForgeVersion forge = new ForgeVersionBuilder(MinecraftInfos.FORGE_VERSION_TYPE)
                    .withOptiFine(new OptiFineInfo(MinecraftInfos.OPTIFINE_VERSION, false))
                    .withForgeVersion(MinecraftInfos.FORGE_VERSION)
                    .withCurseMods(curseMods)
                    .withMods(mods)
                    .withFileDeleter(new ModFileDeleter(true, "flopeniacore.jar"))
                    .build();

            final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                    .withVanillaVersion(vanillaVersion)
                    .withForgeVersion(forge)
                    .withLogger(Launcher.getInstance().getLogger())
                    .withProgressCallback(callback)
                    .withUpdaterOptions(options)
                    .build();

            updater.update(Launcher.getInstance().getLauncherDir());
            this.startGame(updater.getForgeVersion().getForgeVersion());
        } catch (Exception exception) {
            Launcher.getInstance().getLogger().err(exception.toString());
            exception.printStackTrace();
            Platform.runLater(() -> panelManager.getStage().show());
        }
    }

    public void startGame(String gameVersion) {

        File gameFolder = Launcher.getInstance().getLauncherDir().toFile();

        GameInfos infos = new GameInfos(
                Constants.LAUNCHER_NAME,
                gameFolder,
                new GameVersion(gameVersion, MinecraftInfos.OLL_GAME_TYPE),
                new GameTweak[]{GameTweak.FORGE}
        );

        try {
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER, Launcher.getInstance().getAuthInfos());
            profile.getVmArgs().add(this.getRamArgsFromSaver());
            ExternalLauncher launcher = new ExternalLauncher(profile);

            Platform.runLater(() -> panelManager.getStage().hide());

            Process p = launcher.launch();

            Platform.runLater(() -> {
                try {
                    p.waitFor();
                    Platform.exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            Launcher.getInstance().getLogger().err(exception.toString());
        }
    }

    public String getRamArgsFromSaver() {
        int val = 1024;
        try {
            if (saver.get("maxRam") != null) {
                val = Integer.parseInt(saver.get("maxRam"));
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException error) {
            saver.set("maxRam", String.valueOf(val));
            saver.save();
        }

        return "-Xmx" + val + "M";
    }

    public void setStatus(String status) {
        this.stepLabel.setText(status);
    }

    public void setProgress(double current, double max) {
        this.progressBar.setProgress(current / max);
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public enum StepInfo {
        READ("Lecture du fichier json..."),
        DL_LIBS("Téléchargement des libraries..."),
        DL_ASSETS("Téléchargement des ressources..."),
        EXTRACT_NATIVES("Extraction des natives..."),
        FORGE("Installation de forge..."),
        FABRIC("Installation de fabric..."),
        MODS("Téléchargement des mods..."),
        EXTERNAL_FILES("Téléchargement des fichier externes..."),
        POST_EXECUTIONS("Exécution post-installation..."),
        END("Finit !");
        String details;

        StepInfo(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }
    }

    public String avatarUrl(String avatarUrl) {
        if (saver.get("offline-username") != "") {
            return avatarUrl + "MHF_Steve";
        } else {
            return avatarUrl + Launcher.getInstance().getAuthInfos().getUuid();
        }
    }
}