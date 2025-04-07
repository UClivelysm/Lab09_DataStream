import javax.swing.SwingUtilities;

public class StreamRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StreamFrame frame = new StreamFrame();
            frame.setVisible(true);
        });
    }
}
