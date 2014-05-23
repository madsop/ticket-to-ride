package ttr.oppdrag;

import ttr.data.Destinasjon;
import ttr.kjerna.IHovud;
import ttr.rute.Route;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerMissionHandlerImpl extends UnicastRemoteObject implements PlayerMissionHandler {
	private static final long serialVersionUID = 5194460142995578869L;
	private Set<Mission> missions;
	private IHovud hovud;
	private Map<Destinasjon, Set<Destinasjon>> mapBetweenAandB;

	public PlayerMissionHandlerImpl(IHovud hovud) throws RemoteException {
		super();
		missions = new HashSet<>();
		this.hovud = hovud;
		mapBetweenAandB = new HashMap<>();
		initialiserMatrise();
	}

	public void retrieveMission(Mission mission) throws RemoteException{
		this.missions.add(mission);
	}

	public Collection<Mission> getOppdrag() throws RemoteException  {
		return missions;
	}

	public int getAntalOppdrag() throws RemoteException {
		return missions.size();
	}
	
	public int getOppdragspoeng() throws RemoteException  { //TODO skill ut alt som har med å avslutte spelet i ei eiga klasse?
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

	public int getAntalFullfoerteOppdrag() throws RemoteException{
		return (int) missions.stream().filter(x -> isMissionAccomplished(x)).count();
	}

	public void bygg(Route rute) throws RemoteException {
		// Sjekk for fullførde oppdrag?
		mapBetweenAandB.get(rute.getStart()).add(rute.getEnd());
		mapBetweenAandB.get(rute.getEnd()).add(rute.getStart());
		
		transitiveClosure();
	}

	public Mission trekkOppdragskort() throws RemoteException  {
		if (hovud.getAntalGjenverandeOppdrag() > 0) {
			return hovud.getOppdrag();
			//System.out.println(trekt.getDestinasjonar().toArray()[1]);
		}
		return null;
	}

	public void removeChosenMissionFromDeck(int oppdragsid) throws RemoteException {
		hovud.getGjenverandeOppdrag().removeIf(x -> (x.getMissionId() == oppdragsid));
	}

	private void initialiserMatrise() {
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
