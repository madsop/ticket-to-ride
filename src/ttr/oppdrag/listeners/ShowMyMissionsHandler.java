package ttr.oppdrag.listeners;

import ttr.gui.SwingUtils;
import ttr.oppdrag.Mission;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;

public class ShowMyMissionsHandler {
	public ShowMyMissionsHandler(PlayerAndNetworkWTF player) {
		JPanel missionJPanel = new JPanel();

		String missionString = player.getNamn() +": ";

		
		for (Mission mission : player.getOppdrag()) {
			missionString += prepareMissionString(player, mission);
		}
		missionString = missionString.substring(0, missionString.length() - 2) + ".";		
		showToPlayer(missionJPanel, player, missionString);
	}

	private String prepareMissionString(PlayerAndNetworkWTF player, Mission mission) {
		String missionString = mission.toString();
		if (player.isMissionAccomplished(mission)){
			missionString += " (OK)";
		}
		missionString += ", ";
		return missionString;
	}

	private void showToPlayer(JPanel missionJPanel, PlayerAndNetworkWTF player, String missionString) {
		JLabel missionJLabel = new JLabel(missionString);
		missionJPanel.add(missionJLabel);
		SwingUtils.createJFrame("Viser oppdraga til " +player.getNamn(), missionJPanel);
	}
}
