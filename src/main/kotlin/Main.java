import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
    private JButton selectButton;
    private JTextField packageText;
    private JButton verifyCoverageFileButton;

    private JLabel resultLabel;
    private JLabel coverageFileLabel;
    private JPanel root;

    private void launch() {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main().launch();
    }

}
