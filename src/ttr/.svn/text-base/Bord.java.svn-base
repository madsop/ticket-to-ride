package ttr;

public class Bord {
	/*
	 * Kallhierarki her:
	 * 
	 */

	private Farge[] paaBordet;
	/**
	 * Får inn eit array over kva for kort som skal liggje på bordet.
	 */
	public void setPaaBordet(Farge[] paaBordet) {
		this.paaBordet = paaBordet;

		for (int i = 0; i < paaBordet.length; i++){
			int fargenr = Konstantar.finnPosisjonForFarg(paaBordet[i]);
			leggKortPåBordet(i, fargenr);
		}
	}
	/**
	 * Samme som over, men for eitt fastsett kort.
	 * @param f - kva for farge
	 * @param plass - kva for plass på bordet
	 */
	public void setEinPaaBordet(Farge f, int plass) {
		paaBordet[plass] = f;
		int j = Konstantar.finnPosisjonForFarg(f);
		igjenAvFargekort[j]--;
		gui.setKortPaaBordet(plass, f);
	}

	private GUI gui;
	private int[] igjenAvFargekort = {
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT,
			Konstantar.ANTAL_AV_KVART_FARGEKORT+2
	}; // samme rekkjefølge som i fargar
	
	/**
	 * Legg ut så mange kort det skal vera på bordet.
	 */
	public void leggUtFem() {
		for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) { // Legg ut fem kort på bordet
			getTilfeldigKortFråBordet(i,true);			
		}
	}

	/**
	 * Opprettar eit bord-objekt.
	 * @param gui
	 * @param nett
	 */
	public Bord(GUI gui, boolean nett) {
		this.gui = gui;
		paaBordet = new Farge[Konstantar.ANTAL_KORT_PÅ_BORDET];

		// Legg ut dei fem korta på bordet
		if (!nett){
			leggUtFem();
		}
	}

	/**
	 * @return Kor mange fargekort som er igjen i bunken.
	 */
	public int[] getIgjenAvFargekort() {
		return igjenAvFargekort;
	}

	/**
	 * @return eit farge-array med korta som ligg på bordet.
	 */
	public Farge[] getPaaBordet() {
		return paaBordet;
	}


	/**
	 * Tel kor mange kort som ligg i stokken på bordet
	 * @return antalet
	 */
	public int getAntalFargekortPåBordet() {
		int fargekortpåbordet = 0;
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
			fargekortpåbordet += igjenAvFargekort[i];
		}
		return fargekortpåbordet;
	}

	public int tilfeldigFarge(){
		int fargekortpåbordet = getAntalFargekortPåBordet();
		int valtkort = (int) (Math.random() * fargekortpåbordet);

		int teljar = 0;
		int midlertidigverdi = 0;
		while ((midlertidigverdi < valtkort) 
				&& (teljar < Konstantar.ANTAL_FARGAR) 
				&& (midlertidigverdi <= fargekortpåbordet)) {
			midlertidigverdi += igjenAvFargekort[teljar];
			teljar++;
		}
		teljar--;
		return teljar;
	}

	/**
	 * Vel eit fargekort tilfeldig blant dei som ligg i stokken ("det øvste")
	 * @param plass - for å kunne sende vidare
	 * @param leggPåBordet - skal kortet leggjast på bordet eller ikkje?
	 * @return
	 */
	public Farge getTilfeldigKortFråBordet(int plass, boolean leggPåBordet) {
		int teljar = tilfeldigFarge();
		if (teljar >= 0 && teljar <= Konstantar.ANTAL_FARGAR) {
			if (leggPåBordet) {
				leggKortPåBordet(plass, teljar);
			}
			return Konstantar.FARGAR[teljar];
		}
		else {
			// stokk();
			System.out.println("stokk!");
			return null;
		}
	}

	/**
	 * Hårate metode som tar eit kort frå bunken og legg det på bordet. 
	 * Viss det tabbar seg ut, ror det litt. (else[...])
	 * @param Kva for plass på bordet det får (0 til 4)
	 */
	public void leggKortPåBordet(int plass, int teljar) {
		if (teljar >= 0 && teljar < Konstantar.ANTAL_FARGAR) {	
			paaBordet[plass] = Konstantar.FARGAR[teljar];
			igjenAvFargekort[teljar]--;
			Farge f = Konstantar.FARGAR[teljar];
			gui.setKortPaaBordet(plass, f);
		}
		else {
			System.err.println("oops");
			leggKortPåBordet(plass,0);
		}
	}

	/**
	 * Kor mange jokrar ligg på bordet?
	 * @return
	 */
	public boolean sjekkJokrar() {
		int jokrar = 0;
		for (Farge f : paaBordet) {
			if (f == Farge.valfri) {
				jokrar++;
			}
		}

		if (jokrar > Konstantar.MAKS_JOKRAR_PAA_BORDET) {
			return true;
		}
		return false;
	}	
}