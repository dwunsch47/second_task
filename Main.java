import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    static void main() {
        GameProgress firstSave = new GameProgress(100, 0, 42, 33);
        GameProgress secondSave = new GameProgress(200, 0, 84, 66);
        GameProgress thirdSave = new GameProgress(522, 0, 239, 1000);
        List<String> pathsToSaves = Arrays.asList("save1.dat", "save2.dat", "save3.dat");
        saveGame(pathsToSaves.get(0), firstSave);
        saveGame(pathsToSaves.get(1), secondSave);
        saveGame(pathsToSaves.get(2), thirdSave);
        cleanUp(pathsToSaves);
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
                try(FileInputStream fis = new FileInputStream(savePath)) {
                    int nameBegin = Math.max(savePath.lastIndexOf('\\'), savePath.lastIndexOf('/'));
                    nameBegin += (nameBegin > 0 ? 1 : 0); //account for path separator if there is one

                    ZipEntry entry = new ZipEntry(savePath.substring(nameBegin));
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

}
