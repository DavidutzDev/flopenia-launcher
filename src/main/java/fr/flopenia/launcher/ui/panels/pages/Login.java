package fr.flopenia.launcher.ui.panels.pages;

import fr.flopenia.launcher.Launcher;
import fr.flopenia.launcher.ui.PanelManager;
import fr.flopenia.launcher.ui.panel.Panel;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.AuthProfile;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jdk.nashorn.internal.objects.NativeUint8Array;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class Login extends Panel {

    Saver saver = Launcher.getInstance().getSaver();
    AtomicBoolean connectWithMojang = new AtomicBoolean(true);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStyleSheetPath() {
        return "css/login.css";
    }

    TextField usernameField = new TextField("");
    private final Button conectionButton = new Button("Se connecter");

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);
        GridPane loginPanel = new GridPane();
        GridPane mainPanel = new GridPane();
        GridPane bottomPanel = new GridPane();
        GridPane backgroundImage = new GridPane();

        setCanTakeAllSize(backgroundImage);
        backgroundImage.getStyleClass().add("background-image");
        backgroundImage.setStyle("-fx-background-image: url(images/background.jpg);");
        this.layout.add(backgroundImage, 0, 0);

        loginPanel.setMaxWidth(400);
        loginPanel.setMinWidth(400);
        loginPanel.setMaxHeight(480);
        loginPanel.setMinHeight(580);

        GridPane.setVgrow(loginPanel, Priority.ALWAYS);
        GridPane.setHgrow(loginPanel, Priority.ALWAYS);
        GridPane.setValignment(loginPanel, VPos.CENTER);
        GridPane.setHalignment(loginPanel, HPos.CENTER);

        RowConstraints bottomConstraints = new RowConstraints();
        bottomConstraints.setValignment(VPos.BOTTOM);
        bottomConstraints.setMaxHeight(55);
        loginPanel.getRowConstraints().addAll(new RowConstraints(), bottomConstraints);
        loginPanel.add(mainPanel, 0, 0);
        loginPanel.add(bottomPanel, 0, 1);

        GridPane.setVgrow(mainPanel, Priority.ALWAYS);
        GridPane.setHgrow(mainPanel, Priority.ALWAYS);
        GridPane.setVgrow(bottomPanel, Priority.ALWAYS);
        GridPane.setHgrow(bottomPanel, Priority.ALWAYS);

        mainPanel.setStyle("-fx-background-color: #181818;");
        bottomPanel.setStyle("-fx-background-color: #181818; -fx-opacity: 50%;");
        Label noAccount = new Label("Vous n'avez pas encore de compte ?");
        Label registerHere = new Label("S'inscrire ici");

        GridPane.setVgrow(noAccount, Priority.ALWAYS);
        GridPane.setHgrow(noAccount, Priority.ALWAYS);
        GridPane.setValignment(noAccount, VPos.TOP);
        GridPane.setHalignment(noAccount, HPos.CENTER);
        noAccount.setStyle("-fx-text-fill: #bcc6e7; -fx-font-size: 14px;");

        GridPane.setVgrow(registerHere, Priority.ALWAYS);
        GridPane.setHgrow(registerHere, Priority.ALWAYS);
        GridPane.setValignment(registerHere, VPos.BOTTOM);
        GridPane.setHalignment(registerHere, HPos.CENTER);
        registerHere.setStyle("-fx-text-fill: #69a7ed; -fx-font-size: 14px;");
        registerHere.setUnderline(true);
        registerHere.setTranslateY(-10);
        registerHere.setOnMouseEntered(e -> this.layout.setCursor(Cursor.HAND));
        registerHere.setOnMouseExited(e -> this.layout.setCursor(Cursor.DEFAULT));
        registerHere.setOnMouseClicked(e -> {
            openURL("https://www.minecraft.net/fr-fr/get-minecraft");
        });

        bottomPanel.getChildren().addAll(noAccount, registerHere);
        this.layout.getChildren().add(loginPanel);

        Label connectLabel = new Label("SE CONNECTER !");
        GridPane.setVgrow(connectLabel, Priority.ALWAYS);
        GridPane.setHgrow(connectLabel, Priority.ALWAYS);
        GridPane.setValignment(connectLabel, VPos.TOP);
        GridPane.setHalignment(connectLabel, HPos.CENTER);
        connectLabel.setTranslateY(27);
        connectLabel.setStyle("-fx-text-fill: #bcc6e7; -fx-font-size: 18px;");

        Separator connectSeparator = new Separator();
        GridPane.setVgrow(connectSeparator, Priority.ALWAYS);
        GridPane.setHgrow(connectSeparator, Priority.ALWAYS);
        GridPane.setValignment(connectSeparator, VPos.TOP);
        GridPane.setHalignment(connectSeparator, HPos.CENTER);
        connectSeparator.setTranslateY(60);
        connectSeparator.setMinWidth(325);
        connectSeparator.setMaxWidth(325);
        connectSeparator.setStyle("-fx-background-color: #fff; -fx-opacity: 50%;");

        Label usernameLabel = new Label("Adresse mail");
        GridPane.setVgrow(usernameLabel, Priority.ALWAYS);
        GridPane.setHgrow(usernameLabel, Priority.ALWAYS);
        GridPane.setValignment(usernameLabel, VPos.TOP);
        GridPane.setHalignment(usernameLabel, HPos.LEFT);
        usernameLabel.setStyle("-fx-text-fill: #95bad3; -fx-font-size: 14px;");
        usernameLabel.setTranslateY(110);
        usernameLabel.setTranslateX(37.5);

        GridPane.setVgrow(usernameField, Priority.ALWAYS);
        GridPane.setHgrow(usernameField, Priority.ALWAYS);
        GridPane.setValignment(usernameField, VPos.TOP);
        GridPane.setHalignment(usernameField, HPos.LEFT);
        usernameField.setStyle("-fx-background-color: #1e1e1e; -fx-font-size: 16px; -fx-text-fill: #e5e5e5;");
        usernameField.setMaxWidth(325);
        usernameField.setMaxHeight(40);
        usernameField.setTranslateX(37.5);
        usernameField.setTranslateY(140);

        Separator usernameSeparator = new Separator();
        GridPane.setVgrow(usernameSeparator, Priority.ALWAYS);
        GridPane.setHgrow(usernameSeparator, Priority.ALWAYS);
        GridPane.setValignment(usernameSeparator, VPos.TOP);
        GridPane.setHalignment(usernameSeparator, HPos.CENTER);
        usernameSeparator.setTranslateY(181);
        usernameSeparator.setMinWidth(325);
        usernameSeparator.setMaxWidth(325);
        usernameSeparator.setMaxHeight(1);
        usernameSeparator.setStyle("-fx-background-color: #fff; -fx-opacity: 40%;");

        Label passwordLabel = new Label("Mot de passe");
        GridPane.setVgrow(passwordLabel, Priority.ALWAYS);
        GridPane.setHgrow(passwordLabel, Priority.ALWAYS);
        GridPane.setValignment(passwordLabel, VPos.TOP);
        GridPane.setHalignment(passwordLabel, HPos.LEFT);
        passwordLabel.setStyle("-fx-text-fill: #95bad3; -fx-font-size: 14px;");
        passwordLabel.setTranslateY(200);
        passwordLabel.setTranslateX(37.5);

        PasswordField passwordField = new PasswordField();
        GridPane.setVgrow(passwordField, Priority.ALWAYS);
        GridPane.setHgrow(passwordField, Priority.ALWAYS);
        GridPane.setValignment(passwordField, VPos.TOP);
        GridPane.setHalignment(passwordField, HPos.LEFT);
        passwordField.setStyle("-fx-background-color: #1e1e1e; -fx-font-size: 16px; -fx-text-fill: #e5e5e5;");
        passwordField.setMaxWidth(325);
        passwordField.setMaxHeight(40);
        passwordField.setTranslateX(37.5);
        passwordField.setTranslateY(230);

        Separator passwordSeparator = new Separator();
        GridPane.setVgrow(passwordSeparator, Priority.ALWAYS);
        GridPane.setHgrow(passwordSeparator, Priority.ALWAYS);
        GridPane.setValignment(passwordSeparator, VPos.TOP);
        GridPane.setHalignment(passwordSeparator, HPos.CENTER);
        passwordSeparator.setTranslateY(271);
        passwordSeparator.setMinWidth(325);
        passwordSeparator.setMaxWidth(325);
        passwordSeparator.setMaxHeight(1);
        passwordSeparator.setStyle("-fx-background-color: #fff; -fx-opacity: 40%;");

        Label forgotPassword = new Label("Mot de passe oublié ?");
        GridPane.setVgrow(forgotPassword, Priority.ALWAYS);
        GridPane.setHgrow(forgotPassword, Priority.ALWAYS);
        GridPane.setValignment(forgotPassword, VPos.CENTER);
        GridPane.setHalignment(forgotPassword, HPos.LEFT);
        forgotPassword.setStyle("-fx-text-fill: #69a7ed; -fx-font-size: 12px;");
        forgotPassword.setUnderline(true);
        forgotPassword.setTranslateX(37.5);
        forgotPassword.setTranslateY(30);
        forgotPassword.setOnMouseEntered(e -> this.layout.setCursor(Cursor.HAND));
        forgotPassword.setOnMouseExited(e -> this.layout.setCursor(Cursor.DEFAULT));
        forgotPassword.setOnMouseClicked(e -> {
            openURL("https://www.minecraft.net/fr-fr/password/forgot");
        });

        Label errorLabel = new Label("");
        GridPane.setVgrow(errorLabel, Priority.ALWAYS);
        GridPane.setHgrow(errorLabel, Priority.ALWAYS);
        GridPane.setValignment(errorLabel, VPos.CENTER);
        GridPane.setHalignment(errorLabel, HPos.LEFT);
        errorLabel.setStyle("-fx-text-fill: #ff0800; -fx-font-size: 12px;");;
        errorLabel.setTranslateX(37.5);
        errorLabel.setTranslateY(55);

        GridPane.setVgrow(conectionButton, Priority.ALWAYS);
        GridPane.setHgrow(conectionButton, Priority.ALWAYS);
        GridPane.setValignment(conectionButton, VPos.CENTER);
        GridPane.setHalignment(conectionButton, HPos.LEFT);
        conectionButton.setTranslateX(37.5);
        conectionButton.setTranslateY(100);
        conectionButton.setMinWidth(325);
        conectionButton.setMinHeight(50);
        conectionButton.setStyle("-fx-background-color: #007dbe; -fx-border-radius: 0px; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: #fff");
        conectionButton.setOnMouseEntered(e -> this.layout.setCursor(Cursor.HAND));
        conectionButton.setOnMouseExited(e -> this.layout.setCursor(Cursor.DEFAULT));
        conectionButton.setOnMouseClicked(e -> {
            if (connectWithMojang.get()) {
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    errorLabel.setText("Champ Incomplet");
                } else {
                    this.authenticate(usernameField.getText(), passwordField.getText(), errorLabel);
                }
            } else {
                if (usernameField.getText().isEmpty()) {
                    errorLabel.setText("Champ Incomplet");
                } else {
                    authenticate(usernameField.getText(), null, errorLabel);
                }
            }
        });

        Separator choseConnectSeparator = new Separator();
        GridPane.setVgrow(choseConnectSeparator, Priority.ALWAYS);
        GridPane.setHgrow(choseConnectSeparator, Priority.ALWAYS);
        GridPane.setValignment(choseConnectSeparator, VPos.CENTER);
        GridPane.setHalignment(choseConnectSeparator, HPos.CENTER);
        choseConnectSeparator.setTranslateY(160);
        choseConnectSeparator.setMinWidth(325);
        choseConnectSeparator.setMaxWidth(325);
        choseConnectSeparator.setStyle("-fx-opacity: 40%;");

        Button choseConnexion = new Button("CONNEXION AVEC");
        GridPane.setVgrow(choseConnexion, Priority.ALWAYS);
        GridPane.setHgrow(choseConnexion, Priority.ALWAYS);
        GridPane.setValignment(choseConnexion, VPos.CENTER);
        GridPane.setHalignment(choseConnexion, HPos.CENTER);
        choseConnexion.setTranslateY(160);
        choseConnexion.setStyle("-fx-background-color: #181818; -fx-text-fill: #5e5e5e; -fx-font-size: 14px;");

        Image logoImageMojang = new Image("/images/Mojang.png");
        ImageView imageViewMojang = new ImageView(logoImageMojang);

        Image logoImageCrack = new Image("/images/Crack.png");
        ImageView imageViewCrack = new ImageView(logoImageCrack);

        Button mojangButton = new Button("Connection en Crack");
        GridPane.setVgrow(mojangButton, Priority.ALWAYS);
        GridPane.setHgrow(mojangButton, Priority.ALWAYS);
        GridPane.setValignment(mojangButton, VPos.CENTER);
        GridPane.setHalignment(mojangButton, HPos.LEFT);
        mojangButton.setTranslateX(105);
        mojangButton.setTranslateY(205);
        mojangButton.setMinWidth(190);
        mojangButton.setMinHeight(47);
        mojangButton.setFont(Font.font("Consolas", FontWeight.THIN, FontPosture.REGULAR, 14f));
        mojangButton.setStyle("-fx-background-color: #34aa2f; -fx-border-radius: 0px; -fx-background-insets: 0; -fx-font-size: 14px; -fx-text-fill: #fff");
        mojangButton.setGraphic(imageViewCrack);
        mojangButton.setOnMouseEntered(e -> this.layout.setCursor(Cursor.HAND));
        mojangButton.setOnMouseExited(e -> this.layout.setCursor(Cursor.DEFAULT));
        mojangButton.setOnMouseClicked(e -> {
            if (connectWithMojang.get()) {
                connectWithMojang.set(false);
                usernameLabel.setText("Nom d'utilisateur");
                mojangButton.setGraphic(imageViewMojang);
                mojangButton.setText("Mojang");
                passwordField.setDisable(true);
                passwordLabel.setText("");
                passwordField.setText("");
                forgotPassword.setDisable(true);
            }else {
                connectWithMojang.set(true);
                usernameLabel.setText("Adresse mail");
                mojangButton.setGraphic(imageViewCrack);
                mojangButton.setText("Connexion en Crack");
                passwordField.setDisable(false);
                passwordLabel.setText("Mot de passe");
                forgotPassword.setDisable(false);
            }
        });

        Label microsoftButton = new Label("Se connecter avec Microsoft");

        GridPane.setVgrow(microsoftButton, Priority.ALWAYS);
        GridPane.setHgrow(microsoftButton, Priority.ALWAYS);
        GridPane.setValignment(microsoftButton, VPos.BOTTOM);
        GridPane.setHalignment(microsoftButton, HPos.CENTER);
        microsoftButton.setStyle("-fx-text-fill: #69a7ed; -fx-font-size: 14px; -fx-opacity: 50%;");
        microsoftButton.setUnderline(true);
        microsoftButton.setTranslateY(-5);
        microsoftButton.setOnMouseEntered(e -> this.layout.setCursor(Cursor.HAND));
        microsoftButton.setOnMouseExited(e -> this.layout.setCursor(Cursor.DEFAULT));
        microsoftButton.setOnMouseClicked(e -> this.authenticateMS());

        mainPanel.getChildren().addAll(connectLabel, connectSeparator, usernameLabel, usernameField, usernameSeparator,
                passwordLabel, passwordField, passwordSeparator, forgotPassword, errorLabel,
                conectionButton, choseConnectSeparator, choseConnexion, mojangButton, microsoftButton);
        GridPane.setHalignment(mainPanel, HPos.CENTER);
        GridPane.setValignment(mainPanel, VPos.CENTER);

    }

    public void authenticate(String user, String password, Label error) {
        if (connectWithMojang.get()) {
            Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);

            try {
                AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, user, password, null);

                saver.set("accessToken", response.getAccessToken());
                saver.set("clientToken", response.getClientToken());
                saver.set("offline-username", "");
                saver.save();

                AuthInfos infos = new AuthInfos(
                        response.getSelectedProfile().getName(),
                        response.getAccessToken(),
                        response.getClientToken(),
                        response.getSelectedProfile().getId()
                );

                Launcher.getInstance().setAuthInfos(infos);

                this.logger.info("Hello" + " " + infos.getUsername());

                panelManager.showPanel(new App());
            }catch (AuthenticationException e) {
                error.setText("Mot de passe ou adresse mail invalide");
            }
        }else {
            AuthInfos infos = new AuthInfos(usernameField.getText(),
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString());
            saver.set("offline-username", infos.getUsername());
            saver.remove("accessToken");
            saver.remove("clientToken");
            saver.save();
            Launcher.getInstance().setAuthInfos(infos);

            this.logger.info("Hello" + " " + infos.getUsername() + " !");

            panelManager.showPanel(new App());
        }
    }

    public void authenticateMS() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {

            logger.err(error.getMessage());

            if (error != null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Erreur:\n" + "Le joueur n'a pas acheté Minecraft Java Edition ou n'a pas migré son compte",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            saver.set("msAccessToken", response.getAccessToken());
            saver.set("msRefreshToken", response.getRefreshToken());
            saver.save();

            Launcher.getInstance().setAuthInfos(new AuthInfos(
                    response.getProfile().getName(),
                    response.getAccessToken(),
                    response.getProfile().getId()
            ));
            this.logger.info("Hello " + response.getProfile().getName() + " !");

            panelManager.showPanel(new App());

        });
    }

    private void openURL(String url){
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }
}
