package ttr.rute;

import ttr.spelar.ISpelar;
import ttr.utgaave.ISpelUtgaave;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Rutehandsamar implements IRutehandsamar {
	//TODO trengs denne klassa? og kva eksakt gjer ho? +dårleg namn
	private final Set<IRute> ruter;
	private final ArrayList<IRute> alleBygdeRuter;

	public Rutehandsamar(ISpelUtgaave spel){
		this.ruter = spel.getRuter();
		this.alleBygdeRuter = new ArrayList<>();
	}

	public ArrayList<IRute> getAlleBygdeRuter(){
		return alleBygdeRuter;
	}

	public Set<IRute> getRuter() {
		return ruter;
	}

	public void nyRute(IRute rute) {
		alleBygdeRuter.add(rute);
	}

	//TODO denne metoden bør kunne gjerast mykje enklare.
	//TODO Det gjeld vel i grunn lagringa av kven som har bygd kva rute generelt.
	@Override
	public Set<IRute> finnFramRuter(ArrayList<ISpelar> spelarar) throws RemoteException {
		findAllBuiltRoutes(spelarar);

		int numberOfRoutes = ruter.size();
		IRute[] ruterTemp = new IRute[numberOfRoutes];
		Iterator<IRute> ruteIterator = ruter.iterator();
		for (int i = 0; i < numberOfRoutes; i++) {
			ruterTemp[i] =  ruteIterator.next();
		}

		return egSkjonnerFaktiskIkkjeKvadenneGjer(numberOfRoutes, ruterTemp);
	}

	private void findAllBuiltRoutes(ArrayList<ISpelar> spelarar) throws RemoteException {
		for (ISpelar player : spelarar) {
			for (int j = 0; j < player.getBygdeRuterSize(); j++) {
				int routeId = player.getBygdeRuterId(j);
				for (IRute route : ruter) {
					if (route.getRuteId() == routeId && !(alleBygdeRuter.contains(route))) {
						alleBygdeRuter.add(route);
					}
				}
			}
		}
	}

	private Set<IRute> egSkjonnerFaktiskIkkjeKvadenneGjer(int totalNumberOfRoutes, IRute[] ruterTemp) {
//		IRute[] notYetBuiltRoutes = new IRute[totalNumberOfRoutes-alleBygdeRuter.size()];
		Set<IRute> notYetBuiltRoutes = new HashSet<>();
		
		for (int i = 0; i < totalNumberOfRoutes; i++) {
			handleOneRoute(ruterTemp, notYetBuiltRoutes, i);
		}
		return notYetBuiltRoutes;
	}

	private void handleOneRoute(IRute[] ruterTemp, Set<IRute> notYetBuiltRoutes, int i) {
		boolean isRouteBuilt = isRouteBuilt(ruterTemp, i);
		if (!isRouteBuilt) {
			notYetBuiltRoutes.add(ruterTemp[i]);
		}
	}

	private boolean isRouteBuilt(IRute[] ruterTemp, int i) {
		for (IRute eiBygdRute : alleBygdeRuter) {
			if (ruterTemp[i] == eiBygdRute) {
				return true;
			}
		}
		return false;
	}
}