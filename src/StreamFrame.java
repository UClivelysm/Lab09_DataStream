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

    JPanel centerSouthPanel;
    JPanel centerFilterBtnPanel;
    JPanel centerFileBtnPanel;

    JLabel placeholderLabel;
    JLabel inputFileNameLabel;
    JLabel filterFileNameLabel;
    JLabel minCountLabel;

    JButton goButton;
    JButton quitButton;

    JButton addFilterButton;
    JButton removeFilterButton;
    JButton addInputFileButton;
    JButton removeInputFileButton;
    JButton addOutputFileButton;

    JTextArea textArea;
    JScrollPane scrollPane;

    JTextField minFrequencyTF;

    File filterFile = null;
    File inputFile = null;
    String returnedString = "";


    public StreamFrame() {
        super("Extractor App");

        setLayout(new BorderLayout());

        // Add panels to their respective positions
        add(createNorthPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
    }

    private JPanel createNorthPanel() {
        northPanel = new JPanel();
        placeholderLabel = new JLabel("Tag Extractor Version 0.0.1", SwingConstants.CENTER);
        northPanel.add(placeholderLabel);
        return northPanel;
    }

    private JPanel createCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        inputFileNameLabel = new JLabel("Input File: No File Selected", SwingConstants.CENTER);
        filterFileNameLabel = new JLabel("Filter File: No File Selected", SwingConstants.CENTER);

        centerNorthPanel = new JPanel(new GridLayout(2, 1));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        scrollPane = new JScrollPane(textArea);

        centerFilterBtnPanel = new JPanel();
        centerFilterBtnPanel.setLayout(new GridLayout(2, 1));
        addFilterButton = new JButton("Add Filter");
        addFilterButton.addActionListener(e -> {
            System.out.println("Add Filter");
            filterFile = FilePicker.GetFile();
            if (filterFile != null) {
                System.out.println("Filter File: " + filterFile.getAbsolutePath());
                filterFileNameLabel.setText("Filter File: " + filterFile.getAbsolutePath());
            } else {
                System.out.println("Filter File: No File Selected");
            }
        });
        removeFilterButton = new JButton("Remove Filter");
        removeFilterButton.addActionListener(e -> {
            System.out.println("Remove Filter");
            filterFile = null;
            filterFileNameLabel.setText("Filter File: No File Selected");
        });
        centerFilterBtnPanel.add(addFilterButton);
        centerFilterBtnPanel.add(removeFilterButton);

        centerFileBtnPanel = new JPanel();
        centerFileBtnPanel.setLayout(new GridLayout(2, 1));
        addInputFileButton = new JButton("Add Input File");
        addInputFileButton.addActionListener(e -> {
            System.out.println("Add Input File");
            inputFile = FilePicker.GetFile();
            if (inputFile != null) {
                System.out.println("Input File: " + inputFile.getAbsolutePath());
                inputFileNameLabel.setText("Input File: " + inputFile.getAbsolutePath());
            } else {
                System.out.println("Input File: No File Selected");
            }
        });
        removeInputFileButton = new JButton("Remove Input File");
        removeInputFileButton.addActionListener(e -> {
            System.out.println("Remove Input File");
            inputFile = null;
            inputFileNameLabel.setText("Input File: No File Selected");
        });
        centerFileBtnPanel.add(addInputFileButton);
        centerFileBtnPanel.add(removeInputFileButton);

        centerSouthPanel = new JPanel();
        centerSouthPanel.setLayout(new GridLayout(1, 2));
        centerSouthPanel.add(centerFileBtnPanel);
        centerSouthPanel.add(centerFilterBtnPanel);

        centerNorthPanel.add(inputFileNameLabel);
        centerNorthPanel.add(filterFileNameLabel);

        centerPanel.add(centerSouthPanel, BorderLayout.SOUTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(centerNorthPanel, BorderLayout.NORTH);
        return centerPanel;
    }

    private JPanel createSouthPanel() {
        southPanel = new JPanel(new GridLayout(1, 2));

        goButton = new JButton("Start");
        quitButton = new JButton("Quit");
        addOutputFileButton = new JButton("Output to File");
        minCountLabel = new JLabel("Enter the minimum word count:");
        minFrequencyTF = new JTextField(4);
        minFrequencyTF.setText("3");

        southButtonsPanel = new JPanel(new GridLayout(1, 3));
        southInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Add lambda-based action listeners
        goButton.addActionListener(e -> {
            if (filterFile == null && inputFile != null) {
                int minCount;
                try {
                    // Parse the input value from the text field
                    minCount = Integer.parseInt(minFrequencyTF.getText().trim());
                    // Validate the integer is in the allowed range 1-9999
                    if (minCount < 1 || minCount > 9999) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    // Show a dialog box if the input is not a valid int or not in range
                    JOptionPane.showMessageDialog(null, "You must enter a valid int 1-9999", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the action listener if input is invalid
                }
                returnedString = FilePicker.GenerateMap(inputFile, minCount);
                textArea.setText(returnedString);
            } else if (filterFile != null && inputFile != null) {
                int minCount;
                try {
                    // Parse the input value from the text field
                    minCount = Integer.parseInt(minFrequencyTF.getText().trim());
                    // Validate the integer is in the allowed range 1-9999
                    if (minCount < 1 || minCount > 9999) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    // Show a dialog box if the input is not a valid int or not in range
                    JOptionPane.showMessageDialog(null, "You must enter a valid int 1-9999", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the action listener if input is invalid
                }
                returnedString = FilePicker.GenerateFilteredMap(inputFile, filterFile,minCount);
                textArea.setText(returnedString);
            } else {
                JOptionPane.showMessageDialog(null, "You must have a input file and optionally a filter file", "No Input File Selected", JOptionPane.ERROR_MESSAGE);
            }
        });

        addOutputFileButton.addActionListener(e -> {
            if (returnedString != null && !returnedString.trim().isEmpty()) {
                FilePicker.FileWriter(returnedString);
            } else {
                JOptionPane.showMessageDialog(null, "There is nothing to write", "Nothing to Write", JOptionPane.ERROR_MESSAGE);
            }
        });

        quitButton.addActionListener(e -> System.exit(0));

        // Add the text field so the user can enter the min frequency
        southInputPanel.add(minCountLabel);
        southInputPanel.add(minFrequencyTF);
        southButtonsPanel.add(goButton);
        southButtonsPanel.add(addOutputFileButton);
        southButtonsPanel.add(quitButton);

        southPanel.add(southInputPanel);
        southPanel.add(southButtonsPanel);

        return southPanel;
    }

}
