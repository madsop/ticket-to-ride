package ttr.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import ttr.data.Colour;
import ttr.kjerna.Core;
import ttr.spelar.IPlayer;

public class CardDeckHandler implements ActionListener {
	private Core hovud;

	public CardDeckHandler(Core hovud) {
		this.hovud = hovud;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) {
				JOptionPane.showMessageDialog(null, "Det er ikkje din tur!");
				return;
			}
			IPlayer playerInTurn = hovud.findPlayerInAction();
			Colour colour = playerInTurn.trekkFargekort();
			if (colour==null){ return; }

			playerInTurn.receiveCard(colour);
			hovud.sendMessageAboutCard(true,true,colour);

			proceedGame(hovud, playerInTurn);
		}
		catch (RemoteException re) {}
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