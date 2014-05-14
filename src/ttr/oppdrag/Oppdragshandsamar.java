package ttr.oppdrag;

import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.spelar.ISpelar;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Oppdragshandsamar implements IOppdragshandsamar {
    private final ArrayList<IOppdrag> remainingMissions;

    public Oppdragshandsamar(ArrayList<IOppdrag> missions){
        remainingMissions = missions;
        reshuffleMissions();
    }

    @Override
    public ArrayList<IOppdrag> getRemainingMissions(){
        return remainingMissions;
    }

    @Override
    public int getNumberOfRemainingMissions() {
        return remainingMissions.size();
    }

    @Override
    public IOppdrag getMissionAndRemoveItFromDeck() {
        IOppdrag IOppdrag = remainingMissions.get(0);
        remainingMissions.remove(0);
        return IOppdrag;
    }

    private void reshuffleMissions() {
        for (int i = 0; i < remainingMissions.size(); i++) {
            IOppdrag temp = remainingMissions.get(i);
            int rand = (int) (Math.random() * remainingMissions.size());
            remainingMissions.set(i, remainingMissions.get(rand));
            remainingMissions.set(rand, temp);
        }
    }

    public static void trekkOppdrag(IGUI gui, ISpelar player, boolean start) throws RemoteException {
        int numberOfMissionsToPickFrom = start ? Konstantar.ANTAL_STARTOPPDRAG : Konstantar.ANTAL_VELJEOPPDRAG;

        ArrayList<IOppdrag> missions = chooseMissions(gui, numberOfMissionsToPickFrom, getMissionsToChooseFrom(player, numberOfMissionsToPickFrom));

        for (IOppdrag mission : missions) {
            player.faaOppdrag(mission);
        }
    }

	private static ArrayList<IOppdrag> getMissionsToChooseFrom(ISpelar player, int numberOfMissionsToPickFrom) throws RemoteException {
		ArrayList<IOppdrag> missions = new ArrayList<>();
        for (int i = 0; i < numberOfMissionsToPickFrom; i++) {
            missions.add(player.trekkOppdragskort());
        }
		return missions;
	}

	private static ArrayList<IOppdrag> chooseMissions(IGUI gui, int numberOfMissionsToPickFrom, ArrayList<IOppdrag> missions) {
		ArrayList<IOppdrag> chosenMissions = new ArrayList<>();
        while (chosenMissions.size() < numberOfMissionsToPickFrom-2){
            chosenMissions = gui.velOppdrag(missions);
        }
		return chosenMissions;
	}
}