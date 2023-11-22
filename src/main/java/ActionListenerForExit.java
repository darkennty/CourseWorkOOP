import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerForExit implements ActionListener {

    public ActionListenerForExit() {}

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
