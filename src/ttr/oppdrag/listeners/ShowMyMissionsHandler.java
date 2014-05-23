package ttr.oppdrag.listeners;

import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.oppdrag.Mission;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;
import java.rmi.RemoteException;

public class ShowMyMissionsHandler {
	public ShowMyMissionsHandler(IHovud hovud, IGUI gui) throws RemoteException {
		JPanel missionJPanel = new JPanel();

		PlayerAndNetworkWTF player = getPlayer(hovud);
		String missionString = player.getNamn() +": ";

		
		for (Mission mission : player.getOppdrag()) {
			missionString += prepareMissionString(player, mission);
		}
		missionString = missionString.substring(0, missionString.length() - 2) + ".";		
		showToPlayer(gui, missionJPanel, player, missionString);
	}

	private PlayerAndNetworkWTF getPlayer(IHovud hovud) {
		if (hovud.isNetworkGame()) {
			return hovud.getMinSpelar();
		}
		return hovud.getKvenSinTur();
	}

	private String prepareMissionString(PlayerAndNetworkWTF player, Mission mission) throws RemoteException {
		String missionString = mission.toString();
		if (player.isMissionAccomplished(mission)){
			missionString += " (OK)";
		}
		missionString += ", ";
		return missionString;
	}

	private void showToPlayer(IGUI gui, JPanel missionJPanel, PlayerAndNetworkWTF player, String missionString) throws RemoteException {
		JLabel missionJLabel = new JLabel(missionString);
		missionJPanel.add(missionJLabel);
		gui.createJFrame("Viser oppdraga til " +player.getNamn(), missionJPanel);
	}
}
