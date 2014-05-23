package ttr.bord;

import ttr.data.Farge;

public interface Table {
    public void setPaaBordet(Farge[] paaBordet);
    public void setEinPaaBordet(Farge f, int plass);
    public void layFiveCardsOutOnTable();
    public Farge[] getPaaBordet();
    public Farge getRandomCardFromTheDeckAndPutOnTable(int plass, boolean leggPåBordet);
    public boolean areThereTooManyJokersOnTable();
	public void addCardsToDeck(Farge colour, int number);
	public void addJokersToDeck(int jokrar);
	public boolean areThereAnyCardsLeftInDeck();
	public Farge getCardFromTable(int positionOnTable);
}
