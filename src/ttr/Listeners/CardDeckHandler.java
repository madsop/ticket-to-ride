package ttr.Listeners;

import ttr.data.Farge;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.rmi.RemoteException;

class CardDeckHandler {
	public CardDeckHandler(IHovud hovud) throws RemoteException {
		ISpelar playerInTurn = hovud.getKvenSinTur();
		Farge colour = playerInTurn.trekkFargekort();
		if (colour==null){ return; }

		playerInTurn.receiveCard(colour);
		hovud.sendKortMelding(true,true,colour);
		
		displayToUser(colour);

		if (playerInTurn.getValdAllereie()) {
			hovud.nesteSpelar();
		}
		else {
			playerInTurn.setEittKortTrektInn(true);
		}
	}

	private void displayToUser(Farge colour) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Du trakk eit kort av farge " +colour);
		panel.add(label);
	}
}