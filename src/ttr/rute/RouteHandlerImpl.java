package ttr.rute;

import ttr.spelar.PlayerAndNetworkWTF;
import ttr.utgaave.GameVersion;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RouteHandlerImpl implements RouteHandler {
	//TODO trengs denne klassa? og kva eksakt gjer ho? +dårleg namn
	private final Set<Route> allRoutes;
	private final Set<Route> allBuiltRoutes;

	public RouteHandlerImpl(GameVersion spel){
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

	public Set<Route> findRoutesNotYetBuilt(Collection<PlayerAndNetworkWTF> spelarar) throws RemoteException {
		findAllBuiltRoutes(spelarar);
		return findNotYetBuiltRoutes();
	}

	//TODO Lagringa av kven som har bygd kva rute generelt bør kunne gjerast mykje enklare..
	private void findAllBuiltRoutes(Collection<PlayerAndNetworkWTF> players) {
		for (PlayerAndNetworkWTF player : players) {
			findRoutesBuiltByThisPlayer(player);
		}
	}

	private void findRoutesBuiltByThisPlayer(PlayerAndNetworkWTF player) {
		allRoutes.addAll(player.getBygdeRuter());
	}

	private Set<Route> findNotYetBuiltRoutes() {
		Set<Route> notYetBuiltRoutes = new HashSet<>();
		allRoutes.stream().filter(route -> !allBuiltRoutes.contains(route)).forEach(x -> notYetBuiltRoutes.add(x));
		return notYetBuiltRoutes;
	}
}