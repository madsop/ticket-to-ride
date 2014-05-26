package ttr.oppdrag;

import ttr.data.Destination;
import ttr.rute.Route;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerMissionHandler { 
	private Set<Mission> missions;
	private Map<Destination, Set<Destination>> mapBetweenAandB;

	public PlayerMissionHandler() {
		super();
		missions = new HashSet<>();
		initialiseConnectedRoutes();
	}

	public void retrieveMission(Mission mission){
		this.missions.add(mission);
	}

	public Collection<Mission> getMissions()  {
		return missions;
	}
	
	public int getMissionPoints()  { //TODO skill ut alt som har med å avslutte spelet i ei eiga klasse?
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

	public int getNumberOfFulfilledMissions(){
		return (int) missions.stream().filter(x -> isMissionAccomplished(x)).count();
	}

	public void markRouteAsBuilt(Route route) {
		// Sjekk for fullførde oppdrag?
		mapBetweenAandB.get(route.getStart()).add(route.getEnd());
		mapBetweenAandB.get(route.getEnd()).add(route.getStart());
		
		transitiveClosure();
	}

	private void initialiseConnectedRoutes() {
		mapBetweenAandB = new HashMap<>();
		for (int y = 0; y < Destination.values().length; y++) {
			mapBetweenAandB.putIfAbsent(Destination.values()[y], new HashSet<>());
		}
	}

	private void transitiveClosure() {
		// Dette er ein implementasjon av Warshallalgoritma, side 553 i Rosen [DiskMat]
		// Køyretida er på (u)behagelege 2n³, som er tilnærma likt optimalt
		for (Destination d1 : mapBetweenAandB.keySet()) {
			for (Destination d2 : mapBetweenAandB.get(d1)) {
				for (Destination d3 : mapBetweenAandB.get(d2)) {
					if (mapBetweenAandB.get(d1).contains(d2) && mapBetweenAandB.get(d2).contains(d3)) {
						mapBetweenAandB.get(d1).add(d3);
					}
				}
			}
		}
	}
}
