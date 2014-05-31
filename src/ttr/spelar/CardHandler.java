package ttr.spelar;

import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.kjerna.Core;

import java.util.HashMap;
import java.util.Map;

public class CardHandler {
	private Core hovud;
	private Map<Colour, Integer> cards;

	CardHandler(Core hovud) {
		super();
		this.hovud = hovud;
		faaInitielleFargekort();
	}

	public Colour getRandomCardFromTheDeck(int positionOnTable) {
		Colour colourOfTheRandomCard = hovud.getTable().getRandomCardFromTheDeckAndPutOnTable(positionOnTable);

		if (colourOfTheRandomCard == null){
			hovud.displayGraphicallyThatThereIsNoCardHere(positionOnTable);
			return null;
		}

		hovud.getTable().putOneCardOnTable(colourOfTheRandomCard, positionOnTable);
		return colourOfTheRandomCard;
	}

	public void receiveCard(Colour colour) {
		cards.put(colour, cards.get(colour) + 1);
	}

	public Colour drawRandomCardFromTheDeck() {
		return hovud.getTable().getRandomCardFromTheDeck();
	}

	public int getNumberOfCardsLeftInColour(Colour colour) {
		return cards.get(colour);
	}

	public void decrementCardsAt(Colour colour, int number) {
		cards.put(colour, cards.get(colour) - number);
	}

	private void faaInitielleFargekort() {
		initialiseCards();
		for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
			Colour trekt = drawRandomCardFromTheDeck();
			cards.put(trekt, cards.get(trekt) + 1); //todo denne feilar som berre pokker..hmm
		}
	}

	private void initialiseCards() {
		cards = new HashMap<>();
		for (Colour colour : Colour.values()) {
			cards.put(colour, 0);
		}
	}
}