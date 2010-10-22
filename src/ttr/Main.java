package ttr;

import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ttr.europe.Europe;
import ttr.nordic.Nordic;

/*
 * Store utfordringar:
 * - Det grafiske (GUI mm)
 * 	- Bygg ved å klikke på skjermen
 * 	- Kva som er bygd visast på skjermen (drawline e.l.)
 * - Kunstig intelligens
 * 	- Programmere inn støtte for dataspelar
 * 	- Trekkje oppdrag
 * 	- Byggje, trekkje oppdrag eller trekkje kort?
 * 	- Kva for rute/kort?
 * - Lesmeg og dokumentering
 * 
 * Mindre utfordringar:
 * 	Bør på plass:
 *	- Trekk tilfeldig kort (og legg ut på bordet) fungerer ikkje veldig bra
	
	Ikkje altfor viktig:
 * 	- Oppstartsskjerm
 * 		- Nødvendige innstillingar
 * 		- Vel sjølv om ein vi ha duplikatruter, bonus for lengst rute mm
 *	- Nordic manglar eitt oppdrag. No 45 oppdrag, skal vera 46.
 *	- Ordentleg avslutt-spelet-viss-nokon-lukkar-vinduet-funksjon
 *	- Kven har lengst samanhengjande rute-funksjonalitet
 *	- Vis berre antal tog for spelarane som er med, og framvising av antal tog att for fleire enn tre spelarar
 *	- Endring av konstantar for forskjellige spelutgåver
 *	- Vis kartet i ein skrollbar boks med fastsett storleik

 * Eventuelt:
 * 	- Når har spelet starta? Kan folk bli med da?
 *	- Sortering av ledige (og bygde) ruter
 *	- Forskjellig bakgrunn for forskjellige spelarar på bygde ruter-lista - koss?
 *	- Lagring av meldings-lista?
 */

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