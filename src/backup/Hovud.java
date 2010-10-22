package backup;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

public class Hovud {

	// "Variablar"
	private SpelUtgaave spel;
	private Bord bord;
	private static Set<Rute> ruter;
	private ArrayList<Spelar> spelarar;
	private static GUI gui;
	
	public static Spelar host;
	private boolean nett;
	private int antalSpelarar;
	
	// Variablar
	private ArrayList<Oppdrag> gjenverandeOppdrag;
	private Spelar kvenSinTur;
	private ArrayList<Rute> alleBygdeRuter;
	private Spelar minSpelar;

	/**
	 * Oppretter ein hovud-instans
	 * @param gui
	 * @throws RemoteException 
	 */
	public Hovud(GUI gui, boolean nett, SpelUtgaave spel) throws RemoteException {
		Hovud.gui = gui;
		this.nett = nett;
		this.spel = spel;
		LagBrettet(nett);
	}

	/*
	 * Get & set
	 */
	public static Set<Rute> getRuter() {
		return ruter;
	}

	public ArrayList<Rute> getAlleBygdeRuter() {
		return alleBygdeRuter;
	}
	public void setMinSpelar(Spelar spelar){
		minSpelar = spelar;
	}

	public Bord getBord() {
		return bord;
	}
	public boolean isNett() {
		return nett;
	}
	/**
	 * @return Kva for spelar er det sin tur no?
	 */
	public Spelar getKvenSinTur() {
		return kvenSinTur;
	}
	public ArrayList<Spelar> getSpelarar() {
		return spelarar;
	}
	/**
	 * @return GUI-klassa tilhøyrande denne hovudklassa
	 */
	public static GUI getGui() {
		return gui;
	}
	/**
	 * @return kor mange oppdrag som ligg på bordet
	 */
	public int getAntalGjenverandeOppdrag () {
		return gjenverandeOppdrag.size();
	}
	
	/**
	 * Tar det øvste oppdraget frå oppdragsbunken
	 * @return oppdraget
	 */
	public Oppdrag getOppdrag() {
		Oppdrag oppdrag = gjenverandeOppdrag.remove(0);
		return oppdrag;
	}

	/**
	 * Stokkar dei gjenverande oppdraga.
	 */
	private void stokkOppdrag() {
		for (int i = 0; i < gjenverandeOppdrag.size(); i++) {
			Oppdrag temp = gjenverandeOppdrag.get(i);
			int rand = (int) (Math.random() * gjenverandeOppdrag.size());
			gjenverandeOppdrag.set(i, gjenverandeOppdrag.get(rand));
			gjenverandeOppdrag.set(rand, temp);
		}
	}

	/**
	 * Startar spelet.
	 * @throws RemoteException 
	 */
	private void LagBrettet(boolean nett) throws RemoteException {
		spelarar = new ArrayList<Spelar>();
		// Legg til ruter
		bord = new Bord(gui,nett);
		ruter = spel.getRuter();
		alleBygdeRuter = new ArrayList<Rute>();

		// Legg til oppdrag
		gjenverandeOppdrag = spel.getOppdrag();
		stokkOppdrag();

		if (!nett) {
			mekkSpelarar();
		}
		else { // Nettverksspel
			/*			try {

				
				if (minSpelar.getSpelarNummer() == 0) { // Eg er verten
				}
				
				else {
					// Få korta som skal liggje på bordet frå verten.
				}
				
			}
			catch (RemoteException re) {
				re.printStackTrace();
			}*/
		}
	}
	
	private void mekkSpelarar() throws RemoteException {

		// Legg til spelarar
		while ( (antalSpelarar != 2) && (antalSpelarar != 3)) { // Sett antal spelarar
			Object[] val = {2,3};
			antalSpelarar = JOptionPane.showOptionDialog(gui, "Kor mange spelarar skal vera med??", "Antal spelarar?", 0, 3, null,val, 2);
			antalSpelarar += 2;
		}

		//antalSpelarar = 3;
		spelarar = new ArrayList<Spelar>();
		for (int i = 1; i <= antalSpelarar; i++) { // Opprettar spelarar
			try {
				spelarar.add(new SpelarImpl(this,JOptionPane.showInputDialog(gui,"Skriv inn namnet på spelar " +i)));
			}
			catch (RemoteException re) {

			}
		}
		settSinTur(spelarar.get(0));
	}

