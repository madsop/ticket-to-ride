package ttr.listeners;

import ttr.kjerna.Core;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public abstract class DelegationListener implements ActionListener {
	protected final Core core;
	
	public DelegationListener(Core core) {
		this.core = core;
	}

	public boolean allowedToContinue() throws RemoteException {
		if (!core.getMinSpelar().getNamn().equals(core.getKvenSinTur().getNamn())) {
			JOptionPane.showMessageDialog(null, "Det er ikkje din tur!");
			return false;
		}
		if (core.findPlayerInAction().hasAlreadyDrawnOneCard()) {
			JOptionPane.showMessageDialog(null, "Du har allereie trekt eitt kort. Da kan du ikkje bygge eller trekke oppdrag, du må trekke eitt kort til.");
			return false;
		}
		return true;
	}
	
	protected abstract void specific() throws RemoteException;

	public void actionPerformed(ActionEvent arg0) {
		try {
			if (!allowedToContinue()) { return; }
			specific();
		}
		catch (RemoteException e2) {
			e2.printStackTrace();
		}
	}
}