package ttr.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SwingUtils {
    public static void createJFrame(String title, JPanel panel) {
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setBackground(Color.DARK_GRAY);
		frame.setTitle(title);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    public static String showInputDialog(String string) { return JOptionPane.showInputDialog(null, string); }
}
