package ttr.bord;

import java.util.HashMap;

import ttr.data.Farge;
import ttr.data.Konstantar;

public class Deck {
	public Deck() {
		initCardsLeft();
	}
    
	//todo reinare og finare med cardsleft
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
    
    private HashMap<Farge, Integer> cardsLeft;

    void initCardsLeft() { // todo fortsett å fase inn denne
    	cardsLeft = new HashMap<>();
    	cardsLeft.put(Farge.blå, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.grønn, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.gul, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.kvit, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.lilla, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.oransje, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.raud, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.svart, Konstantar.ANTAL_AV_KVART_FARGEKORT);
    	cardsLeft.put(Farge.valfri, Konstantar.ANTAL_AV_KVART_FARGEKORT + 2);
    }
	
	void removeCardFromDeck(Farge colour) {
		int position = Konstantar.finnPosisjonForFarge(colour);
		colourCardsLeftInDeck[position]--;
		cardsLeft.put(colour, cardsLeft.get(colour) - 1);
	}
	
	int getCardsLeftOfColour(Farge colour) {
		return cardsLeft.get(colour);
	}

	void addCards(Farge colour, int number) {
		cardsLeft.put(colour, cardsLeft.get(colour) + number);
	}

	void addJokers(int jokers) {
		colourCardsLeftInDeck[colourCardsLeftInDeck.length-1] += jokers;
	}
	
    private int getIDOfCardInRandomColour(){ //TODO: den siste her bør vel erstattes av ein map el
    	int colourCardsLeftOnTable = getNumberOfColouredCardsLeftInDeck();
        int randomlyChosenCardInDeck = (int) (Math.random() * colourCardsLeftOnTable);

        int counter = 0;
        int temporaryValue = 0;
        while (shouldIncrease(colourCardsLeftOnTable, randomlyChosenCardInDeck, counter, temporaryValue)) {
            temporaryValue += colourCardsLeftInDeck[counter];
            counter++;
        }
        return counter-1;
    }
    
    Farge getCardInRandomColour() { //TODO denne gir out of bounds om tom for kort?
    	int fargeID = getIDOfCardInRandomColour();
    	return Konstantar.FARGAR[fargeID];
    }

	private boolean shouldIncrease(int colourCardsLeftOnTable, int indexOfRandomlyChosenCardInDeck, int colorCounter, int temporaryValue) {
		return (temporaryValue < indexOfRandomlyChosenCardInDeck) && (colorCounter < Konstantar.ANTAL_FARGAR) && (temporaryValue <= colourCardsLeftOnTable);
	}

	public boolean areThereAnyCardsLeftInDeck() {
		return true;//todo
	}
	
	private int getNumberOfColouredCardsLeftInDeck() {
		int fargekortpåbordet = 0;
		for (Farge colour : Konstantar.FARGAR) {
			fargekortpåbordet += getCardsLeftOfColour(colour);
		}
		return fargekortpåbordet;
	}
}