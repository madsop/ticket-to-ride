package ttr.Listeners;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class WrapperKortListener implements ActionListener{
	private final JButton kortBunke;
	private final JButton[] kortButtons;
	private final IHovud hovud;
	private final JFrame frame;
	private final boolean nett;

	public WrapperKortListener(JButton kortBunke, JButton[] kortButtons, IHovud hovud, JFrame frame, boolean nett){
		this.kortBunke = kortBunke;
		this.kortButtons = kortButtons;
		this.hovud = hovud;
		this.frame = frame;
		this.nett = nett;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == kortBunke) {
			try {
				new CardDeckHandler(hovud);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
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

			ISpelar host = orienterAndreSpelarar(positionOnTable);

			if (nett && host != null){
				Farge newColour = host.getTilfeldigKortFråBordet(positionOnTable);
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

	private Farge placeNewCardsOnTable(ISpelar host) throws RemoteException {
		Farge newColour = null;
		host.leggUtFem();
		int[] cardsOnTableAsIntegers = host.getPaaBordetInt();

		for (int plass = 0; plass < hovud.getBord().getPaaBordet().length; plass++){
			newColour = Konstantar.FARGAR[cardsOnTableAsIntegers[plass]];
			hovud.getMinSpelar().putCardOnTable(newColour,plass);
			hovud.newCardPlacedOnTableInNetworkGame(host, newColour, plass);
		}
		return newColour;
	}

	private void retrieveOneCardFromTheTable(int positionOnTable,ISpelar kvenSinTur) throws RemoteException {
		Farge colour = hovud.getBord().getPaaBordet()[positionOnTable];
		if (kvenSinTur.getValdAllereie()) {
			if (colour == Farge.valfri) {
				JOptionPane.showMessageDialog(frame, "Haha. Nice try. Du kan ikkje ta ein joker frå bordet når du allereie har trekt inn eitt kort");
				return;
			}
			else if (colour==null){return;}
			else {
				kvenSinTur.receiveCard(colour);
				hovud.sendKortMelding(true,false,colour);
				hovud.getBord().getRandomCardFromTheDeck(positionOnTable, true);
				hovud.nesteSpelar();
			}
		}
		else {
			if (colour==null){return;}
			hovud.getKvenSinTur().receiveCard(colour);
			hovud.sendKortMelding(true,false,colour);
			hovud.getBord().getRandomCardFromTheDeck(positionOnTable, true);
			if (colour == Farge.valfri) {
				hovud.nesteSpelar();
			}
			hovud.getKvenSinTur().setEittKortTrektInn(true);
		}
	}

	private ISpelar orienterAndreSpelarar(int positionOnTable) throws  RemoteException{
		ISpelar host = null;
		if (nett){
			if (hovud.getMinSpelar().getSpelarNummer()==0) {
				host = hovud.getMinSpelar();
			}
		}
		for (ISpelar s : hovud.getSpelarar()) {
			if (!nett){
				s.getTilfeldigKortFråBordet(positionOnTable);
			}
			else {
				if (s.getSpelarNummer()==0) {
					host = s;
				}
			}
		}
		return host;
	}
}