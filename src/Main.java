import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

public class Main {

    private static final int MAX_COUNT_OF_KEYS = 256;

    public static void main(String[] args) {
        fileWork();
    }

    private static void fileWork() {
        PowerGui gui = new PowerGui();
        File fileCheck = new File("file.txt");
        File backupCheck = new File("backup.txt");
        RandomAccessFile file;
        RandomAccessFile backupFile;
        if (fileCheck.exists()) {
            try {
                file = new RandomAccessFile("file.txt", "rw");
                backupFile = new RandomAccessFile("file.txt", "rw");
                gui.startGUI(file, backupFile, 0);
            }
            catch (Exception e) {
                System.out.println("Failed to open file");
            }
        }
        else if (backupCheck.exists()) {
            try {
                file = new RandomAccessFile("file.txt", "rw");
                backupFile = new RandomAccessFile("backup.txt", "r");
                cloneDataFromBackup(file, backupFile);
                backupFile = new RandomAccessFile("backup.txt", "rw");
                gui.startGUI(file, backupFile, 1);
            }
            catch (Exception e) {
                System.out.println("Failed to open file");
            }
        }
        else {
            try {
                file = new RandomAccessFile("file.txt", "rw");
                backupFile = new RandomAccessFile("backup.txt", "rw");
                for (int i = 0; i < MAX_COUNT_OF_KEYS; ++i) {
                    file.writeBytes("*-------------------------------------------------*\n");
                }
                for (int i = 0; i < MAX_COUNT_OF_KEYS; ++i) {
                    backupFile.writeBytes("*-------------------------------------------------*\n");
                }

                gui.startGUI(file, backupFile, 2);

            }
            catch (Exception e) {
                System.out.println("Failed to open file");
            }
        }
    }

    private static void cloneDataFromBackup(RandomAccessFile file, RandomAccessFile backup) {
        try {
            backup.seek(0);
            file.seek(0);
            byte[] buffer = new byte[52000];
            int bytesRead;

            while ((bytesRead = backup.read(buffer)) != -1) {
                file.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            System.out.println("-!- Error clone" + e);
        }
    }
}

