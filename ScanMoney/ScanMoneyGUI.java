import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ScanMoneyGUI extends JFrame {

    private JTextField creditsField;
    private JTextField scanValueField;
    private JTextField payoutAmountField;
    private JCheckBox firstFootfallCheckbox;
    private JTextArea outputArea;

    public ScanMoneyGUI() {
        setTitle("Scan Money Calculator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel creditsLabel = new JLabel("Current Credits:");
        JLabel scanValueLabel = new JLabel("Scan Value:");
        JLabel payoutAmountLabel = new JLabel("Organic Scans Value:");
        JLabel firstFootfallLabel = new JLabel("First Footfall:");

        creditsField = new JTextField();
        scanValueField = new JTextField();
        payoutAmountField = new JTextField();
        firstFootfallCheckbox = new JCheckBox();
        firstFootfallCheckbox.setSelected(true);

        inputPanel.add(creditsLabel);
        inputPanel.add(creditsField);
        inputPanel.add(scanValueLabel);
        inputPanel.add(scanValueField);
        inputPanel.add(payoutAmountLabel);
        inputPanel.add(payoutAmountField);
        inputPanel.add(firstFootfallLabel);
        inputPanel.add(firstFootfallCheckbox);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
            }

            private void calculate() {
                try {
                    long currentCredits = Long.parseLong(creditsField.getText().replaceAll(",", ""));
                    long scanValue = Long.parseLong(scanValueField.getText().replaceAll(",", ""));
                    long payoutAmount = Long.parseLong(payoutAmountField.getText().replaceAll(",", ""));
                    boolean isFirstFootfall = firstFootfallCheckbox.isSelected();

                    long payoutAmountMultiplier = isFirstFootfall ? payoutAmount * 5 : payoutAmount;
                    long newCreditsFF = currentCredits + payoutAmountMultiplier + scanValue;

                    NumberFormat formatter = new DecimalFormat("#,###");

                    outputArea.append("Current Credits: " + formatter.format(currentCredits) + "\n");
                    outputArea.append("-------------------------------------\n");
                    outputArea.append(
                            "Total Scan Earnings: " + formatter.format(payoutAmountMultiplier + scanValue) + "\n");
                    outputArea.append("\tBiological Scan Earnings: " + formatter.format(payoutAmountMultiplier) + "\n");
                    outputArea.append("\tPlanetary Scan Earnings: " + formatter.format(scanValue) + "\n");
                    outputArea.append("-------------------------------------\n");
                    outputArea.append("New Credit Total: " + formatter.format(newCreditsFF) + "\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ScanMoneyGUI.this, "Please enter valid numbers in all fields.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ScanMoneyGUI().setVisible(true);
            }
        });
    }
}
