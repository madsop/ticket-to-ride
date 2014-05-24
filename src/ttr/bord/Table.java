package ttr.bord;

import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.gui.GUI;

public class Table {
    private final GUI gui;
	private Deck deck;
	private CardsOnTable cardsOnTable;
    
    public Table(GUI gui, boolean nett, Deck deck) {
        this.gui = gui;
        this.deck = deck;
        this.cardsOnTable = new CardsOnTable();

        if (!nett){ // TODO: Få generalisert bort denne if-en
            layFiveCardsOutOnTable();
        }
    }

	public void setPaaBordet(Colour[] paaBordet) {
		this.setCardsOpenOnTable(paaBordet);

		for (int position = 0; position < paaBordet.length; position++){
			putCardOnTable(position, paaBordet[position]);
		}
	}

    public void putOneCardOnTable(Colour colour, int position) {
		cardsOnTable.putCardOnTable(position, colour);
		deck.removeCardFromDeck(colour);
		gui.drawCardsOnTable(position, colour);
	}

	public void layFiveCardsOutOnTable() {
		for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) {
			getRandomCardFromTheDeckAndPutOnTable(i);
		}
	}

	public Colour[] getPaaBordet() {
		return cardsOnTable.getCardsOpenOnTable();
	}
	
	public Colour getRandomCardFromTheDeckAndPutOnTable(int position) {
		Colour colour = getRandomCardFromTheDeck(position);
		putCardOnTable(position, colour);
		return colour;
	}

	public Colour getRandomCardFromTheDeck(int plass) {
		Colour randomColour = deck.getCardInRandomColourFromTheDeck();
		if (randomColour == null) {
			System.out.println("stokk!");
			return null;
		}
		return randomColour;
	}

    private void putCardOnTable(int position, int counter) { //TODO fas ut denne
    	putCardOnTable(position, Konstantar.FARGAR[counter]);
    }
    
    private void putCardOnTable(int position, Colour colour) {
		if (colour != null) {	
			cardsOnTable.putCardOnTable(position, colour);
			deck.removeCardFromDeck(colour);
			gui.drawCardsOnTable(position, colour);
		}
		else { // TODO fas ut denne
			System.err.println("oops");
			putCardOnTable(position,0);
		}
	}

	public boolean areThereTooManyJokersOnTable() {
		return cardsOnTable.areThereTooManyJokersOnTable();
    }

	public void addCardsToDeck(Colour colour, int number) {
		deck.addCards(colour, number);		
	}

	public void addJokersToDeck(int jokers) {
		deck.addJokers(jokers);
	}

	public boolean areThereAnyCardsLeftInDeck() {
		return deck.areThereAnyCardsLeftInDeck();
	}

	private void setCardsOpenOnTable(Colour[] cardsOpenOnTable) {
		cardsOnTable.setCardsOpenOnTable(cardsOpenOnTable);
	}

	public Colour getCardFromTable(int positionOnTable) {
		return cardsOnTable.getCardAt(positionOnTable);
	}

	public void updateDeckOnTable(Colour colour, int kortKrevd, int krevdJokrar, int jokrar) {
		addCardsToDeck(colour, kortKrevd - (jokrar - krevdJokrar));
		addJokersToDeck(jokrar);
	}
}