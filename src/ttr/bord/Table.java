package ttr.bord;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.gui.IGUI;

public class Table {
    private final IGUI gui;
	private Deck deck;
	private CardsOnTable cardsOnTable;
    
    public Table(IGUI gui, boolean nett, Deck deck) {
        this.gui = gui;
        this.deck = deck;
        this.cardsOnTable = new CardsOnTable();

        if (!nett){ // TODO: Få generalisert bort denne if-en
            layFiveCardsOutOnTable();
        }
    }

	public void setPaaBordet(Farge[] paaBordet) {
		this.setCardsOpenOnTable(paaBordet);

		for (int position = 0; position < paaBordet.length; position++){
			putCardOnTable(position, paaBordet[position]);
		}
	}

    public void putOneCardOnTable(Farge colour, int position) {
		cardsOnTable.putCardOnTable(position, colour);
		deck.removeCardFromDeck(colour);
		gui.drawCardsOnTable(position, colour);
	}

	public void layFiveCardsOutOnTable() {
		for (int i = 0; i < Konstantar.ANTAL_KORT_PÅ_BORDET; i++) {
			getRandomCardFromTheDeckAndPutOnTable(i);
		}
	}

	public Farge[] getPaaBordet() {
		return cardsOnTable.getCardsOpenOnTable();
	}
	
	public Farge getRandomCardFromTheDeckAndPutOnTable(int position) {
		Farge colour = getRandomCardFromTheDeck(position);
		putCardOnTable(position, colour);
		return colour;
	}

	public Farge getRandomCardFromTheDeck(int plass) {
		Farge randomColour = deck.getCardInRandomColourFromTheDeck();
		if (randomColour == null) {
			System.out.println("stokk!");
			return null;
		}
		return randomColour;
	}

    private void putCardOnTable(int position, int counter) { //TODO fas ut denne
    	putCardOnTable(position, Konstantar.FARGAR[counter]);
    }
    
    private void putCardOnTable(int position, Farge colour) {
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

	public void addCardsToDeck(Farge colour, int number) {
		deck.addCards(colour, number);		
	}

	public void addJokersToDeck(int jokers) {
		deck.addJokers(jokers);
	}

	public boolean areThereAnyCardsLeftInDeck() {
		return deck.areThereAnyCardsLeftInDeck();
	}

	private void setCardsOpenOnTable(Farge[] cardsOpenOnTable) {
		cardsOnTable.setCardsOpenOnTable(cardsOpenOnTable);
	}

	public Farge getCardFromTable(int positionOnTable) {
		return cardsOnTable.getCardAt(positionOnTable);
	}

	public void updateDeckOnTable(Farge colour, int kortKrevd, int krevdJokrar, int jokrar) {
		addCardsToDeck(colour, kortKrevd - (jokrar - krevdJokrar));
		addJokersToDeck(jokrar);
	}
}