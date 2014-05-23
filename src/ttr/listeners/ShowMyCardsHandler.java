package ttr.listeners;

import ttr.data.Konstantar;
import ttr.gui.SwingUtils;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

class ShowMyCardsHandler {
	public ShowMyCardsHandler(PlayerAndNetworkWTF playerWhoseCardToShow) throws RemoteException{
		JPanel myCardsPanel = new JPanel();
		String[] kort = setUpCardText(playerWhoseCardToShow);
		JLabel[] cardsJLabel = new JLabel[kort.length];

		myCardsPanel.add(new JLabel(playerWhoseCardToShow.getNamn()));

		for (int i = 0; i < cardsJLabel.length; i++) {
			cardsJLabel[i] = setUpCardsJLabel(playerWhoseCardToShow, kort, i);
			myCardsPanel.add(cardsJLabel[i]);
		}
		
		SwingUtils.createJFrame("Viser korta til " +playerWhoseCardToShow.getNamn(), myCardsPanel);
	}

	private String[] setUpCardText(PlayerAndNetworkWTF visSine) throws RemoteException {
		String[] kort = new String[Konstantar.FARGAR.length];
		for (int i = 0; i < Konstantar.FARGAR.length; i++) {
			kort[i] = Konstantar.FARGAR[i] +": " +visSine.getNumberOfCardsLeftInColour(Konstantar.FARGAR[i]);
		}
		return kort;
	}

	private JLabel setUpCardsJLabel(PlayerAndNetworkWTF visSine, String[] kort, int i) throws RemoteException {
		JLabel label = new JLabel();
		label.setText(kort[i]);
		if (visSine.getNumberOfCardsLeftInColour(Konstantar.FARGAR[i]) > 0) {
			label.setForeground(Konstantar.fargeTilColor(Konstantar.FARGAR[i]));
		}
		else {
			label.setForeground(Color.LIGHT_GRAY);
		}
		return label;
	}
}