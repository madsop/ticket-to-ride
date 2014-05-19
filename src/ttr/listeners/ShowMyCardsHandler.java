package ttr.listeners;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

class ShowMyCardsHandler {
	public ShowMyCardsHandler(IHovud hovud, IGUI gui) throws RemoteException{
		JPanel myCardsPanel = new JPanel();
		ISpelar visSine = findPlayer(hovud);
		String[] kort = setUpCardText(visSine);
		JLabel[] cardsJLabel = new JLabel[kort.length];

		myCardsPanel.add(new JLabel(visSine.getNamn()));

		for (int i = 0; i < cardsJLabel.length; i++) {
			cardsJLabel[i] = setUpCardsJLabel(visSine, kort, i);
			myCardsPanel.add(cardsJLabel[i]);
		}
		
		gui.lagRamme("Viser korta til " +visSine.getNamn(), myCardsPanel);
	}

	private ISpelar findPlayer(IHovud hovud) {
		if (hovud.isNett()) {
			return hovud.getMinSpelar();
		}
		return hovud.getKvenSinTur();
	}

	private String[] setUpCardText(ISpelar visSine) throws RemoteException {
		String[] kort = new String[Konstantar.ANTAL_FARGAR];
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
			kort[i] = Konstantar.FARGAR[i] +": " +visSine.getNumberOfCardsLeftInColour(Konstantar.FARGAR[i]);
		}
		return kort;
	}

	private JLabel setUpCardsJLabel(ISpelar visSine, String[] kort, int i) throws RemoteException {
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