	public Spelar getMinSpelar() {
		return minSpelar;
	}

	/**
	 * Sett at det er denne spelaren sin tur;
	 * @throws RemoteException 
	 */
	public void settSinTur(Spelar spelar) throws RemoteException {
		kvenSinTur = spelar;
		gui.setSpelarnamn(kvenSinTur.getNamn());
	}

	/** Spelaren er ferdig med sin tur, no er det neste spelar
	 * @throws RemoteException 
	 * 
	 */
	public void nesteSpelar() throws RemoteException {
		int sp = 0;
		for (int i = 0; i < spelarar.size(); i++) {
			if (spelarar.get(i) == kvenSinTur) {
				sp = i;
			}
		}
		sp++;

		if (!nett) {
			if (sp == spelarar.size()) {
				kvenSinTur = spelarar.get(0);
			}
			else {
				kvenSinTur = spelarar.get(sp);
			}

			if (kvenSinTur == minSpelar) {
				kvenSinTur = spelarar.get(0);
			}
			for (int i = 0; i < spelarar.size(); i++) {
				if (kvenSinTur == spelarar.get(i) && spelarar.size() > i+1) {
					kvenSinTur = spelarar.get(i+1);
				}
			}

		}

		if (nett) {
			int no = kvenSinTur.getSpelarNummer();
			Spelar host = null;
			if (minSpelar.getSpelarNummer() == 0) {
				host = (Spelar)minSpelar;
			}
			else {
				for (Spelar s : spelarar) {
					if (s.getSpelarNummer() == 0) {
						host = (Spelar) s;
					}
				}
			}

			int neste;
			if (no+1 < host.getSpelarteljar()) {
				neste = no+1;
			}
			else {
				neste = 0;
			}

			if (minSpelar.getSpelarNummer() == neste) {
				kvenSinTur = (Spelar) minSpelar;
			}
			else {
				for (Spelar s : spelarar) {
					if (s.getSpelarNummer() == neste) {
						kvenSinTur = (Spelar) s;
					}
				}
			}

			for (Spelar s : spelarar) {
				s.settSinTur(kvenSinTur);
			}

			if (kvenSinTur.getNamn().equals(minSpelar.getNamn())) {
				gui.getSpelarnamn().setBackground(Color.YELLOW);
			}
		}
		gui.setSpelarnamn(kvenSinTur.getNamn());
		kvenSinTur.setEinVald(false);


		if (kvenSinTur.getGjenverandeTog() < 3) {
			String poeng = new String("Spelet er ferdig.");

			if (nett) {
				poeng += " Spelar " +minSpelar.getNamn() + " fekk " +reknUtPoeng(minSpelar) + " poeng. ";				
			}

			for (int i = 0; i < spelarar.size(); i++) {
				poeng += " Spelar " +spelarar.get(i).getNamn() +" fekk " +reknUtPoeng(spelarar.get(i)) +" poeng. ";
			}

			JOptionPane.showMessageDialog(Hovud.gui, poeng);
		}
	}

	private int reknUtPoeng(Spelar s) throws RemoteException {
		int poeng = s.getOppdragspoeng();
		for (int j = 0; j < s.getBygdeRuterStr(); j++) {
			for (Rute r : ruter) {
				if (s.getBygdeRuterId(j) == r.getRuteId()) {
					poeng += r.getVerdi();
				}
			}
		}

		return poeng;
	}

