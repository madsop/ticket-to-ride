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
		JPanel korta = new JPanel();

		ISpelar visSine;
		if (hovud.isNett()) {
			visSine = hovud.getMinSpelar();
		}
		else {
			visSine = hovud.getKvenSinTur();
		}
		String[] kort = new String[Konstantar.ANTAL_FARGAR];

		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++) {
			kort[i] = Konstantar.FARGAR[i] +": " +visSine.getNumberOfCardsLeftInColour(i);
		}
		JLabel[] oppdr = new JLabel[kort.length];

		korta.add(new JLabel(visSine.getNamn()));

		for (int i = 0; i < oppdr.length; i++) {
			oppdr[i] = new JLabel();
			oppdr[i].setText(kort[i]);
			if (visSine.getNumberOfCardsLeftInColour(i) != 0) {
				oppdr[i].setForeground(Konstantar.fargeTilColor(Konstantar.FARGAR[i]));
			}
			else {
				oppdr[i].setForeground(Color.LIGHT_GRAY);
			}

			korta.add(oppdr[i]);
		}
		gui.lagRamme("Viser korta til " +visSine.getNamn(), korta);
	}
}
