package ttr.oppdrag.listeners;

import ttr.gui.IGUI;
import ttr.kjerna.IHovud;
import ttr.oppdrag.IOppdrag;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.rmi.RemoteException;

public class ShowMyMissionsHandler {

	public ShowMyMissionsHandler(IHovud hovud, IGUI gui) throws RemoteException {
		JPanel missionJPanel = new JPanel();

		ISpelar player = getPlayer(hovud);
		String missionString = player.getNamn() +": ";

		for (int i = 0; i < player.getAntalOppdrag(); i++) {
			IOppdrag mission = player.getOppdrag().get(i);
			missionString += prepareMissionString(player, i, mission);
		}
		
		showToPlayer(gui, missionJPanel, player, missionString);
	}

	private ISpelar getPlayer(IHovud hovud) {
		if (hovud.isNett()) {
			return hovud.getMinSpelar();
		}
		return hovud.getKvenSinTur();
	}

	private String prepareMissionString(ISpelar player,int i, IOppdrag mission) throws RemoteException {
		String missionString = mission.toString();
		if (player.erOppdragFerdig(mission.getOppdragsid())){
			missionString += " (OK)";
		}
		if (i == player.getAntalOppdrag()-1){
			missionString += ".";
		}
		else{
			missionString += ", ";
		}
		return missionString;
	}

	private void showToPlayer(IGUI gui, JPanel missionJPanel, ISpelar player, String missionString) throws RemoteException {
		JLabel missionJLabel = new JLabel(missionString);
		missionJPanel.add(missionJLabel);
		gui.lagRamme("Viser oppdraga til " +player.getNamn(), missionJPanel);
	}
}
