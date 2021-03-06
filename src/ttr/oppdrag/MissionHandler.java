package ttr.oppdrag;

import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.gui.MissionChooserViewController;
import ttr.spelar.IPlayer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

public class MissionHandler {
	private final ArrayList<Mission> remainingMissions;
	private MissionChooserViewController missionChooserViewController;

	public MissionHandler(ArrayList<Mission> missions, MissionChooserViewController missionChooserViewController) {
		remainingMissions = missions;
		this.missionChooserViewController = missionChooserViewController;
		reshuffleMissions();
	}

	public void removeChosenMissionFromDeck(Mission mission) {
		remainingMissions.remove(mission);
	}

	public Mission trekkOppdragskort()  {
		if (remainingMissions.size() > 0) {
			return remainingMissions.remove(0);
		}
		return null;
	}

	public void trekkOppdrag(GUI gui, IPlayer player, boolean start) throws RemoteException {
		int numberOfMissionsToPickFrom = start ? Konstantar.ANTAL_STARTOPPDRAG : Konstantar.ANTAL_VELJEOPPDRAG;

		Collection<Mission> missions = chooseMissions(gui, numberOfMissionsToPickFrom, getMissionsToChooseFrom(player, numberOfMissionsToPickFrom));

		for (Mission mission : missions) {
			player.receiveMission(mission);
		}
	}

	private void reshuffleMissions() { //TODO er eigentleg denne god nok?
		for (int i = 0; i < remainingMissions.size(); i++) {
			Mission temp = remainingMissions.get(i);
			int rand = (int) (Math.random() * remainingMissions.size());
			remainingMissions.set(i, remainingMissions.get(rand));
			remainingMissions.set(rand, temp);
		}
	}

	private ArrayList<Mission> getMissionsToChooseFrom(IPlayer player, int numberOfMissionsToPickFrom) throws RemoteException {
		ArrayList<Mission> missions = new ArrayList<>();
		for (int i = 0; i < numberOfMissionsToPickFrom; i++) {
			missions.add(player.trekkOppdragskort());
		}
		return missions;
	}

	private Collection<Mission> chooseMissions(GUI gui, int numberOfMissionsToPickFrom, ArrayList<Mission> missions) {
		Collection<Mission> chosenMissions = new ArrayList<>();
		while (chosenMissions.size() < numberOfMissionsToPickFrom-2) {
			chosenMissions = missionChooserViewController.setUpMissionChooser(missions);
		}
		return chosenMissions;
	}
}