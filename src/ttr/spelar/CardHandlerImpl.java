package ttr.spelar;

import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.kjerna.Core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class CardHandlerImpl extends UnicastRemoteObject implements CardHandler {
	private static final long serialVersionUID = 3899317463384337994L;
	private Core hovud;
	private Map<Colour, Integer> cards;

	CardHandlerImpl(Core hovud) throws RemoteException {
		super();
		this.hovud = hovud;
		faaInitielleFargekort();
	}

	private void initialiseCards() {
		cards = new HashMap<>();
		for (Colour colour : Colour.values()) {
			cards.put(colour, 0);
		}
	}

	private void faaInitielleFargekort() {
		initialiseCards();
		for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
			Colour trekt = drawRandomCardFromTheDeck();
			cards.put(trekt, cards.get(trekt)+1); //todo denne feilar som berre pokker..hmm
		}
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

	public int getNumberOfCardsLeftInColour(Colour colour) throws RemoteException {
		return cards.get(colour);
	}

	public void decrementCardsAt(Colour colour, int number) throws RemoteException {
		cards.put(colour, cards.get(colour) - number);
	}
}