package ttr.listeners;

import ttr.kjerna.Core;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class WrapperKortListener implements ActionListener {
	private final Core core;
	private final int positionOnTable;
	
	public WrapperKortListener(Core hovud, int positionOnTable) {
		this.core = hovud;
		this.positionOnTable = positionOnTable;
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
		core.createButtonForRetrievingCardFromTable(positionOnTable);
	}
}