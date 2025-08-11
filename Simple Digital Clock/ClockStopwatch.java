// ClockStopwatch.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

// main class for clock and stopwatch app
public class ClockStopwatch extends JFrame {

    private Timer clockTimer;
    private Timer stopwatchTimer;
    private long stopwatchStartTime = 0;
    private boolean stopwatchRunning = false;
    private final JButton startButton; // start/stop button
    private final JLabel stopwatchLabel;

    public ClockStopwatch() {
        // set up colors and fonts
        Color darkBlue = new Color(20, 30, 40);
        Color panelBlue = new Color(30, 45, 60);
        Color accentCyan = new Color(0, 200, 200);
        Color textColor = new Color(225, 225, 225);
        Color buttonColor = new Color(40, 60, 80);
        Font labelFont = new Font("DS-Digital", Font.BOLD, 64);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // set up window
        setTitle("Clock & Stopwatch");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(darkBlue);

        // tabs for clock and stopwatch
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(buttonFont);
        tabbedPane.setBackground(darkBlue);
        tabbedPane.setForeground(textColor);

        // clock tab
        JPanel clockPanel = new JPanel(new GridBagLayout());
        clockPanel.setBackground(panelBlue);
        JLabel clockLabel = new JLabel("00:00:00 AM");
        clockLabel.setFont(labelFont);
        clockLabel.setForeground(accentCyan);
        clockPanel.add(clockLabel);

        // stopwatch tab
        JPanel stopwatchPanel = new JPanel(new BorderLayout(10, 10));
        stopwatchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        stopwatchPanel.setBackground(panelBlue);

        stopwatchLabel = new JLabel("00:00:00.0");
        stopwatchLabel.setFont(labelFont);
        stopwatchLabel.setForeground(accentCyan);
        stopwatchLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(panelBlue);
        
        startButton = new JButton("Start");
        JButton resetButton = new JButton("Reset");

        // style buttons
        for (JButton btn : new JButton[]{startButton, resetButton}) {
            btn.setFont(buttonFont);
            btn.setBackground(buttonColor);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(10, 25, 10, 25));
        }

        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);

        stopwatchPanel.add(stopwatchLabel, BorderLayout.CENTER);
        stopwatchPanel.add(buttonPanel, BorderLayout.SOUTH);

        // add tabs
        tabbedPane.addTab("  Digital Clock  ", clockPanel);
        tabbedPane.addTab("  Stopwatch  ", stopwatchPanel);
        add(tabbedPane);

        // timer for clock
        clockTimer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
            clockLabel.setText(sdf.format(new Date()));
        });
        clockTimer.start();

        // timer for stopwatch
        stopwatchTimer = new Timer(100, e -> {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - stopwatchStartTime;
            long hours = (elapsedTime / 3600000);
            long minutes = (elapsedTime / 60000) % 60;
            long seconds = (elapsedTime / 1000) % 60;
            long tenths = (elapsedTime / 100) % 10;
            stopwatchLabel.setText(String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, tenths));
        });
        
        // start/stop button logic
        Action toggleStopwatchAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stopwatchRunning) {
                    stopwatchTimer.stop();
                    startButton.setText("Start");
                    stopwatchRunning = false;
                } else {
                    stopwatchStartTime = System.currentTimeMillis() - (stopwatchLabel.getText().equals("00:00:00.0") ? 0 : Long.parseLong(stopwatchLabel.getText().replaceAll("[^\\d]", "")));
                    stopwatchTimer.start();
                    startButton.setText("Stop");
                    stopwatchRunning = true;
                }
            }
        };

        // connect button to logic
        startButton.addActionListener(toggleStopwatchAction);

        // reset button logic
        resetButton.addActionListener(e -> {
            stopwatchTimer.stop();
            stopwatchLabel.setText("00:00:00.0");
            startButton.setText("Start");
            stopwatchRunning = false;
        });
        
        // spacebar toggles start/stop
        JPanel contentPane = (JPanel) this.getContentPane();
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "toggleStopwatch");
        contentPane.getActionMap().put("toggleStopwatch", toggleStopwatchAction);
    }

    // start the app
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClockStopwatch().setVisible(true));
    }
}