	/**
	 * @return Eit array over alle rutene som ikkje alt er bygd
	 * @throws RemoteException 
	 */
	public Rute[] finnFramRuter() throws RemoteException {
		Set<Rute> ruter = Hovud.getRuter();
		for (int i = 0; i < spelarar.size(); i++) {
			for (int j = 0; j < spelarar.get(i).getBygdeRuterStr(); j++) {
				int ruteId = spelarar.get(i).getBygdeRuterId(j);
				for (Rute r : ruter) {
					if (r.getRuteId() == ruteId && !(alleBygdeRuter.contains(r))) {
						alleBygdeRuter.add(r);
					}
				}
			}
		}

		int str = ruter.size();
		Rute[] ruterTemp = new Rute[str];
		Iterator<Rute> it2 = ruter.iterator();
		for (int i = 0; i < str; i++) {
			ruterTemp[i] = it2.next();
		}

		/*		bR = new ArrayList<Rute>();
		for (int i = 0; i < spelarar.size(); i++) {
			/*for (int j = 0; j < spelarar.get(i).getBygdeRuter().size(); j++ ) {
				bR.add(spelarar.get(i).getBygdeRuter().get(j));
			}
			for (int j = 0; j < spelarar.get(i).getBygdeRuterStr(); j++) {
				int ruteId = spelarar.get(i).getBygdeRuterId(j);
				for (Rute r : ruter) {
					if (r.getRuteId() == ruteId) {
						bR.add(r);
					}
				}
			}
		}
		 */
		Rute[] ruterArray = new Rute[str-alleBygdeRuter.size()];
		System.out.println(ruterArray.length);
		int c = 0;

		for (int i = 0; i < str; i++) {
			boolean b = true;
			for (int j = 0; j < alleBygdeRuter.size(); j++) {
				if (ruterTemp[i] == alleBygdeRuter.get(j)) {
					b = false;
				}
			}
			if (b) {
				Rute r = ruterTemp[i];
				if (c < ruterArray.length) {
					ruterArray[c] = r;
					c++;
				}

			}
		}
		return ruterArray;
	}

	/**
	 * Byggjar ei rute.
	 * @param bygd - kva for rute.
	 * @param plass - kva for farge - i plass i farge-arrayet.
	 * @param spelarensKort - korta spelaren har.
	 * @param kortKrevd - kor mange vanlege kort ruta krev.
	 * @param krevdJokrar - kor mange jokrar som trengs for å byggje ruta.
	 * @throws RemoteException 
	 */
	public void bygg(Rute bygd, int plass, int[] spelarensKort, int kortKrevd, int krevdJokrar) throws RemoteException {
		Spelar byggjandeSpelar;

		if (nett) {
			byggjandeSpelar = minSpelar;
		}
		else {
			byggjandeSpelar = kvenSinTur;
		}

		byggjandeSpelar.bygg(bygd, bygd.getFarge());
		byggjandeSpelar.getKort()[plass] -= (kortKrevd-krevdJokrar);
		byggjandeSpelar.getKort()[spelarensKort.length-1] -= krevdJokrar;
		alleBygdeRuter.add(bygd);

		if (nett) {
			for (Spelar s : spelarar) {
				System.out.println("så langt bra");
				s.nybygdRute(bygd.getRuteId(),byggjandeSpelar);
				s.setTogAtt(byggjandeSpelar.getSpelarNummer()+1, byggjandeSpelar.getGjenverandeTog());
			}
		}
		gui.getTogAtt()[byggjandeSpelar.getSpelarNummer()+1].setText(String.valueOf(byggjandeSpelar.getGjenverandeTog()));		
		bord.getIgjenAvFargekort()[plass]+=kortKrevd;
		bord.getIgjenAvFargekort()[spelarensKort.length-1]+=krevdJokrar;
		// Generaliser desse.
		
		nesteSpelar();
	}

	/**
	 * Blir kalla viss spelaren prøver å byggje ei tunnelrute. Samme parameter som for bygg.
	 * @param bygd
	 * @param plass
	 * @param spelarensKort
	 * @param kortKrevd
	 * @param krevdJokrar
	 * @throws RemoteException 
	 */
	public void byggTunnel(Rute bygd, int plass, int[] spelarensKort, int kortKrevd, int krevdJokrar) throws RemoteException {
		Farge[] treTrekte = new Farge[3];
		int ekstra = 0;
		for (int i = 0; i < treTrekte.length; i++) {
			treTrekte[i] = bord.getTilfeldigKortFråBordet(0, false);
			if (treTrekte[i] == Farge.valfri || treTrekte[i] == bygd.getFarge()) {
				ekstra++;
			}
		}

		int byggLell = JOptionPane.showConfirmDialog(gui, "Du prøver å byggje ei rute som er tunnel. " +
				"Det krev at du snur tre kort. Det vart " +treTrekte[0] +", "
				+treTrekte[1] +" og " +treTrekte[2] 
				                                 +". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
		if (byggLell == JOptionPane.OK_OPTION) {
			bygg(bygd, plass, spelarensKort, kortKrevd+ekstra, krevdJokrar);
		}
	}

}