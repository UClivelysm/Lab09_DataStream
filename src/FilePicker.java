import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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

    public static String GenerateMap(File inputFile, int minCount) {
        // Check if input file exists
        if (!inputFile.exists()) {
            return "File not found.";
        }

        Map<String, Integer> wordFrequency = new HashMap<>();

        // Read the input file and build the word frequency map.
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split on non-word characters.
                String[] words = line.split("[^A-Za-z]+");
                for (String word : words) {
                    // Only process words with at least 3 characters.
                    if (word.length() < 3) continue;
                    String processedWord = word.toLowerCase();
                    wordFrequency.put(processedWord, wordFrequency.getOrDefault(processedWord, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file.";
        }

        return getFormattedString(wordFrequency, minCount);
    }

    // Static method that builds a filtered word frequency map.
    // It excludes any words found in filterFile from being counted.
    public static String GenerateFilteredMap(File inputFile, File filterFile, int minCount) {
        // Check if the input file exists.
        if (!inputFile.exists()) {
            return "Input file not found.";
        }
        // Check if the filter file exists.
        if (!filterFile.exists()) {
            return "Filter file not found.";
        }

        // Load the filter words into an ArrayList.
        List<String> filterWords = new ArrayList<>();
        try (BufferedReader filterReader = new BufferedReader(new FileReader(filterFile))) {
            String filterLine;
            while ((filterLine = filterReader.readLine()) != null) {
                if (!filterLine.trim().isEmpty()) {
                    filterWords.add(filterLine.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading filter file.";
        }

        // Build the word frequency map from the input file.
        Map<String, Integer> wordFrequency = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("[^A-Za-z]+");
                for (String word : words) {
                    // Only process words with at least 3 characters.
                    if (word.length() < 3) continue;
                    String processedWord = word.toLowerCase();
                    // Skip words that are in the filter list.
                    if (filterWords.contains(processedWord)) {
                        continue;
                    }
                    wordFrequency.put(processedWord, wordFrequency.getOrDefault(processedWord, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading input file.";
        }

        return getFormattedString(wordFrequency, minCount);
    }

    // Private helper method to format the word frequency map into a string.
    // Only words with a frequency of at least minCount are included.
    // Each word is truncated to 30 characters and counts are formatted in a 4-digit field.
    private static String getFormattedString(Map<String, Integer> wordFrequency, int minCount) {
        StringBuilder formattedOutput = new StringBuilder();
        List<String> wordsList = new ArrayList<>(wordFrequency.keySet());
        Collections.sort(wordsList);

        for (String word : wordsList) {
            int count = wordFrequency.get(word);
            if (count < minCount) {
                continue;
            }
            String truncatedWord = word.length() > 30 ? word.substring(0, 30) : word;
            formattedOutput.append(String.format("%-30.30s: %4d%n", truncatedWord, count));
        }

        return formattedOutput.toString();
    }

    public static void FileWriter(String input) {
        // Get the current working directory and create a File object for OutputFile.txt
        String workingDir = System.getProperty("user.dir");
        File outputFile = new File(workingDir, "OutputFile.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(input);
            // Pop-up message showing the file was written along with its full path.
            JOptionPane.showMessageDialog(null,
                    "File was written successfully to:\n" + outputFile.getAbsolutePath(),
                    "File Written",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error writing to file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
