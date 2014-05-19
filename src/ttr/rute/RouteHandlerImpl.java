package ttr.rute;

import ttr.spelar.ISpelar;
import ttr.utgaave.ISpelUtgaave;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RouteHandlerImpl implements RouteHandler {
	//TODO trengs denne klassa? og kva eksakt gjer ho? +dårleg namn
	private final Set<Route> allRoutes;
	private final Set<Route> allBuiltRoutes;

	public RouteHandlerImpl(ISpelUtgaave spel){
		this.allRoutes = spel.getRuter();
		this.allBuiltRoutes = new HashSet<>();
	}

	public Set<Route> getBuiltRoutes(){
		return allBuiltRoutes;
	}

	public Set<Route> getRoutes() {
		return allRoutes;
	}

	public void newRoute(Route rute) {
		allBuiltRoutes.add(rute);
	}

	public Set<Route> findRoutesNotYetBuilt(Collection<ISpelar> spelarar) throws RemoteException {
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
			allRoutes.stream().filter(route -> route.getRouteId() == routeId).forEach(x -> allBuiltRoutes.add(x));
		}
	}

	private Set<Route> findNotYetBuiltRoutes() {
		Set<Route> notYetBuiltRoutes = new HashSet<>();
		allRoutes.stream().filter(route -> !allBuiltRoutes.contains(route)).forEach(x -> notYetBuiltRoutes.add(x));
		return notYetBuiltRoutes;
	}
}