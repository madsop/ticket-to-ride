package ttr.listeners;

import ttr.data.Farge;
import ttr.kjerna.IHovud;
import ttr.spelar.PlayerAndNetworkWTF;

import java.rmi.RemoteException;

class CardDeckHandler {
	public CardDeckHandler(IHovud hovud) throws RemoteException {
		PlayerAndNetworkWTF playerInTurn = hovud.getKvenSinTur();
		Farge colour = playerInTurn.trekkFargekort();
		if (colour==null){ return; }

		playerInTurn.receiveCard(colour);
		hovud.sendMessageAboutCard(true,true,colour);
		
		proceedGame(hovud, playerInTurn);
	}

	private void proceedGame(IHovud hovud, PlayerAndNetworkWTF playerInTurn) throws RemoteException {
		if (playerInTurn.getValdAllereie()) {
			hovud.nesteSpelar();
		}
		else {
			playerInTurn.setEittKortTrektInn(true);
		}
	}
}