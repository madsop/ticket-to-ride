package ttr.bord;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;

public class BordImpl implements IBord {
    private final IGUI gui;
    private Farge[] cardsOpenOnTable;
    private final int[] colourCardsLeftInDeck = {    // TODO lag ein finare rute for fargar og fargekort generelt
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

    public BordImpl(IGUI gui, boolean nett) {
        this.gui = gui;
        cardsOpenOnTable = new Farge[Konstantar.ANTAL_KORT_PÅ_BORDET];

        // Legg ut dei fem korta på bordet
        if (!nett){       // TODO: Få generalisert bort denne if-en
            leggUtFem();
        }
    }

	public void setPaaBordet(Farge[] paaBordet) {
		this.cardsOpenOnTable = paaBordet;

		for (int i = 0; i < paaBordet.length; i++){
			int fargenr = Konstantar.finnPosisjonForFarg(paaBordet[i]);
			leggKortPåBordet(i, fargenr);
		}
	}

    public void setEinPaaBordet(Farge farge, int plass) {
		cardsOpenOnTable[plass] = farge;
		int kortPosisjon = Konstantar.finnPosisjonForFarg(farge);
		colourCardsLeftInDeck[kortPosisjon]--;
		gui.teiknOppKortPåBordet(plass, farge);
	}

	public void leggUtFem() {
		for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) { // Legg ut fem kort på bordet
			getRandomCardFromTheDeck(i,true);			
		}
	}

	public int[] getFargekortaSomErIgjenIBunken() {
		return colourCardsLeftInDeck;
	}

	public Farge[] getPaaBordet() {
		return cardsOpenOnTable;
	}

	public int getAntalFargekortPåBordet() {
		int fargekortpåbordet = 0;
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
			fargekortpåbordet += colourCardsLeftInDeck[i];
		}
		return fargekortpåbordet;
	}

	public Farge getRandomCardFromTheDeck(int plass, boolean leggPåBordet) {
        int numberOfColouredCardsOnTheTable = getAntalFargekortPåBordet();
		int randomColour = tilfeldigFarge(numberOfColouredCardsOnTheTable, colourCardsLeftInDeck);
		if (randomColour >= 0 && randomColour <= Konstantar.ANTAL_FARGAR) {
			if (leggPåBordet) {
				leggKortPåBordet(plass, randomColour);
			}
			return Konstantar.FARGAR[randomColour];
		}
		//TODO: stokk();
		System.out.println("stokk!");
		return null;
	}

	/**
	 * Hårate metode som tar eit kort frå bunken og legg det på bordet. 
	 * Viss det tabbar seg ut, ror det litt. (else[...])
	 * @param Kva for plass på bordet det får (0 til 4)
	 */
    private void leggKortPåBordet(int plass, int teljar) {
		if (teljar >= 0 && teljar < Konstantar.ANTAL_FARGAR) {	
			cardsOpenOnTable[plass] = Konstantar.FARGAR[teljar];
			colourCardsLeftInDeck[teljar]--;
			Farge f = Konstantar.FARGAR[teljar];
			gui.teiknOppKortPåBordet(plass, f);
		}
		else {
			System.err.println("oops");
			leggKortPåBordet(plass,0);
		}
	}

	public boolean sjekkOmAntalJokrarPaaBordetErOK() {
		int jokrar = 0;
		for (Farge f : cardsOpenOnTable) {
			if (f == Farge.valfri) {
				jokrar++;
			}
		}
        return jokrar > Konstantar.MAKS_JOKRAR_PAA_BORDET;
    }

    private int tilfeldigFarge(int colourCardsLeftOnTable, int[] cardsLeftOnTable){ //TODO: den siste her bør vel erstattes av ein map el
        int randomlyChosenCardInDeck = (int) (Math.random() * colourCardsLeftOnTable);

        int counter = 0;
        int temporaryValue = 0;
        while (shouldIncrease(colourCardsLeftOnTable, randomlyChosenCardInDeck, counter, temporaryValue)) {
            temporaryValue += cardsLeftOnTable[counter];
            counter++;
        }
        return counter-1;
    }

	private boolean shouldIncrease(int colourCardsLeftOnTable, int indexOfRandomlyChosenCardInDeck, int colorCounter, int temporaryValue) {
		return (temporaryValue < indexOfRandomlyChosenCardInDeck) && (colorCounter < Konstantar.ANTAL_FARGAR) && (temporaryValue <= colourCardsLeftOnTable);
	}
}