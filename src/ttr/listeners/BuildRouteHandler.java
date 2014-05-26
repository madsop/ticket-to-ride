package ttr.listeners;

import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.IPlayer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.rmi.RemoteException;
import java.util.Set;

class BuildRouteHandler {

	public BuildRouteHandler(Core core, JFrame frame) throws RemoteException {
		Set<Route> notYetBuiltRoutes = core.findRoutesNotYetBuilt();
		Route routeWantedToBuild = letUserChooseRouteToBuild(frame, notYetBuiltRoutes);
		if (routeWantedToBuild == null) { return; }

		int normalCardsDemanded = routeWantedToBuild.getLength()-routeWantedToBuild.getNumberOfRequiredJokers();
		int numberOfDemandedJokers = routeWantedToBuild.getNumberOfRequiredJokers();
		int playersNumberOfJokers = core.findPlayerInAction().getNumberOfRemainingJokers();

		buildRoute(core, frame, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers, playersNumberOfJokers);
	}

	private void buildRoute(Core core, JFrame frame, Route routeWantedToBuild,	int normalCardsDemanded, 
			int numberOfDemandedJokers, int playersNumberOfJokers) throws RemoteException {
		if (playerCanBuildThisRoute(core.findPlayerInAction(), normalCardsDemanded, numberOfDemandedJokers, playersNumberOfJokers) ){
			buildRoute(core, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers);
		}
		else { //TODO god feilmelding her.
			JOptionPane.showMessageDialog(frame, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
		}
	}

	private Route letUserChooseRouteToBuild(JFrame frame, Set<Route> ruterArray) {
		return (Route) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
				JOptionPane.QUESTION_MESSAGE, null, ruterArray.toArray(), ruterArray.iterator().next());
	}

	private void buildRoute(Core hovud, Route routeWantedToBuild, int kortKrevd, int krevdJokrar) throws RemoteException {
		if (routeWantedToBuild.isTunnel()) {
			hovud.byggTunnel(routeWantedToBuild, kortKrevd, krevdJokrar);
		}
		else {
			hovud.bygg(routeWantedToBuild, kortKrevd, krevdJokrar);
		}
	}

	private boolean playerCanBuildThisRoute(IPlayer player, int kortKrevd, int krevdJokrar, int harjokrar) throws RemoteException {
		return
				player.getGjenverandeTog() >= (kortKrevd + krevdJokrar)
				&& krevdJokrar <= harjokrar; 
//				&& (kortKrevd <= ( (harjokrar-krevdJokrar) + player.getNumberOfCardsLeftInColour(routeColour)) );
	}
}