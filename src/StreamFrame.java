import javax.swing.*;
import java.awt.*;
import java.io.File;

public class StreamFrame extends JFrame {

    JPanel northPanel;
    JPanel centerPanel;
    JPanel southPanel;

    JPanel centerNorthPanel;

    JPanel southButtonsPanel;
    JPanel southInputPanel;

    JPanel centerTAPanel;
    JPanel centerFileBtnPanel;

    JLabel placeholderLabel;
    JLabel inputFileNameLabel;
    JLabel minCountLabel;

    JButton goButton;
    JButton quitButton;

    JButton addInputFileButton;
    JButton removeInputFileButton;
    JButton addOutputFileButton;

    JTextArea rawTextArea;
    JScrollPane scrollPane1;

    JTextArea filteredTextArea;
    JScrollPane scrollPane2;

    JTextField searchTermTF;

    File inputFile = null;
    String rawString = "";
    String filteredString = "";


    public StreamFrame() {
        super("File Stream Searcher");

        setLayout(new BorderLayout());

        // Add panels to their respective positions
        add(createNorthPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null); // Center on screen
    }

    private JPanel createNorthPanel() {
        northPanel = new JPanel();
        placeholderLabel = new JLabel("File Stream Searcher Version 0.0.1", SwingConstants.CENTER);
        northPanel.add(placeholderLabel);
        return northPanel;
    }

    private JPanel createCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        inputFileNameLabel = new JLabel("Input File: No File Selected", SwingConstants.CENTER);

        centerNorthPanel = new JPanel(new GridLayout(2, 1));

        rawTextArea = new JTextArea();
        rawTextArea.setEditable(false);
        rawTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        scrollPane1 = new JScrollPane(rawTextArea);

        filteredTextArea = new JTextArea();
        filteredTextArea.setEditable(false);
        filteredTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        scrollPane2 = new JScrollPane(filteredTextArea);

        centerTAPanel = new JPanel(new GridLayout(1, 2));
        centerTAPanel.add(scrollPane1);
        centerTAPanel.add(scrollPane2);



        centerFileBtnPanel = new JPanel();
        centerFileBtnPanel.setLayout(new GridLayout(2, 1));
        addInputFileButton = new JButton("Add Input File");
        addInputFileButton.addActionListener(e -> {
            System.out.println("Add Input File");
            inputFile = FilePicker.GetFile();
            if (inputFile != null) {
                System.out.println("Input File: " + inputFile.getAbsolutePath());
                inputFileNameLabel.setText("Input File: " + inputFile.getAbsolutePath());
                rawString = FilePicker.SimpleStreamRead(inputFile);
                rawTextArea.setText(rawString);
            } else {
                System.out.println("Input File: No File Selected");
            }
        });
        removeInputFileButton = new JButton("Remove Input File");
        removeInputFileButton.addActionListener(e -> {
            System.out.println("Remove Input File");
            inputFile = null;
            inputFileNameLabel.setText("Input File: No File Selected");
            rawTextArea.setText("");
            filteredTextArea.setText("");
        });
        centerFileBtnPanel.add(addInputFileButton);
        centerFileBtnPanel.add(removeInputFileButton);


        centerNorthPanel.add(inputFileNameLabel);

        centerPanel.add(centerFileBtnPanel, BorderLayout.SOUTH);
        centerPanel.add(centerTAPanel, BorderLayout.CENTER);
        centerPanel.add(centerNorthPanel, BorderLayout.NORTH);
        return centerPanel;
    }

    private JPanel createSouthPanel() {
        southPanel = new JPanel(new GridLayout(1, 2));

        goButton = new JButton("Search");
        quitButton = new JButton("Quit");
        addOutputFileButton = new JButton("Output to File");
        minCountLabel = new JLabel("Search Term:");
        searchTermTF = new JTextField(35);
        searchTermTF.setText("Hello World!");

        southButtonsPanel = new JPanel(new GridLayout(1, 2));
        southInputPanel = new JPanel(new GridLayout(2, 1));


        goButton.addActionListener(e -> {
            if (inputFile == null) {
                JOptionPane.showMessageDialog(null, "No file selected.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Retrieve and trim the search term from the text field
            String searchTerm = searchTermTF.getText().trim();

            // Validate that the search term is not empty
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Search term cannot be empty.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate that the search term has a length of 35 characters or fewer
            if (searchTerm.length() > 35) {
                JOptionPane.showMessageDialog(null, "Search term must be 35 characters or fewer.", "Invalid Search Term", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call the method to process the search term (to be implemented)
            if (inputFile != null) {
                filteredString = FilePicker.SearchedStreamRead(inputFile, searchTerm);
                filteredTextArea.setText(filteredString);
            } else {
                JOptionPane.showMessageDialog(null, "No file selected.", "Invalid File", JOptionPane.ERROR_MESSAGE);
            }

        });

        quitButton.addActionListener(e -> System.exit(0));


        // Add the text field so the user can enter the min frequency
        southInputPanel.add(minCountLabel);
        southInputPanel.add(searchTermTF);
        southButtonsPanel.add(goButton);
        southButtonsPanel.add(quitButton);

        southPanel.add(southInputPanel);
        southPanel.add(southButtonsPanel);

        return southPanel;
    }

}
