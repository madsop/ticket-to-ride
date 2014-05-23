package ttr.spelar;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class CardHandlerImpl extends UnicastRemoteObject implements CardHandler { //TODO refaktoriser veldig mykje av denne...
	private static final long serialVersionUID = 3899317463384337994L;
	private IHovud hovud;
	private Map<Farge, Integer> cards;

	CardHandlerImpl(IHovud hovud) throws RemoteException {
		super();
		this.hovud = hovud;
		faaInitielleFargekort();
	}

	private void faaInitielleFargekort() {
		initialiseCards();
		for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
			Farge trekt = trekkFargekort();
			cards.put(trekt, cards.get(trekt)+1);
		}
	}

	private void initialiseCards() {
		cards = new HashMap<>();
		for (Farge colour : Farge.values()) {
			cards.put(colour, 0);
		}
	}

	public Farge getRandomCardFromTheDeck(int positionOnTable) {
		Farge colourOfTheRandomCard = hovud.getBord().getRandomCardFromTheDeckAndPutOnTable(positionOnTable, true);

		if (colourOfTheRandomCard == null){
			hovud.displayGraphicallyThatThereIsNoCardHere(positionOnTable);
			return null;
		}

		hovud.getBord().setEinPaaBordet(colourOfTheRandomCard, positionOnTable);
		return colourOfTheRandomCard;
	}

	public void receiveCard(Farge colour) {
		cards.put(colour, cards.get(colour) + 1);
	}

	/** @return eit tilfeldig fargekort frå toppen av stokken */
	public Farge trekkFargekort() {
		if (hovud.getBord().areThereAnyCardsLeftInDeck()) { // TODO ser ut som om denne if-en bør inn i bord-klassa
			return hovud.getBord().getRandomCardFromTheDeckAndPutOnTable(0, false);
		}
		return null;
	}

	public int getNumberOfCardsLeftInColour(Farge colour) throws RemoteException {
		return cards.get(colour);
	}

	public void decrementCardsAt(Farge colour, int number) throws RemoteException {
		cards.put(colour, cards.get(colour) - number);
	}
}