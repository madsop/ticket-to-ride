package ttr.oppdrag.listeners;

import java.rmi.RemoteException;

import ttr.gui.SwingUtils;
import ttr.oppdrag.Mission;
import ttr.spelar.IPlayer;
import javax.swing.*;

public class ShowMyMissionsHandler {
	public ShowMyMissionsHandler(IPlayer player) throws RemoteException {
		JPanel missionJPanel = new JPanel();

		String missionString = player.getNamn() +": ";

		
		for (Mission mission : player.getOppdrag()) {
			missionString += prepareMissionString(player, mission);
		}
		missionString = missionString.substring(0, missionString.length() - 2) + ".";		
		showToPlayer(missionJPanel, player, missionString);
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
