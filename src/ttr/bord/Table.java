package ttr.bord;

import ttr.data.Farge;

public interface Table {
    public void setPaaBordet(Farge[] paaBordet);
    public void putOneCardOnTable(Farge f, int plass);
    public void layFiveCardsOutOnTable();
    public Farge[] getPaaBordet();
    public Farge getRandomCardFromTheDeck(int plass);
    public Farge getRandomCardFromTheDeckAndPutOnTable(int position);
    public boolean areThereTooManyJokersOnTable();
	public void addCardsToDeck(Farge colour, int number);
	public void addJokersToDeck(int jokrar);
	public boolean areThereAnyCardsLeftInDeck();
	public Farge getCardFromTable(int positionOnTable);
}
