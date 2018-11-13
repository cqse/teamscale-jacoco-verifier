import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainForm {
    public JButton selectButton;
    public JTextField packageText;
    public JButton verifyCoverageFileButton;

    public JLabel resultLabel;
    public JLabel coverageFileLabel;
    public JPanel root;

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:noGrow",
                "center:d:noGrow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow," +
                        "top:4dlu:noGrow,center:max(d;4px):grow"));
        root.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16), null));
        coverageFileLabel = new JLabel();
        coverageFileLabel.setText("(please select)");
        CellConstraints cc = new CellConstraints();
        root.add(coverageFileLabel, cc.xy(3, 1));
        final JLabel label1 = new JLabel();
        label1.setText("Your Application's Package:");
        root.add(label1, cc.xy(1, 3));
        final JLabel label2 = new JLabel();
        label2.setText("Coverage File:");
        root.add(label2, cc.xy(1, 1));
        selectButton = new JButton();
        selectButton.setText("Select...");
        root.add(selectButton, cc.xy(5, 1));
        packageText = new JTextField();
        packageText.setText("com.yourcompany");
        packageText.setToolTipText("e.g: com.yourcompany");
        root.add(packageText, cc.xyw(3, 3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        root.add(panel1, cc.xyw(1, 5, 5));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1,
                        GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        verifyCoverageFileButton = new JButton();
        verifyCoverageFileButton.setText("Verify Coverage File");
        panel1.add(verifyCoverageFileButton,
                new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2,
                new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        resultLabel = new JLabel();
        resultLabel.setText("Result");
        root.add(resultLabel, cc.xyw(1, 7, 5, CellConstraints.DEFAULT, CellConstraints.TOP));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }
}
