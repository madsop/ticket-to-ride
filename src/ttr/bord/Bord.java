package ttr.bord;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;

public class Bord implements IBord {
    private final IGUI gui;
    private Farge[] paaBordet;
    private final BordHjelpar bordHjelpar;
    private final int[] igjenAvFargekort = {    // TODO lag ein finare struktur for fargar og fargekort generelt
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

    public Bord(IGUI gui, boolean nett) {
        this.gui = gui;
        paaBordet = new Farge[Konstantar.ANTAL_KORT_PÅ_BORDET];
        bordHjelpar = new BordHjelpar();

        // Legg ut dei fem korta på bordet
        if (!nett){       // TODO: Få generalisert bort denne if-en
            leggUtFem();
        }
    }

	public void setPaaBordet(Farge[] paaBordet) {
		this.paaBordet = paaBordet;

		for (int i = 0; i < paaBordet.length; i++){
			int fargenr = Konstantar.finnPosisjonForFarg(paaBordet[i]);
			leggKortPåBordet(i, fargenr);
		}
	}

    public void setEinPaaBordet(Farge f, int plass) {
		paaBordet[plass] = f;
		int j = Konstantar.finnPosisjonForFarg(f);
		igjenAvFargekort[j]--;
		gui.setKortPaaBordet(plass, f);
	}


	public void leggUtFem() {
		for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) { // Legg ut fem kort på bordet
			getTilfeldigKortFråBordet(i,true);			
		}
	}

	public int[] getIgjenAvFargekort() {
		return igjenAvFargekort;
	}


	public Farge[] getPaaBordet() {
		return paaBordet;
	}


	public int getAntalFargekortPåBordet() {
		int fargekortpåbordet = 0;
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
			fargekortpåbordet += igjenAvFargekort[i];
		}
		return fargekortpåbordet;
	}

	public Farge getTilfeldigKortFråBordet(int plass, boolean leggPåBordet) {
        int fargekortpåbordet = getAntalFargekortPåBordet();
		int teljar = bordHjelpar.tilfeldigFarge(fargekortpåbordet, igjenAvFargekort);
		if (teljar >= 0 && teljar <= Konstantar.ANTAL_FARGAR) {
			if (leggPåBordet) {
				leggKortPåBordet(plass, teljar);
			}
			return Konstantar.FARGAR[teljar];
		}
		else {
			//TODO: stokk();
			System.out.println("stokk!");
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
			System.err.println("oops");
			leggKortPåBordet(plass,0);
		}
	}

	public boolean sjekkOmJokrarPaaBordetErOK() {
		int jokrar = 0;
		for (Farge f : paaBordet) {
			if (f == Farge.valfri) {
				jokrar++;
			}
		}
        return jokrar > Konstantar.MAKS_JOKRAR_PAA_BORDET;
    }
}