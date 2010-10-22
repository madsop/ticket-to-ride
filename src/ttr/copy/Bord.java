package ttr.copy;

import java.rmi.RemoteException;

public class Bord {

	private Farge[] paaBordet;
	public void setPaaBordet(Farge[] paaBordet) {
		this.paaBordet = paaBordet;

		for (int i = 0; i < paaBordet.length; i++){
			int fargenr = -1;
			for (int j = 0; j < Konstantar.FARGAR.length; j++) {
				if (paaBordet[i] == Konstantar.FARGAR[j]) {
					fargenr = j;
				}
			}
			leggKortPåBordet(i, fargenr);			
		}
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
	
	public void leggUtFem() {
		for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) { // Legg ut fem kort på bordet
			getTilfeldigKortFråBordet(i,true);			
		}
	}

	public Bord(GUI gui, boolean nett) {
		this.gui = gui;
		paaBordet = new Farge[Konstantar.ANTAL_KORT_PÅ_BORDET];

		// Legg ut dei fem korta på bordet
		if (!nett){ // samme viss nett
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



	/**
	 * Vel eit fargekort tilfeldig blant dei som ligg i stokken ("det øvste")
	 * @param plass - for å kunne sende vidare
	 * @param leggPåBordet - skal kortet leggjast på bordet eller ikkje?
	 * @return
	 */
	public Farge getTilfeldigKortFråBordet(int plass, boolean leggPåBordet) {
		int fargekortpåbordet = getAntalFargekortPåBordet();
		int valtkort = (int) (Math.random() * fargekortpåbordet);

		int teljar = 0;
		int midlertidigverdi = 0;
		while ((midlertidigverdi < valtkort) && (teljar < Konstantar.ANTAL_FARGAR) && (midlertidigverdi < fargekortpåbordet)) {
			midlertidigverdi += igjenAvFargekort[teljar];
			teljar++;
		}
		teljar--;

		if (leggPåBordet) {
			leggKortPåBordet(plass, teljar);
		}
		// sjekkJokrar(); // Evig løkke?

		if (teljar >= 0) {
			igjenAvFargekort[teljar]--;
			return Konstantar.FARGAR[teljar];
		}
		else {
			// stokk();
			return null;
		}
	}

	/**
	 * Hårate metode som tar eit kort frå bunken og legg det på bordet. 
	 * Viss det tabbar seg ut, ror det litt. (else[...])
	 * @param Kva for plass på bordet det får (0 til 4)
	 */
	private void leggKortPåBordet(int plass, int teljar) {
		if (teljar >= 0 && teljar < Konstantar.ANTAL_FARGAR) {	
			paaBordet[plass] = Konstantar.FARGAR[teljar];
			igjenAvFargekort[teljar]--;
			Farge f = Konstantar.FARGAR[teljar];
			gui.setKortPaaBordet(plass, f);
		}
		else {
			leggKortPåBordet(plass,0);
		}
	}

	private void sjekkJokrar() {
		int jokrar = 0;
		for (int i = 0; i < paaBordet.length; i++) {
			if (paaBordet[i] == Farge.valfri) {
				jokrar++;
			}
		}

		if (jokrar > Konstantar.MAKS_JOKRAR_PAA_BORDET) {
			for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) {
				getTilfeldigKortFråBordet(i, true);
			}
		}
	}	
}