import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    static void main() {
        //second task
        GameProgress firstSave = new GameProgress(100, 0, 42, 33);
        GameProgress secondSave = new GameProgress(200, 0, 84, 66);
        GameProgress thirdSave = new GameProgress(522, 0, 239, 1000);
        List<String> pathsToSaves = Arrays.asList("save1.dat", "save2.dat", "save3.dat");
        saveGame(pathsToSaves.get(0), firstSave);
        saveGame(pathsToSaves.get(1), secondSave);
        saveGame(pathsToSaves.get(2), thirdSave);
        String zipPath = "saves.zip";
        zipFiles(zipPath, pathsToSaves);
        cleanUp(pathsToSaves);

        //third task
        openZip(zipPath, ".");
        System.out.println(openProgress("save1.dat"));
    }

    public static void saveGame(String path, GameProgress toSave) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(toSave);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String path, List<String> pathsToSave) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String savePath : pathsToSave) {
                File file = new File(savePath);
                try(FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);

                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException e) {
                    System.out.println("Problem with save file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Problem with archive creation: " + e.getMessage());
        }
    }

    public static void cleanUp(List<String> pathsToDelete) {
        for (String path : pathsToDelete) {
            File file = new File(path);
            file.delete();
        }
    }

    // third task

    public static void openZip(String pathToZip, String dirToUnzip) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathToZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                File file = new File(dirToUnzip, name);
                file.getParentFile().mkdirs();
                try(FileOutputStream fout = new FileOutputStream(file)) {
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    fout.flush();
                }
                zin.closeEntry();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String pathToFile) {
        GameProgress result = null;
        try (FileInputStream fis = new FileInputStream(pathToFile);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            result = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
