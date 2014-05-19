package ttr.spelar;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Korthandsamar extends UnicastRemoteObject implements IKorthandsamar { //TODO refaktoriser veldig mykje av denne...
	private static final long serialVersionUID = 3899317463384337994L;
	private IHovud hovud;
	private HashMap<Farge, Integer> cardz;

	Korthandsamar(IHovud hovud) throws RemoteException {
		super();
		this.hovud = hovud;
		faaInitielleFargekort();
	}

	private void faaInitielleFargekort() {
		initialiseCards();
		for (int startkortPosisjon = 0; startkortPosisjon < Konstantar.ANTAL_STARTKORT; startkortPosisjon++) {
			Farge trekt = trekkFargekort();
			cardz.put(trekt, cardz.get(trekt)+1);
		}
	}

	private void initialiseCards() {        
		cardz = new HashMap<>();
		for (Farge colour : Farge.values()) {
			cardz.put(colour, 0);
		}
	}

	public Farge getRandomCardFromTheDeck(int positionOnTable) {
		Farge colourOfTheRandomCard = hovud.getBord().getRandomCardFromTheDeckAndPutOnTable(positionOnTable, true);

		if (colourOfTheRandomCard == null){
			displayGraphicallyThatThereIsNoCardHere(positionOnTable);
			return null;
		}

		hovud.getBord().setEinPaaBordet(colourOfTheRandomCard, positionOnTable);
		return colourOfTheRandomCard;
	}

	private void displayGraphicallyThatThereIsNoCardHere(int positionOnTable) {
		JOptionPane.showMessageDialog((Component) hovud.getGui(), "Det er ikkje noko kort der, ser du vel.");
		hovud.getGui().getKortButtons()[positionOnTable].setBackground(Color.GRAY);
		hovud.getGui().getKortButtons()[positionOnTable].setText("Tom");
	}

	public void receiveCard(Farge colour) {
		cardz.put(colour, cardz.get(colour) + 1);
	}

	/** @return eit tilfeldig fargekort frå toppen av stokken */
	public Farge trekkFargekort() {
		if (hovud.getBord().areThereAnyCardsLeftInDeck()) {
			return hovud.getBord().getRandomCardFromTheDeckAndPutOnTable(0, false);
		}
		return null;
	}

	public int getNumberOfCardsLeftInColour(Farge colour) throws RemoteException {
		return cardz.get(colour);
	}

	public int getNumberOfRemainingJokers() throws RemoteException {
		return cardz.get(Farge.valfri);
	}

	public void decrementCardsAt(Farge colour, int number) throws RemoteException {
		cardz.put(colour, cardz.get(colour) - number);
	}
}