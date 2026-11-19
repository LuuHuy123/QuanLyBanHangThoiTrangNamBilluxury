import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import GUI.LoginFrame;

public class Main {

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {

            System.err.println(
                    "Không thể thiết lập giao diện hệ điều hành.");

            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame = new LoginFrame();

            loginFrame.setLocationRelativeTo(null);

            loginFrame.setVisible(true);
        });
    }
}