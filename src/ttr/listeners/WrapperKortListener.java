package ttr.listeners;

import ttr.data.Farge;
import ttr.kjerna.Core;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class WrapperKortListener implements ActionListener{
	private final JButton kortBunke;
	private final JButton[] kortButtons;
	private final Core hovud;
	private final JFrame frame;
	public WrapperKortListener(JButton kortBunke, JButton[] kortButtons, Core hovud, JFrame frame){
		this.kortBunke = kortBunke;
		this.kortButtons = kortButtons;
		this.hovud = hovud;
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			buttonPressed(arg0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void buttonPressed(ActionEvent arg0) throws RemoteException {
		if (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) {
			JOptionPane.showMessageDialog(frame, "Det er ikkje din tur!");
			return;
			
		}
		if (arg0.getSource() == kortBunke) {
			new CardDeckHandler(hovud);
		}
		else if (arg0.getSource() == kortButtons[0]) {
			createButtonForRetrievingCardFromTable(0);
		}
		else if (arg0.getSource() == kortButtons[1]) {
			createButtonForRetrievingCardFromTable(1);
		}
		else if (arg0.getSource() == kortButtons[2]) {
			createButtonForRetrievingCardFromTable(2);
		}
		else if (arg0.getSource() == kortButtons[3]) {
			createButtonForRetrievingCardFromTable(3);
		}
		else if (arg0.getSource() == kortButtons[4]) {
			createButtonForRetrievingCardFromTable(4);
		}
	}

	private void createButtonForRetrievingCardFromTable(int positionOnTable) {
		try {
			retrieveOneCardFromTheTable(positionOnTable,hovud.getKvenSinTur());
			hovud.orientOtherPlayers(positionOnTable);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void retrieveOneCardFromTheTable(int positionOnTable,PlayerAndNetworkWTF kvenSinTur) throws RemoteException {
		Farge colour = hovud.getTable().getCardFromTable(positionOnTable);
		if (colour == null) { return; }
		if (kvenSinTur.hasAlreadyDrawnOneCard()) {
			retrieveSecondCard(positionOnTable, kvenSinTur, colour);
		}
		else {
			retrieveFirstCard(positionOnTable, kvenSinTur, colour);
		}
	}

	private void retrieveSecondCard(int positionOnTable, PlayerAndNetworkWTF kvenSinTur, Farge colour) throws RemoteException {
		if (colour == Farge.valfri) {
			JOptionPane.showMessageDialog(frame, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
			return;
		}
		kvenSinTur.receiveCard(colour);
		putRandomCardFromTheDeckOnTable(positionOnTable, colour);
		hovud.nesteSpelar();
	}

	private void retrieveFirstCard(int positionOnTable, PlayerAndNetworkWTF kvenSinTur, Farge colour) throws RemoteException {
		kvenSinTur.receiveCard(colour);
		putRandomCardFromTheDeckOnTable(positionOnTable, colour);
		if (colour == Farge.valfri) {
			hovud.nesteSpelar();
		}
		kvenSinTur.setEittKortTrektInn(true);
	}

	private void putRandomCardFromTheDeckOnTable(int positionOnTable, Farge colour) throws RemoteException {
		hovud.sendMessageAboutCard(true,false,colour);
		hovud.getTable().getRandomCardFromTheDeckAndPutOnTable(positionOnTable);
	}
}