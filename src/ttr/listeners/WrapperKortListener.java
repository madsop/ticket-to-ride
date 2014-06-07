package ttr.listeners;

import ttr.kjerna.Core;
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
			if (arg0.getSource() == cardButtons[i]) { core.createButtonForRetrievingCardFromTable(i); }
		}
	}
}