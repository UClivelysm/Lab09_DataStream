import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilePicker {
    public static File GetFile() {
        File selectedFile = null;
        try {
            // Create a JFileChooser instance
            JFileChooser chooser = new JFileChooser();
            // Open the file chooser dialog; null centers it on the screen
            int result = chooser.showOpenDialog(null);

            // Check if the user selected a file
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
            } else {
                // Optionally, handle the case where the user canceled the operation
                System.out.println("No file was selected.");
            }
        }
        // Catch for security-related exceptions, e.g., when access to file system is denied
        catch (SecurityException se) {
            System.err.println("Security exception encountered: " + se.getMessage());
            se.printStackTrace();
        }
        // General catch-all for any other exceptions that might occur
        catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return selectedFile;
    }

    public static String SimpleStreamRead(File inputFile) {
        try (Stream<String> lines = Files.lines(inputFile.toPath())) {
            return lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error in Reading";
        }
    }

    public static String SearchedStreamRead(File inputFile, String searchTerm) {
        try (Stream<String> lines = Files.lines(inputFile.toPath())) {
            return lines.filter(line -> line.contains(searchTerm))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error in Reading";
        }
    }




}
