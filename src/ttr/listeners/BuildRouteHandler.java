package ttr.listeners;

import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.IPlayer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.rmi.RemoteException;
import java.util.Set;

public class BuildRouteHandler extends DelegationListener {
	public BuildRouteHandler(Core core) {
		super(core);
	}

	public void specific() throws RemoteException {
		Set<Route> notYetBuiltRoutes = core.findRoutesNotYetBuilt();
		
		Route routeWantedToBuild = letUserChooseRouteToBuild(null, notYetBuiltRoutes);
		if (routeWantedToBuild == null) { return; }

		int normalCardsDemanded = routeWantedToBuild.getLength()-routeWantedToBuild.getNumberOfRequiredJokers();
		int numberOfDemandedJokers = routeWantedToBuild.getNumberOfRequiredJokers();

		buildRoute(core, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers, core.findPlayerInAction().getNumberOfRemainingJokers());
	}

	private void buildRoute(Core core, Route routeWantedToBuild,	int normalCardsDemanded, 
			int numberOfDemandedJokers, int playersNumberOfJokers) throws RemoteException {
		if (playerCanBuildThisRoute(core.findPlayerInAction(), normalCardsDemanded, numberOfDemandedJokers, playersNumberOfJokers) ){
			buildRoute(core, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers);
		}
		else { //TODO god feilmelding her.
			JOptionPane.showMessageDialog(null, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
		}
	}

	private Route letUserChooseRouteToBuild(JFrame frame, Set<Route> ruterArray) {
		return (Route) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
				JOptionPane.QUESTION_MESSAGE, null, ruterArray.toArray(), ruterArray.iterator().next());
	}

	private void buildRoute(Core core, Route routeWantedToBuild, int kortKrevd, int krevdJokrar) throws RemoteException {
		//TODO kanskje heller ein propertychange enn dette rotet her?
		if (routeWantedToBuild.isTunnel()) {
			core.byggTunnel(routeWantedToBuild, kortKrevd, krevdJokrar);
		}
		else {
			core.bygg(routeWantedToBuild, kortKrevd, krevdJokrar);
		}
	}

	private boolean playerCanBuildThisRoute(IPlayer player, int kortKrevd, int krevdJokrar, int harjokrar) throws RemoteException {
		return
				player.getGjenverandeTog() >= (kortKrevd + krevdJokrar)
				&& krevdJokrar <= harjokrar; 
//				&& (kortKrevd <= ( (harjokrar-krevdJokrar) + player.getNumberOfCardsLeftInColour(routeColour)) );
	}
}