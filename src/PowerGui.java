import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


public class PowerGui {

    public void startGUI(RandomAccessFile file, RandomAccessFile backup, int systemFlag) {
        Notification notification = new Notification(systemFlag);
        // Window creating
        JFrame frame = new JFrame("Power Data BASE");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(1080, 900);
        frame.setResizable(false);

        // Panel Creating
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, file, backup);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
                // Check the confirmation
                if (response == JOptionPane.YES_OPTION) {
                    makeBackup(file, backup);
                    frame.dispose();
                    try {
                        file.close();
                        backup.close();
                    }
                    catch (Exception ex) {
                        System.out.println("Error with close");
                    }
                }
                // Else do nothing
            }
        });
        // Window visibility
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel, RandomAccessFile file, RandomAccessFile backup) {
        panel.setLayout(null);
        // Some text
        JLabel userLabel = new JLabel("Set some data there >>>>>>>");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setBounds(25, 15, 250, 50);
        panel.add(userLabel);

        // For char (Primary key)
        JTextField userTextChar = new JTextField(20);
        userTextChar.setBounds(270, 25, 110, 25);
        panel.add(userTextChar);

        JLabel infoTextChar = new JLabel("CHAR");
        infoTextChar.setBounds(300, 65, 50, 10);
        panel.add(infoTextChar);

        JLabel infoLabel = new JLabel("i"); // Hint label
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoLabel.setBounds(336, 60, 10, 15);
        panel.add(infoLabel);
        infoLabel.setToolTipText("This is a Primary key, Only one symbol from Ascii table");
        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }
        });

        // For Int
        JTextField userTextInt = new JTextField(20);
        userTextInt.setBounds(390, 25, 110, 25);
        panel.add(userTextInt);

        JLabel infoTextInt = new JLabel("INTEGER");
        infoTextInt.setBounds(410, 65, 50, 10);
        panel.add(infoTextInt);

        JLabel infoLabelInt = new JLabel("i"); // Hint label
        infoLabelInt.setFont(new Font("Arial", Font.BOLD, 18));
        infoLabelInt.setBounds(462, 60, 10, 15);
        panel.add(infoLabelInt);
        infoLabelInt.setToolTipText("This is a Unsigned NOT NULL INTEGER value (<= 99999999)");
        infoLabelInt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }
        });

        // For String
        JTextField userTextString = new JTextField(20);
        userTextString.setBounds(510, 25, 110, 25);
        panel.add(userTextString);

        JLabel infoTextString = new JLabel("STRING");
        infoTextString.setBounds(535, 65, 50, 10);
        panel.add(infoTextString);

        JLabel infoLabelString = new JLabel("i"); // Hint label
        infoLabelString.setFont(new Font("Arial", Font.BOLD, 18));
        infoLabelString.setBounds(580, 60, 10, 15);
        panel.add(infoLabelString);
        infoLabelString.setToolTipText("This is a String value (Latin symbols, numbers and \".-,+=!\")");
        infoLabelString.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }
        });

        // For Bool
        JTextField userTextBool = new JTextField(20);
        userTextBool.setBounds(630, 25, 110, 25);
        panel.add(userTextBool);

        JLabel infoTextBool = new JLabel("BOOLEAN");
        infoTextBool.setBounds(650, 65, 65, 10);
        panel.add(infoTextBool);

        JLabel infoLabelBool = new JLabel("i"); // Hint label
        infoLabelBool.setFont(new Font("Arial", Font.BOLD, 18));
        infoLabelBool.setBounds(710, 60, 10, 15);
        panel.add(infoLabelBool);
        infoLabelBool.setToolTipText("This is a Bool value (0, 1) only");
        infoLabelBool.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }
        });

        // For Date
        JTextField userTextDate = new JTextField(20);
        userTextDate.setBounds(750, 25, 110, 25);
        panel.add(userTextDate);

        JLabel infoTextDate = new JLabel("DATE");
        infoTextDate.setBounds(782, 65, 45, 10);
        panel.add(infoTextDate);

        JLabel infoLabelDate = new JLabel("i"); // Hint label
        infoLabelDate.setFont(new Font("Arial", Font.BOLD, 18));
        infoLabelDate.setBounds(818, 60, 10, 15);
        panel.add(infoLabelDate);
        infoLabelDate.setToolTipText("This is a Date value format {dd.mm.yyyy}");
        infoLabelDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }
        });


        // This button appends information
        JButton loginButton = new JButton("Add!");
        loginButton.setBounds(870, 25, 140, 25);
        panel.add(loginButton);

        // This button find information by parameters
        JButton findButton = new JButton("Find");
        findButton.setBounds(870, 60, 140, 25);
        panel.add(findButton);

        // Remove button
        JButton removeByKeysButton = new JButton("Remove");
        removeByKeysButton.setBounds(870, 95, 140, 25);
        panel.add(removeByKeysButton);

        // This button shows data
        JButton showDataButton = new JButton("Show all data");
        showDataButton.setBounds(10, 730, 150, 25);
        panel.add(showDataButton);

        // Button to clear the data table
        JButton removeButton = new JButton("Remove all data");
        removeButton.setBounds(10, 760, 150, 25);
        panel.add(removeButton);

        // Backup button
        JButton backupButton = new JButton("Backup all data");
        backupButton.setBounds(10, 790, 150, 25);
        panel.add(backupButton);

        // Some visuals
        ImageIcon originalIconRect = new ImageIcon(getClass().getResource("/images/Rect.png"));

        Image scaledImageRect = originalIconRect.getImage().getScaledInstance(1100, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIconRect = new ImageIcon(scaledImageRect);

        JLabel imageLabelRect = new JLabel(scaledIconRect);
        imageLabelRect.setBounds(0, 150, scaledIconRect.getIconWidth(), scaledIconRect.getIconHeight());
        panel.add(imageLabelRect);

        Image scaledImageRect2 = originalIconRect.getImage().getScaledInstance(1100, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIconRect2 = new ImageIcon(scaledImageRect2);

        JLabel imageLabelRect2 = new JLabel(scaledIconRect2);
        imageLabelRect2.setBounds(0, 685, scaledIconRect2.getIconWidth(), scaledIconRect2.getIconHeight());
        panel.add(imageLabelRect2);

        // Logo
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/Power_Data_Base.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(1080 - 176, 900 - 198, scaledIcon.getIconWidth(), scaledIcon.getIconHeight());
        panel.add(imageLabel);

        // Appends all data in text labels
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dataChar = userTextChar.getText().trim();
                String dataInt = userTextInt.getText().trim();
                String dataString = userTextString.getText().trim();
                String dataBool = userTextBool.getText().trim();
                String dataDate = userTextDate.getText().trim();
                // Checking data and writes it if it is correct
                switch (checkInputData(dataChar, dataInt, dataString, dataBool, dataDate, file)) {
                    case (0) : {
                        JOptionPane.showMessageDialog(null, "Wrong format of char!");
                        break;
                    }
                    case (1) : {
                        JOptionPane.showMessageDialog(null, "Wrong format of Int");
                        break;
                    }
                    case (2) : {
                        JOptionPane.showMessageDialog(null, "Wrong format of String");
                        break;
                    }
                    case (3) : {
                        JOptionPane.showMessageDialog(null, "Wrong format of Bool");
                        break;
                    }
                    case (4) : {
                        JOptionPane.showMessageDialog(null, "Wrong format of Date");
                        break;
                    }
                    case (5) : {
                        JOptionPane.showMessageDialog(null, "Data was successfully added");
                        break;
                    }
                    default: {
                        int response = JOptionPane.showConfirmDialog(null,
                                "The data with this key is already exists, do you want to change it?",
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            changeData(normaliseData(dataChar, dataInt, dataString, dataBool, dataDate), file);
                            JOptionPane.showMessageDialog(null, "Data was successfully recorded");
                        }
                        break;
                    }
                }
            }
        });

        // Action removes all found data
        removeByKeysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dataChar = userTextChar.getText().trim();
                String dataInt = userTextInt.getText().trim();
                String dataString = userTextString.getText().trim();
                String dataBool = userTextBool.getText().trim();
                String dataDate = userTextDate.getText().trim();
                if (dataChar.isEmpty() && dataString.isEmpty() && dataBool.isEmpty() && dataInt.isEmpty() && dataDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No parameters was given!");
                }
                else {
                    String itemChar = "", itemInt = "", itemString = "", itemBool = "", itemData = "";
                    ArrayList<Integer> save = new ArrayList<>();
                    try {
                        byte[] buffer = new byte[52];
                        for (int i = 0; i < 256; ++i) {
                            file.seek(i * 52);
                            file.read(buffer);
                            itemChar = "" + (char)buffer[1];
                            for (int j = 3; j < 11; ++j) {
                                itemInt = itemInt + (char)buffer[j];
                            }
                            for (int j = 13; j < 37; ++j) {
                                if ((char)buffer[j] != '-')
                                    itemString = itemString + (char)buffer[j];
                            }
                            itemBool = String.valueOf((char)buffer[38]);
                            for (int j = 40; j < 50; ++j) {
                                itemData = itemData + (char)buffer[j];
                            }


                            if ((dataChar.isEmpty() || itemChar.equals(dataChar)) &&
                                    (dataInt.isEmpty() || itemInt.equals(dataInt)) &&
                                    (dataString.isEmpty() || itemString.equals(dataString)) &&
                                    (dataBool.isEmpty() || itemBool.equals(dataBool)) &&
                                    (dataDate.isEmpty() || itemData.equals(dataDate))) {
                                save.add(i);
                            }
                            itemChar = "";
                            itemInt = "";
                            itemString = "";
                            itemBool = "";
                            itemData = "";
                        }

                    }
                    catch (Exception ex) {
                        System.out.println("Failed to open file");
                    }
                    if (!save.isEmpty()) {
                        String line = "*-------------------------------------------------*\n";
                        byte[] buffer = line.getBytes();
                        for (int i = 0; i < save.size(); ++i) {
                            try {
                                file.seek(save.get(i) * 52);
                                file.write(buffer);
                            }
                            catch (Exception ex) {
                                System.out.println("Error");
                                System.exit(0);
                            }
                        }
                        JOptionPane.showMessageDialog(null, "All found data was removed");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Nothing found");
                    }
                }
            }
        });

        // This button finds data
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dataChar = userTextChar.getText().trim();
                String dataInt = userTextInt.getText().trim();
                String dataString = userTextString.getText().trim();
                String dataBool = userTextBool.getText().trim();
                String dataDate = userTextDate.getText().trim();
                if (dataChar.isEmpty() && dataString.isEmpty() && dataBool.isEmpty() && dataInt.isEmpty() && dataDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No parameters was given!");
                }
                else {
                    String itemChar = "", itemInt = "", itemString = "", itemBool = "", itemData = "";
                    StringBuilder save = new StringBuilder();
                    try {
                        byte[] buffer = new byte[52];
                        for (int i = 0; i < 256; ++i) {
                            file.seek(i * 52);
                            file.read(buffer);
                            itemChar = "" + (char)buffer[1];
                            for (int j = 3; j < 11; ++j) {
                                itemInt = itemInt + (char)buffer[j];
                            }
                            for (int j = 13; j < 37; ++j) {
                                if ((char)buffer[j] != '-')
                                    itemString = itemString + (char)buffer[j];
                            }
                            itemBool = String.valueOf((char)buffer[38]);
                            for (int j = 40; j < 50; ++j) {
                                itemData = itemData + (char)buffer[j];
                            }


                            if ((dataChar.isEmpty() || itemChar.equals(dataChar)) &&
                                    (dataInt.isEmpty() || itemInt.equals(dataInt)) &&
                                    (dataString.isEmpty() || itemString.equals(dataString)) &&
                                    (dataBool.isEmpty() || itemBool.equals(dataBool)) &&
                                    (dataDate.isEmpty() || itemData.equals(dataDate))) {
                                save.append(itemChar + "   " + itemInt + "   " + itemString + "   " + itemBool + "   " + itemData + "\n");
                            }
                            itemChar = "";
                            itemInt = "";
                            itemString = "";
                            itemBool = "";
                            itemData = "";
                        }

                    }
                    catch (Exception ex) {
                        System.out.println("Failed to open file");
                    }
                    if (!save.isEmpty())
                        JOptionPane.showMessageDialog(null, save.toString());
                    else
                        JOptionPane.showMessageDialog(null, "Nothing found");
                }
            }
        });

        // Removes all data
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to continue?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "All of data were successfully removed");
                    removeData(file);
                } else {
                    JOptionPane.showMessageDialog(null, "You declined the removing");
                }
            }
        });

        // Shows all data in base
        showDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String show = getNotNullData(file);
                String message;
                if (show.isEmpty()) {
                    message = "There is no data :(";
                    JOptionPane.showMessageDialog(null, message, "All Data", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    message = show;
                    JOptionPane.showMessageDialog(null, "Id          Quantity                                   Name" +
                            "                             Stock          LastUpdate\n" + message, "All Data", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Record all data in the backup file
        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeBackup(file, backup);
                JOptionPane.showMessageDialog(null, "Data was successfully added to backup file", "Backup", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // Main checking function
    private int checkInputData(String dataChar, String dataInt, String dataString, String dataBool, String dataDate, RandomAccessFile file) {
        // We need to check primary key firstly
        if (dataChar.length() != 1) {
            return 0;
        }
        if (dataChar.charAt(0) == '-' || dataChar.charAt(0) == '_') {
            return 0;
        }
        if (dataInt.isEmpty()) {
            return 1;
        }
        // INT CHECK
        String intAlf = "1234567890";
        if (dataInt.length() > 8) {
            return 1;
        }
        for (int i = 0; i < dataInt.length(); ++i) {
            boolean flag = false;
            for (int j = 0; j < intAlf.length(); ++j) {
                if (dataInt.charAt(i) == intAlf.charAt(j)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return 1;
            }
        }

        // STRING CHECK
        if (dataString.length() > 25) {
            return 2;
        }
        String stringAlf = " .1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM-,+=!";
        for (int i = 0; i < dataString.length(); ++i) {
            boolean flag = false;
            for (int j = 0; j < stringAlf.length(); ++j) {
                if (dataString.charAt(i) == stringAlf.charAt(j)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return 2;
            }
        }

        // BOOL CHECK
        if (dataBool.length() > 1) {
            return 3;
        }
        String boolAlf = "01";
        for (int i = 0; i < dataBool.length(); ++i) {
            boolean flag = false;
            for (int j = 0; j < boolAlf.length(); ++j) {
                if (dataBool.charAt(i) == boolAlf.charAt(j)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return 3;
            }
        }

        // DATE CHECK
        if (dataDate.length() > 10) {
            return 4;
        }
        String dateAlf = ".01234567890";
        for (int i = 0; i < dataDate.length(); ++i) {
            boolean flag = false;
            for (int j = 0; j < dateAlf.length(); ++j) {
                if (dataDate.charAt(i) == dateAlf.charAt(j)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return 4;
            }
        }

        // Trying to cast to types
        if (!dataInt.isEmpty()) {
            try {
                int numberInt = Integer.parseInt(dataInt);
            }
            catch (Exception e) {
                return 1;
            }
        }

        if (!dataBool.isEmpty()) {
            try {
                boolean numberBool = Boolean.parseBoolean(dataBool);
            }
            catch (Exception e) {
                return 3;
            }
        }
        if (!dataDate.isEmpty()) {
            String format = "dd.MM.yyyy";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            try {
                LocalDate date = LocalDate.parse(dataDate, formatter);
            } catch (DateTimeParseException e) {
                return 4;
            }
        }


        byte[] buffer = new byte[52];
        try {
            file.seek(returnPosition(dataChar.charAt(0)));
            file.read(buffer);
            file.seek(returnPosition(dataChar.charAt(0)));

            if ((char)buffer[1] == '-') {
                // It means that there is no recording with this key
                // We can write our new data

                file.write(normaliseData(dataChar, dataInt, dataString, dataBool, dataDate));
                return 5;
            }
        }
        catch (IOException e) {
            System.out.println("-!- Error" + e);
        }

        // If recording is already exists in the table
        return 6;
    }

    private int returnPosition(char primaryKey) {
        return 52 * primaryKey;
    }

    // This function normalise data
    private byte[] normaliseData(String dataChar, String dataInt, String dataString, String dataBool, String dataDate) {
        if (!dataInt.isEmpty()) {
            while (dataInt.length() < 8) {
                dataInt = "0" + dataInt;
            }
        }
        else {
            dataInt = "--------";
        }
        if (!dataString.isEmpty()) {
            while (dataString.length() < 25) {
                dataString = "-" + dataString;
            }
        }
        else {
            dataString = "-------------------------";
        }
        if (dataBool.isEmpty()) {
            dataBool = "-";
        }
        if (dataDate.isEmpty()) {
            dataDate = "----------";
        }
        return ("*" + dataChar + "|" + dataInt + "|" + dataString + "|" + dataBool + "|" + dataDate + "*").getBytes();
    }

    // Used for show all data
    private String getNotNullData(RandomAccessFile file) {
        StringBuilder ans = new StringBuilder();
        byte[] buffer = new byte[52];
        for (int i = 0; i < 256; ++i) {
            try {
                file.seek(i * 52);
                file.read(buffer);
                if (buffer[1] != '-') {
                    for (int b = 1; b < buffer.length - 2; ++b) {
                        if ((char)buffer[b] == '|')
                            ans.append("           ");
                        else if ((char)buffer[b] == '-') {
                            ans.append("_");
                        }
                        else
                            ans.append((char)buffer[b]);
                    }
                    ans.append('\n');
                }
            }
            catch (Exception e) {
                System.out.println("-!- Error" + e);
            }

        }
        return ans.toString();
    }

    // Removes all data in base
    private void removeData(RandomAccessFile file) {
        try {
            file.seek(0);
            String line = "*-------------------------------------------------*\n";
            byte[] buffer = line.getBytes();
            for (int i = 0; i < 256; ++i) {
                file.write(buffer);
            }
        }
        catch (Exception e) {
            System.out.println("-!- Error" + e);
        }
    }

    // For backup button
    private void makeBackup(RandomAccessFile file, RandomAccessFile backup) {
        try {
            Path source = Path.of("file.txt");
            Path destination = Path.of("backup.txt");
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
        }
    }

    // For change data button
    private void changeData(byte[] normalised_data, RandomAccessFile file) {
        try {
            file.seek(returnPosition((char)normalised_data[1]));
            file.write(normalised_data);
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }
}
