package fr.flopenia.launcher.ui.panel;

import fr.flopenia.launcher.Launcher;
import fr.flopenia.launcher.PanelManager;
import fr.flowarg.flowlogger.ILogger;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public abstract class Panel implements IPanel, IMovable, ITakePlace {
    protected ILogger logger;
    protected GridPane layout = new GridPane();
    protected PanelManager panelManager;

    public Panel() {
        this.logger = Launcher.getInstance().getLogger();
    }

    @Override
    public void init(PanelManager panelManager) {
        this.panelManager = panelManager;
        setCanTakeAllSize(this.layout);
    }

    @Override
    public GridPane getLayout() {
        return layout;
    }

    public void OnShow() {
        FadeTransition transition = new FadeTransition(Duration.seconds(1), this.layout);
        transition.setFromValue(0);
        transition.setFromValue(1);
        transition.setAutoReverse(true);
        transition.play();
    }

    @Override
    public abstract String getName();

    @Override
    public void setLeft(Node node) {

    }
}
