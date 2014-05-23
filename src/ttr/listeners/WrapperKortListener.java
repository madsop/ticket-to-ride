package ttr.listeners;

import ttr.data.Farge;
import ttr.data.Konstantar;
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
	private final boolean nett;

	public WrapperKortListener(JButton kortBunke, JButton[] kortButtons, Core hovud, JFrame frame, boolean nett){
		this.kortBunke = kortBunke;
		this.kortButtons = kortButtons;
		this.hovud = hovud;
		this.frame = frame;
		this.nett = nett;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			buttonPressed(arg0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void buttonPressed(ActionEvent arg0) throws RemoteException {
		if (nett && (!hovud.getMinSpelar().getNamn().equals(hovud.getKvenSinTur().getNamn())) ) {
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

			PlayerAndNetworkWTF host = orienterAndreSpelarar(positionOnTable);

			if (nett && host != null){
				Farge newColour = host.getRandomCardFromTheDeck(positionOnTable);
				while (host.areThereTooManyJokersOnTable()) {
					newColour = placeNewCardsOnTable(host);
				}
				hovud.newCardPlacedOnTableInNetworkGame(host, newColour, positionOnTable);
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private Farge placeNewCardsOnTable(PlayerAndNetworkWTF host) throws RemoteException {
		Farge newColour = null;
		host.leggUtFem();
		int[] cardsOnTableAsIntegers = host.getPaaBordetInt();

		for (int plass = 0; plass < hovud.getTable().getPaaBordet().length; plass++){
			newColour = Konstantar.FARGAR[cardsOnTableAsIntegers[plass]];
			hovud.getMinSpelar().putCardOnTable(newColour,plass);
			hovud.newCardPlacedOnTableInNetworkGame(host, newColour, plass);
		}
		return newColour;
	}

	private void retrieveOneCardFromTheTable(int positionOnTable,PlayerAndNetworkWTF kvenSinTur) throws RemoteException {
		Farge colour = hovud.getTable().getCardFromTable(positionOnTable);
		if (colour == null) { return; }
		if (kvenSinTur.getValdAllereie()) {
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

	private PlayerAndNetworkWTF orienterAndreSpelarar(int positionOnTable) throws  RemoteException{
		PlayerAndNetworkWTF host = null;
		if (nett && hovud.getMinSpelar().getSpelarNummer()==0) {
			host = hovud.getMinSpelar(); // TODO forsvinn ikkje denne uansett i løpet av for-løkka under?
		}
		for (PlayerAndNetworkWTF player : hovud.getSpelarar()) {
			if (!nett){
				player.getRandomCardFromTheDeck(positionOnTable);
			}
			else {
				if (player.getSpelarNummer()==0) {
					host = player;
				}
			}
		}
		return host;
	}
}