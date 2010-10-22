package nordic;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Startar spelet og er eigentleg kjerna. Blir kalla frå static void main.
 */
public class Hovud extends JPanel {
	// Brettet
	// Kartet (dvs bakgrunnen)
	private static final int ANTAL_KORT_PÅ_BORDET = 5;
	private static final int ANTAL_STARTOPPDRAG = 5;
	private static final int ANTAL_FARGAR = 9;
	private Spelar[] spelarar;
	private Farge[] paaBordet;
	private ArrayList<Oppdrag> gjenverandeOppdrag;
	private static final Farge[] fargar = {Farge.blå, Farge.raud, Farge.kvit, Farge.svart, 
			Farge.gul, Farge.grønn, Farge.oransje, Farge.lilla, Farge.valfri};
	private int[] igjenAvFargekort = {12,12,12,12,12,12,12,12,14}; // samme rekkjefølge som i fargar
	private int antalSpelarar;
	
	// GUI-variablar
	private GridBagLayout gbl;
	private GridBagConstraints c;
	private final int hogde = 976;
	private final int breidde = 1200;
	private JPanel hogre;
	private final int hogrehogde = hogde-100;
	private final int hogrebreidde = hogde/3;
	
	
	public Hovud() {
		LagBrettet();
		// Gi oppdrag til spelarane
		// La resten av oppdraga ligge i bunken
		// La førstemann byrja
	}
	
	public int getAntalGjenverandeOppdrag () {
		return gjenverandeOppdrag.size();
	}
	public Oppdrag getOppdrag() {
		Oppdrag oppdrag = gjenverandeOppdrag.remove(0);
		return oppdrag;
	}
	
	/**
	 * Hårate metode som tar eit kort frå bunken og legg det på bordet.
	 * @param Kva for plass på bordet det får (0 til 4)
	 */
	public void leggKortPåBordet(int plass) {
		int fargekortpåbordet = 0;
		for (int i = 0; i < ANTAL_FARGAR; i++) {
			fargekortpåbordet += igjenAvFargekort[i];
		}
		int valtkort = (int) (Math.random() * fargekortpåbordet);
		
		int teljar = 0;
		int midlertidigverdi = 0;
		while ((midlertidigverdi < valtkort) && (teljar < ANTAL_FARGAR) && (midlertidigverdi < fargekortpåbordet)) {
			midlertidigverdi += igjenAvFargekort[teljar];
			teljar++;
		}
		teljar--;
		
			System.out.println("plass: " +plass +", teljar: " +teljar);
		if (teljar >= 0 && teljar < ANTAL_FARGAR) {	
			paaBordet[plass] = fargar[teljar];
			igjenAvFargekort[teljar]--;
		}
		else {
			leggKortPåBordet(plass);
		}
	}
	
	//public void leggTilSpelar(); etc
	
	public void LagBrettet() {
		
		// GUI-oppsett
		gbl = new GridBagLayout();
		setLayout(gbl);
		c = new GridBagConstraints();
		c.ipadx = 40;
		c.ipady = 10;
		
		setPreferredSize(new Dimension(breidde,hogde));
		
		hogre = new JPanel();
		hogre.setSize(new Dimension(hogrehogde,hogrebreidde));
		c.anchor = GridBagConstraints.EAST;
		
		add(hogre,c);
		
		gjenverandeOppdrag = new ArrayList<Oppdrag>();
		// sett inn oppdrag manuelt her
		
		paaBordet = new Farge[ANTAL_KORT_PÅ_BORDET];
		// Opprettar alle ruter og oppdrag mm

		for (int i = 0; i < ANTAL_KORT_PÅ_BORDET; i++) { // Legg ut fem kort på bordet
			leggKortPåBordet(i);
		}
		while ( (antalSpelarar != 2) && (antalSpelarar != 3)) { // Sett antal spelarar
			Object[] val = {2,3};
			antalSpelarar = JOptionPane.showOptionDialog(this, "Kor mange spelarar skal vera med??", "Antal spelarar?", 0, 3, null,val, 2);
			antalSpelarar += 2;
		}
		spelarar = new Spelar[antalSpelarar];
		for (int i = 1; i <= antalSpelarar; i++) { // Opprettar spelarar
			spelarar[i-1] = new Spelar(this,JOptionPane.showInputDialog(this,"Skriv inn namnet på spelar " +i));
		}
		
		for (int spelar = 0; spelar < antalSpelarar; spelar++) {
			for (int oppdragsNummer = 0; oppdragsNummer < ANTAL_STARTOPPDRAG; oppdragsNummer++) {
				spelarar[spelar].trekkOppdragskort();
			}
		}

	}
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("Ticket to ride - Nordic countries");
		frame.setContentPane(new Hovud());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
