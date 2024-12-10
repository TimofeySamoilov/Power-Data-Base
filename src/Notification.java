import javax.swing.*;


public class Notification extends JFrame {

    public Notification(int systemFlag) {
        String message;
        switch (systemFlag) {
            case 0 : {
                message = "File was successfully opened";
                break;
            }
            case 1 : {
                message = "File was successfully restored from backup";
                break;
            }
            default : {
                message = "File and backup file was not found, created new!";
                break;
            }
        }
        JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE);

    }

}