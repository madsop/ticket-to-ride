package ttr.listeners;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

class visMineKortHandler {
	public visMineKortHandler(IHovud hovud, IGUI gui) throws RemoteException{
		// vis korta mine
		JPanel myCardsPanel = new JPanel();

		ISpelar visSine;
		if (hovud.isNett()) {
			visSine = hovud.getMinSpelar();
		}
		else {
			visSine = hovud.getKvenSinTur();
		}
		String[] kort = new String[Konstantar.ANTAL_FARGAR];

		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
			kort[i] = Konstantar.FARGAR[i] +": " +visSine.getNumberOfCardsLeftInColour(Konstantar.FARGAR[i]);
		}
		JLabel[] cardsJLabel = new JLabel[kort.length];

		myCardsPanel.add(new JLabel(visSine.getNamn()));

		for (int i = 0; i < cardsJLabel.length; i++) {
			cardsJLabel[i] = new JLabel();
			cardsJLabel[i].setText(kort[i]);
			if (visSine.getNumberOfCardsLeftInColour(Konstantar.FARGAR[i]) != 0) {
				cardsJLabel[i].setForeground(Konstantar.fargeTilColor(Konstantar.FARGAR[i]));
			}
			else {
				cardsJLabel[i].setForeground(Color.LIGHT_GRAY);
			}

			myCardsPanel.add(cardsJLabel[i]);
		}
		gui.lagRamme("Viser korta til " +visSine.getNamn(), myCardsPanel);
	}
}
