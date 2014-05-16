package ttr.oppdrag;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Oppdragshandsamar implements IOppdragshandsamar {
    private final ArrayList<Mission> remainingMissions;

    public Oppdragshandsamar(ArrayList<Mission> missions){
        remainingMissions = missions;
        reshuffleMissions();
    }

    @Override
    public ArrayList<Mission> getRemainingMissions(){
        return remainingMissions;
    }

    @Override
    public int getNumberOfRemainingMissions() {
        return remainingMissions.size();
    }

    @Override
    public Mission getMissionAndRemoveItFromDeck() {
        Mission IOppdrag = remainingMissions.get(0);
        remainingMissions.remove(0);
        return IOppdrag;
    }

    private void reshuffleMissions() {
        for (int i = 0; i < remainingMissions.size(); i++) {
            Mission temp = remainingMissions.get(i);
            int rand = (int) (Math.random() * remainingMissions.size());
            remainingMissions.set(i, remainingMissions.get(rand));
            remainingMissions.set(rand, temp);
        }
    }

    public static void trekkOppdrag(IGUI gui, ISpelar player, boolean start) throws RemoteException {
        int numberOfMissionsToPickFrom = start ? Konstantar.ANTAL_STARTOPPDRAG : Konstantar.ANTAL_VELJEOPPDRAG;

        ArrayList<Mission> missions = chooseMissions(gui, numberOfMissionsToPickFrom, getMissionsToChooseFrom(player, numberOfMissionsToPickFrom));

        for (Mission mission : missions) {
            player.faaOppdrag(mission);
        }
    }

	private static ArrayList<Mission> getMissionsToChooseFrom(ISpelar player, int numberOfMissionsToPickFrom) throws RemoteException {
		ArrayList<Mission> missions = new ArrayList<>();
        for (int i = 0; i < numberOfMissionsToPickFrom; i++) {
            missions.add(player.trekkOppdragskort());
        }
		return missions;
	}

	private static ArrayList<Mission> chooseMissions(IGUI gui, int numberOfMissionsToPickFrom, ArrayList<Mission> missions) {
		ArrayList<Mission> chosenMissions = new ArrayList<>();
        while (chosenMissions.size() < numberOfMissionsToPickFrom-2) {
            chosenMissions = gui.velOppdrag(missions);
        }
		return chosenMissions;
	}
}