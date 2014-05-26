package ttr.listeners;

import java.rmi.RemoteException;

import ttr.data.Colour;
import ttr.kjerna.Core;
import ttr.spelar.IPlayer;

class CardDeckHandler {
	public CardDeckHandler(Core hovud) throws RemoteException {
		IPlayer playerInTurn = hovud.findPlayerInAction();
		Colour colour = playerInTurn.trekkFargekort();
		if (colour==null){ return; }

		playerInTurn.receiveCard(colour);
		hovud.sendMessageAboutCard(true,true,colour);
		
		proceedGame(hovud, playerInTurn);
	}

	private void proceedGame(Core hovud, IPlayer playerInTurn) throws RemoteException {
		if (playerInTurn.hasAlreadyDrawnOneCard()) {
			hovud.nesteSpelar();
		}
		else {
			playerInTurn.setEittKortTrektInn(true);
		}
	}
}