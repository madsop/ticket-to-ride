package ttr.bord;

import java.beans.PropertyChangeListener;

import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.gui.GUI;

public class Table {
	private Deck deck;
	private CardsOnTable cardsOnTable;

	public Table(GUI gui, boolean isNetworkGame, Deck deck) {
		this.deck = deck;
		this.cardsOnTable = new CardsOnTable();

		if (!isNetworkGame){ // TODO: Få generalisert bort denne if-en
			layFiveCardsOutOnTable();
		}
	}

	public void putCardsOnTable(Colour[] cardsToPutOnTable) {
		cardsOnTable.setCardsOpenOnTable(cardsToPutOnTable);
		for (int position = 0; position < cardsToPutOnTable.length; position++){
			putOneCardOnTable(cardsToPutOnTable[position], position);
		}
	}

	public void putOneCardOnTable(Colour colour, int position) {
		if (colour != null) {
			cardsOnTable.putCardOnTable(position, colour);
			deck.removeCardFromDeck(colour);
		}
		else { // TODO fas ut denne
			System.err.println("oops");
			putOneCardOnTable(Colour.blå, position);
		}
	}

	public void layFiveCardsOutOnTable() {
		for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) {
			getRandomCardFromTheDeckAndPutOnTable(i);
		}
	}

	public Colour[] getCardsOpenOnTable() {
		return cardsOnTable.getCardsOpenOnTable();
	}

	public Colour getRandomCardFromTheDeckAndPutOnTable(int position) {
		Colour colour = getRandomCardFromTheDeck();
		putOneCardOnTable(colour, position);
		return colour;
	}

	public Colour getRandomCardFromTheDeck() {
		Colour randomColour = deck.getCardInRandomColourFromTheDeck();
		if (randomColour == null) {
			System.out.println("stokk!");
			return null;
		}
		return randomColour;
	}

	public boolean areThereTooManyJokersOnTable() {
		return cardsOnTable.areThereTooManyJokersOnTable();
	}

	public boolean areThereAnyCardsLeftInDeck() {
		return deck.areThereAnyCardsLeftInDeck();
	}

	public Colour getCardFromTable(int positionOnTable) {
		return cardsOnTable.getCardAt(positionOnTable);
	}

	public void updateDeckOnTable(Colour colour, int numberOfCardsDemanded, int numberOfJokersDemanded, int numberOfJokers) {
		deck.addCards(colour, numberOfCardsDemanded - (numberOfJokers - numberOfJokersDemanded));
		deck.addCards(Colour.valfri, numberOfJokers);
	}

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		cardsOnTable.addPropertyChangeListener(propertyChangeListener);
	}
}