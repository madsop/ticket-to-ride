package ttr.copy;

import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ttr.europe.Europe;
import ttr.nordic.Nordic;

/*
 * Status for utveklinga (ganske klasse for klasse):
 * - Destinasjon er ferdig
 * - Farge er ferdig
 * - Main er nokså ferdig
 * - Hovud er litt uferdig
 * - Oppdrag er ferdig (?)
 * - Rute er nokså ferdig
 * - Spelar er mindre uferdig
 * - GUI er nok uferdig?
 * - Grensesnitta er rimeleg ferdige
 * - Bord er ferdig (?)
 * 
 * Seriøse utfordringar:
 * - Det grafiske (GUI mm)
 * - KI
 * - Nettverk - siste del
 * 
 * Mindre utfordringar:
 * 	- Få brukt jokrar skikkeleg
 * 	- Få dei samme korta på bordet for alle.
 * 	- Meir skikkeleg JPanel for å vise korta ein har
 *	- Sortering av ledige (og bygde) ruter
 */

public class Main {
	static JFrame frame;
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
		GUI gui = new GUI(frame,arg,spela[spel]);
	
		frame.setTitle(frame.getTitle() + " - " +spela[spel].getTittel());
		frame.setContentPane(gui);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
