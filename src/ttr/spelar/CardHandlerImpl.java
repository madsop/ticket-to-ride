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
		initialiseCards();
		faaInitielleFargekort();
	}

	private void initialiseCards() {
		cards = new HashMap<>();
		for (Farge colour : Farge.values()) {
			cards.put(colour, 0);
		}
	}

	private void faaInitielleFargekort() {
		for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
			Farge trekt = drawRandomCardFromTheDeck();
			cards.put(trekt, cards.get(trekt)+1);
		}
	}

	public Farge getRandomCardFromTheDeck(int positionOnTable) {
		Farge colourOfTheRandomCard = hovud.getTable().getRandomCardFromTheDeckAndPutOnTable(positionOnTable);

		if (colourOfTheRandomCard == null){
			hovud.displayGraphicallyThatThereIsNoCardHere(positionOnTable);
			return null;
		}

		hovud.getTable().setEinPaaBordet(colourOfTheRandomCard, positionOnTable);
		return colourOfTheRandomCard;
	}

	public void receiveCard(Farge colour) {
		cards.put(colour, cards.get(colour) + 1);
	}

	public Farge drawRandomCardFromTheDeck() {
		return hovud.getTable().getRandomCardFromTheDeck(0);
	}

	public int getNumberOfCardsLeftInColour(Farge colour) throws RemoteException {
		return cards.get(colour);
	}

	public void decrementCardsAt(Farge colour, int number) throws RemoteException {
		cards.put(colour, cards.get(colour) - number);
	}
}