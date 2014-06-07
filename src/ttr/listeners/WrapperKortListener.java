package ttr.listeners;

import ttr.data.Colour;
import ttr.kjerna.Core;
import ttr.spelar.IPlayer;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class WrapperKortListener implements ActionListener {
	private final JButton cardDeckButton;
	private final JButton[] cardButtons;
	private final Core core;
	
	public WrapperKortListener(JButton kortBunke, JButton[] kortButtons, Core hovud) {
		this.cardDeckButton = kortBunke;
		this.cardButtons = kortButtons;
		this.core = hovud;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			buttonClicked(arg0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void buttonClicked(ActionEvent arg0) throws RemoteException {
		if (!core.getMinSpelar().getNamn().equals(core.getKvenSinTur().getNamn())) {
			JOptionPane.showMessageDialog(null, "Det er ikkje din tur!");
			return;
		}
		if (arg0.getSource() == cardDeckButton) { new CardDeckHandler(core); }
		for (int i = 0; i < cardButtons.length; i++) {
			if (arg0.getSource() == cardButtons[i]) { createButtonForRetrievingCardFromTable(i); }
		}
	}

	private void createButtonForRetrievingCardFromTable(int positionOnTable) throws RemoteException {
		retrieveOneCardFromTheTable(positionOnTable,core.findPlayerInAction());
		core.orientOtherPlayers(positionOnTable);
	}

	private void retrieveOneCardFromTheTable(int positionOnTable, IPlayer kvenSinTur) throws RemoteException {
		Colour colour = core.getTable().getCardFromTable(positionOnTable);
		if (colour == null) { return; }
		if (!kvenSinTur.hasAlreadyDrawnOneCard()) {
			retrieveFirstCard(positionOnTable, kvenSinTur, colour);
		}
		else {
			retrieveSecondCard(positionOnTable, kvenSinTur, colour);
		}
	}

	private void retrieveFirstCard(int positionOnTable, IPlayer kvenSinTur, Colour colour) throws RemoteException {
		kvenSinTur.receiveCard(colour);
		putRandomCardFromTheDeckOnTable(positionOnTable, colour);
		if (colour == Colour.valfri) {
			core.nesteSpelar();
		}
		kvenSinTur.setEittKortTrektInn(true);
	}

	private void retrieveSecondCard(int positionOnTable, IPlayer kvenSinTur, Colour colour) throws RemoteException {
		if (colour == Colour.valfri) {
			JOptionPane.showMessageDialog(null, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
			return;
		}
		kvenSinTur.receiveCard(colour);
		putRandomCardFromTheDeckOnTable(positionOnTable, colour);
		core.nesteSpelar();
	}

	private void putRandomCardFromTheDeckOnTable(int positionOnTable, Colour colour) throws RemoteException {
		core.sendMessageAboutCard(true,false,colour);
		core.getTable().getRandomCardFromTheDeckAndPutOnTable(positionOnTable);
	}
}