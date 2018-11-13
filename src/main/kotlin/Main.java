import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
    public JButton selectButton;
    public JTextField packageText;
    public JButton verifyCoverageFileButton;

    public JLabel resultLabel;
    public JLabel coverageFileLabel;
    public JPanel root;

    public static void main(String[] args) {
        new MainController(new Main()).run();
    }

}
