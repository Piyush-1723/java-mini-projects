import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.security.Key;
import java.util.Arrays;

// This is the main class for the file encryptor app
public class FileEncryptor extends JFrame {

    public FileEncryptor() {
        // set up colors for the app
        Color darkGray = new Color(45, 45, 45);
        Color lightGray = new Color(70, 70, 70);
        Color textColor = new Color(220, 220, 220);

        // set up the main window
        setTitle("Secure File Encryptor");
        setSize(450, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Use BorderLayout for better structure
        setLayout(new BorderLayout());
        getContentPane().setBackground(darkGray);

        // title at the top
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(darkGray);
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Secure File Encryptor");
        titleLabel.setForeground(textColor);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // input panel for the key
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(darkGray);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel keyLabel = new JLabel("Secret Key:");
        keyLabel.setForeground(textColor);
        
        JTextField keyField = new JTextField(16);
        keyField.setBackground(lightGray);
        keyField.setForeground(textColor);
        keyField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        keyField.setCaretColor(textColor); // Blinking cursor color
        keyField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(lightGray),
            new EmptyBorder(5, 5, 5, 5) // Inner padding
        ));

        inputPanel.add(keyLabel);
        inputPanel.add(keyField);
        add(inputPanel, BorderLayout.CENTER);

        // buttons for encrypt and decrypt
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(darkGray);
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        
        JButton encryptButton = new JButton("Encrypt File");
        JButton decryptButton = new JButton("Decrypt File");
        JFileChooser fileChooser = new JFileChooser();
        
        // style buttons
        for (JButton btn : new JButton[]{encryptButton, decryptButton}) {
            btn.setBackground(lightGray);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        }

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // what happens when you click encrypt
        encryptButton.addActionListener(e -> {
            if (keyField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a key.");
                return;
            }
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                processFile(Cipher.ENCRYPT_MODE, keyField.getText(), fileChooser.getSelectedFile());
            }
        });

        // what happens when you click decrypt
        decryptButton.addActionListener(e -> {
            if (keyField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a key.");
                return;
            }
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                processFile(Cipher.DECRYPT_MODE, keyField.getText(), fileChooser.getSelectedFile());
            }
        });
    }

    // does the actual encrypt or decrypt work
    private void processFile(int cipherMode, String key, File inputFile) {
        try {
            byte[] keyBytes = Arrays.copyOf(key.getBytes("UTF-8"), 16);
            Key secretKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);
            byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
            byte[] outputBytes = cipher.doFinal(inputBytes);
            String outputFileName = (cipherMode == Cipher.ENCRYPT_MODE)
                    ? inputFile.getAbsolutePath() + ".enc"
                    : inputFile.getAbsolutePath().replaceFirst("\\.enc$", ".dec");
            File outputFile = new File(outputFileName);
            Files.write(outputFile.toPath(), outputBytes);
            JOptionPane.showMessageDialog(this, "Success! File saved to " + outputFile.getName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // start the app
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileEncryptor().setVisible(true));
    }
}