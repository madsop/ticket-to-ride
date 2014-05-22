package ttr.bord;

import java.util.HashMap;

import ttr.data.Farge;
import ttr.data.Konstantar;

public class Deck {
	private HashMap<Farge, Integer> cardsLeft;

	public Deck() {
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
		cardsLeft.put(colour, cardsLeft.get(colour) - 1);
	}

	int getCardsLeftOfColour(Farge colour) {
		return cardsLeft.get(colour);
	}

	void addCards(Farge colour, int number) {
		cardsLeft.put(colour, cardsLeft.get(colour) + number);
	}

	void addJokers(int jokers) {
		cardsLeft.put(Farge.valfri, cardsLeft.get(Farge.valfri) + jokers);
	}

	Farge getCardInRandomColour() {
		int randomlyChosenCardInDeck = (int) (Math.random() * getNumberOfColouredCardsLeftInDeck());

		Farge colourToReturn = null;
		int temporaryValue = 0;
		for (Farge colour : cardsLeft.keySet()) {
			if (temporaryValue >= randomlyChosenCardInDeck) {
				break;
			}
			temporaryValue += cardsLeft.get(colour);
			colourToReturn = colour;
		}

		return colourToReturn;
	}

	public boolean areThereAnyCardsLeftInDeck() {
		return getNumberOfColouredCardsLeftInDeck() > 0;
	}

	private int getNumberOfColouredCardsLeftInDeck() {
		return cardsLeft.values().stream().mapToInt(x -> x.intValue()).sum();
	}
}