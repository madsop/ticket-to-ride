package ttr.rute;

import ttr.spelar.ISpelar;
import ttr.utgaave.ISpelUtgaave;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Rutehandsamar implements IRutehandsamar {
	//TODO trengs denne klassa? og kva eksakt gjer ho? +dårleg namn
	private final Set<IRute> allRoutes;
	private final Set<IRute> allBuiltRoutes;

	public Rutehandsamar(ISpelUtgaave spel){
		this.allRoutes = spel.getRuter();
		this.allBuiltRoutes = new HashSet<>();
	}

	public Set<IRute> getAlleBygdeRuter(){
		return allBuiltRoutes;
	}

	public Set<IRute> getRuter() {
		return allRoutes;
	}

	public void nyRute(IRute rute) {
		allBuiltRoutes.add(rute);
	}

	public Set<IRute> findRoutesNotYetBuilt(Collection<ISpelar> spelarar) throws RemoteException {
		findAllBuiltRoutes(spelarar);
		return findNotYetBuiltRoutes();
	}

	//TODO Lagringa av kven som har bygd kva rute generelt bør kunne gjerast mykje enklare..
	private void findAllBuiltRoutes(Collection<ISpelar> players) throws RemoteException {
		for (ISpelar player : players) {
			findRoutesBuiltByThisPlayer(player);
		}
	}

	private void findRoutesBuiltByThisPlayer(ISpelar player) throws RemoteException {
		for (int i = 0; i < player.getBygdeRuterSize(); i++) {
			int routeId = player.getBygdeRuterId(i);
			for (IRute route : allRoutes) {
				if (route.getRuteId() == routeId) {
					allBuiltRoutes.add(route);
				}
			}
		}
	}

	private Set<IRute> findNotYetBuiltRoutes() {
		Set<IRute> notYetBuiltRoutes = new HashSet<>();

		Iterator<IRute> iterator = allRoutes.iterator();
		while (iterator.hasNext()) {
			handleOneRoute(notYetBuiltRoutes, iterator.next());
		}
		
		return notYetBuiltRoutes;
	}

	private void handleOneRoute(Set<IRute> notYetBuiltRoutes, IRute routeToCheck) {
		if (!isRouteBuilt(routeToCheck)) {
			notYetBuiltRoutes.add(routeToCheck);
		}
	}

	private boolean isRouteBuilt(IRute routeToCheck) {
		return allBuiltRoutes.contains(routeToCheck);
	}	
}