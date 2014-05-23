package ttr.listeners;

import ttr.data.Farge;
import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

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
		Farge routeColour = routeWantedToBuild.getColour();
		int numberOfDemandedJokers = routeWantedToBuild.getNumberOfRequiredJokers();
		int playersNumberOfJokers = core.findPlayerInAction().getNumberOfRemainingJokers();

		buildRoute(core, frame, routeWantedToBuild, normalCardsDemanded,
				routeColour, numberOfDemandedJokers, playersNumberOfJokers);
	}

	private void buildRoute(Core core, JFrame frame, Route routeWantedToBuild,	int normalCardsDemanded, 
			Farge routeColour, int numberOfDemandedJokers, int playersNumberOfJokers) throws RemoteException {
		if (playerCanBuildThisRoute(core.findPlayerInAction(), routeColour, normalCardsDemanded, numberOfDemandedJokers, playersNumberOfJokers) ){
			buildRoute(core, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers, routeColour);
		}
		else { //TODO god feilmelding her.
			JOptionPane.showMessageDialog(frame, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
		}
	}

	private Route letUserChooseRouteToBuild(JFrame frame, Set<Route> ruterArray) {
		return (Route) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
				JOptionPane.QUESTION_MESSAGE, null, ruterArray.toArray(), ruterArray.iterator().next());
	}

	private void buildRoute(Core hovud, Route routeWantedToBuild, int kortKrevd, int krevdJokrar, Farge colour) throws RemoteException {
		if (routeWantedToBuild.isTunnel()) {
			hovud.byggTunnel(routeWantedToBuild, colour, kortKrevd, krevdJokrar);
		}
		else {
			hovud.bygg(routeWantedToBuild, colour, kortKrevd, krevdJokrar);
		}
	}

	private boolean playerCanBuildThisRoute(PlayerAndNetworkWTF player, Farge routeColour, int kortKrevd, int krevdJokrar, int harjokrar) throws RemoteException {
		return
				player.getGjenverandeTog() >= (kortKrevd + krevdJokrar)
				&& krevdJokrar <= harjokrar; 
//				&& (kortKrevd <= ( (harjokrar-krevdJokrar) + player.getNumberOfCardsLeftInColour(routeColour)) );
	}
}