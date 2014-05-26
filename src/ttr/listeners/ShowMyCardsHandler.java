package ttr.listeners;

import ttr.data.Colour;
import ttr.data.Konstantar;
import ttr.gui.SwingUtils;
import ttr.spelar.IPlayer;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

class ShowMyCardsHandler {
	public ShowMyCardsHandler(IPlayer myPlayer) throws RemoteException {
		JPanel myCardsPanel = new JPanel();
		myCardsPanel.add(new JLabel(myPlayer.getNamn()));

		for (Colour colour : Konstantar.FARGAR) {
			myCardsPanel.add(setUpCardsJLabel(myPlayer, colour));
		}

		SwingUtils.createJFrame("Viser korta til " +myPlayer.getNamn(), myCardsPanel);
	}

	private JLabel setUpCardsJLabel(IPlayer visSine, Colour colour) throws RemoteException {
		JLabel label = new JLabel();
		label.setText(setUpCardText(visSine, colour));
		label.setForeground(getForegroundColour(visSine, colour));
		return label;
	}

	private Color getForegroundColour(IPlayer myPlayer, Colour colour) throws RemoteException {
		if (myPlayer.getNumberOfCardsLeftInColour(colour) > 0) {
			return Konstantar.fargeTilColor(colour);
		}
		return Color.LIGHT_GRAY;
	}

	private String setUpCardText(IPlayer myPlayer, Colour colour) throws RemoteException {
		return colour +": " +myPlayer.getNumberOfCardsLeftInColour(colour);
	}
}