package ttr.bord;

import java.util.HashMap;

import ttr.data.Colour;
import ttr.data.Konstantar;

public class Deck {
	private HashMap<Colour, Integer> cardsLeft;

	public Deck() {
		cardsLeft = new HashMap<>();
		cardsLeft.put(Colour.blå, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.grønn, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.gul, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.kvit, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.lilla, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.oransje, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.raud, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.svart, Konstantar.ANTAL_AV_KVART_FARGEKORT);
		cardsLeft.put(Colour.valfri, Konstantar.ANTAL_AV_KVART_FARGEKORT + 2);
	}

	void removeCardFromDeck(Colour colour) {
		cardsLeft.put(colour, cardsLeft.get(colour) - 1);
	}

	int getCardsLeftOfColour(Colour colour) {
		return cardsLeft.get(colour);
	}

	void addCards(Colour colour, int number) {
		cardsLeft.put(colour, cardsLeft.get(colour) + number);
	}

	void addJokers(int jokers) {
		cardsLeft.put(Colour.valfri, cardsLeft.get(Colour.valfri) + jokers);
	}

	Colour getCardInRandomColourFromTheDeck() {
		if (!areThereAnyCardsLeftInDeck()) { return null; }
		int randomlyChosenCardInDeck = (int) (Math.random() * getNumberOfColouredCardsLeftInDeck());

		Colour colourToReturn = null;
		int temporaryValue = 0;
		for (Colour colour : cardsLeft.keySet()) {
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