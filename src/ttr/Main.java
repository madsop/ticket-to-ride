package ttr;

import ttr.europe.Europe;
import ttr.gui.GUI;
import ttr.nordic.Nordic;

import javax.swing.*;
import java.rmi.RemoteException;

public class Main {
	private static JFrame frame;
	public static JFrame getFrame() {
		return frame;
	}
	public static void setFrame(JFrame frame) {
		Main.frame = frame;
	}

	public static SpelUtgaave[] spela = new SpelUtgaave[2];
	public static void main(String args[]) throws RemoteException {
		frame = new JFrame("Ticket to ride");
		String arg;
		if (args.length < 1) { arg = "localhost"; } // If there are no arguments passed, we choose localhost as default.
		else { arg = args[0]; }
		String valstring = "Vel Ticket to ride-utgåve";
		spela[0] = new Nordic();
		spela[1] = new Europe();
		int spel = JOptionPane.showOptionDialog(frame, valstring, valstring, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, spela, spela[0]);
		if (spel >= 0 && spel < spela.length){
			GUI gui = new GUI(frame,arg,spela[spel]);	
			frame.setTitle(frame.getTitle() + " - " +spela[spel].getTittel());
			frame.setContentPane(gui);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		else {
			System.exit(0);
		}
	}
}