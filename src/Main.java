import config.AppConfig;
import ui.swing.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();

        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame(config);
            frame.setVisible(true);
        });
    }
}
