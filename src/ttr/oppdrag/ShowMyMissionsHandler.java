package ttr.oppdrag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import ttr.gui.SwingUtils;
import ttr.kjerna.Core;
import ttr.spelar.IPlayer;

import javax.swing.*;

public class ShowMyMissionsHandler implements ActionListener {
	private Core core;

	public ShowMyMissionsHandler(Core core) {
		this.core = core;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			IPlayer player = core.findPlayerInAction();
			JPanel missionJPanel = new JPanel();

			String missionString = player.getNamn() +": ";

			for (Mission mission : player.getOppdrag()) {
				missionString += prepareMissionString(player, mission);
			}

			missionString = missionString.substring(0, missionString.length() - 2) + ".";		
			showToPlayer(missionJPanel, player, missionString);
		} catch (RemoteException re) {}
	}

	private String prepareMissionString(IPlayer player, Mission mission) throws RemoteException {
		String missionString = mission.toString();
		if (player.isMissionAccomplished(mission)){
			missionString += " (OK)";
		}
		missionString += ", ";
		return missionString;
	}

	private void showToPlayer(JPanel missionJPanel, IPlayer player, String missionString) throws RemoteException {
		JLabel missionJLabel = new JLabel(missionString);
		missionJPanel.add(missionJLabel);
		SwingUtils.createJFrame("Viser oppdraga til " +player.getNamn(), missionJPanel);
	}
}
