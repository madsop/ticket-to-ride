package ttr.bord;

import ttr.data.Farge;

public interface IBord {
    public void setPaaBordet(Farge[] paaBordet);
    public void setEinPaaBordet(Farge f, int plass);
    public void layFiveCardsOutOnTable();
    public Farge[] getPaaBordet();
    public Farge getRandomCardFromTheDeckAndPutOnTable(int plass, boolean leggPåBordet);
    public boolean areThereTooManyJokersOnTable();
	public void addCardsToDeck(int tabellplass, int number);
	public void addJokersToDeck(int jokrar);
	public boolean areThereAnyCardsLeftInDeck();
	public Farge getCardFromTable(int positionOnTable);
}
