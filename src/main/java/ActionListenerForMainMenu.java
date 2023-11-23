import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerForMainMenu implements ActionListener {

    private CardLayout cardLayout;
    private Container container;
    private String panelNameToSwitchTo;
    public ActionListenerForMainMenu(CardLayout cardLayout, Container container, String panelNameToSwitchTo) {
        this.cardLayout = cardLayout;
        this.container = container;
        this.panelNameToSwitchTo = panelNameToSwitchTo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cardLayout.show(container, "menu");
    }
}
