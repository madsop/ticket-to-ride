package ttr.oppdrag;

import ttr.data.Destinasjon;
import ttr.rute.Route;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerMissionHandler extends UnicastRemoteObject implements Remote {
	private static final long serialVersionUID = 5194460142995578869L;
	private Set<Mission> missions;
	private Map<Destinasjon, Set<Destinasjon>> mapBetweenAandB;

	public PlayerMissionHandler() throws RemoteException {
		super();
		missions = new HashSet<>();
		initialiseConnectedRoutes();
	}

	public void retrieveMission(Mission mission) throws RemoteException{
		this.missions.add(mission);
	}

	public Collection<Mission> getMissions() throws RemoteException  {
		return missions;
	}
	
	public int getMissionPoints() throws RemoteException  { //TODO skill ut alt som har med å avslutte spelet i ei eiga klasse?
		int totalMissionValue = 0;
		for (Mission mission : missions) {
			if (isMissionAccomplished(mission)) {
				totalMissionValue += mission.getValue();
			} else {
				totalMissionValue -= mission.getValue();
			}
		}
		return totalMissionValue;
	}
	
	public boolean isMissionAccomplished(Mission mission) {
		return mapBetweenAandB.get(mission.getStart()).contains(mission.getEnd()) || mapBetweenAandB.get(mission.getEnd()).contains(mission.getStart());
	}

	public int getNumberOfFulfilledMissions() throws RemoteException{
		return (int) missions.stream().filter(x -> isMissionAccomplished(x)).count();
	}

	public void markRouteAsBuilt(Route route) throws RemoteException {
		// Sjekk for fullførde oppdrag?
		mapBetweenAandB.get(route.getStart()).add(route.getEnd());
		mapBetweenAandB.get(route.getEnd()).add(route.getStart());
		
		transitiveClosure();
	}

	private void initialiseConnectedRoutes() {
		mapBetweenAandB = new HashMap<>();
		for (int y = 0; y < Destinasjon.values().length; y++) {
			mapBetweenAandB.putIfAbsent(Destinasjon.values()[y], new HashSet<>());
		}
	}

	private void transitiveClosure() {
		// Dette er ein implementasjon av Warshallalgoritma, side 553 i Rosen [DiskMat]
		// Køyretida er på (u)behagelege 2n³, som er tilnærma likt optimalt
		for (Destinasjon d1 : mapBetweenAandB.keySet()) {
			for (Destinasjon d2 : mapBetweenAandB.get(d1)) {
				for (Destinasjon d3 : mapBetweenAandB.get(d2)) {
					if (mapBetweenAandB.get(d1).contains(d2) && mapBetweenAandB.get(d2).contains(d3)) {
						mapBetweenAandB.get(d1).add(d3);
					}
				}
			}
		}
	}
}
