package ttr.oppdrag;

import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.spelar.IPlayer;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class MissionHandler {
	private final ArrayList<Mission> remainingMissions;

	public MissionHandler(ArrayList<Mission> missions){
		remainingMissions = missions;
		reshuffleMissions();
	}

	public void removeChosenMissionFromDeck(Mission mission) { //TODO flytt vidare inn i oppdragshandsamar?
		remainingMissions.remove(mission);
	}

	public Mission trekkOppdragskort()  {
		if (remainingMissions.size() > 0) {
			return remainingMissions.remove(0);
			//System.out.println(trekt.getDestinasjonar().toArray()[1]);
		}
		return null;
	}

	private void reshuffleMissions() { //TODO er eigentleg denne god nok?
		for (int i = 0; i < remainingMissions.size(); i++) {
			Mission temp = remainingMissions.get(i);
			int rand = (int) (Math.random() * remainingMissions.size());
			remainingMissions.set(i, remainingMissions.get(rand));
			remainingMissions.set(rand, temp);
		}
	}

	public static void trekkOppdrag(GUI gui, IPlayer player, boolean start) throws RemoteException {
		int numberOfMissionsToPickFrom = start ? Konstantar.ANTAL_STARTOPPDRAG : Konstantar.ANTAL_VELJEOPPDRAG;

		ArrayList<Mission> missions = chooseMissions(gui, numberOfMissionsToPickFrom, getMissionsToChooseFrom(player, numberOfMissionsToPickFrom));

		for (Mission mission : missions) {
			player.receiveMission(mission);
		}
	}

	private static ArrayList<Mission> getMissionsToChooseFrom(IPlayer player, int numberOfMissionsToPickFrom) throws RemoteException {
		ArrayList<Mission> missions = new ArrayList<>();
		for (int i = 0; i < numberOfMissionsToPickFrom; i++) {
			missions.add(player.trekkOppdragskort());
		}
		return missions;
	}

	private static ArrayList<Mission> chooseMissions(GUI gui, int numberOfMissionsToPickFrom, ArrayList<Mission> missions) {
		ArrayList<Mission> chosenMissions = new ArrayList<>();
		while (chosenMissions.size() < numberOfMissionsToPickFrom-2) {
			chosenMissions = gui.chooseMissions(missions);
		}
		return chosenMissions;
	}
}