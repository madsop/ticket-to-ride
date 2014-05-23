package ttr.listeners;

import ttr.data.Farge;
import ttr.kjerna.Core;
import ttr.rute.Route;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.rmi.RemoteException;
import java.util.Set;

class BuildRouteHandler {

	public BuildRouteHandler(Core hovud, JFrame frame) throws RemoteException {
		Set<Route> notYetBuiltRoutes = hovud.findRoutesNotYetBuilt();
		Route routeWantedToBuild = letUserChooseRouteToBuild(frame, notYetBuiltRoutes);
		if (routeWantedToBuild == null) { return; }

		int normalCardsDemanded = routeWantedToBuild.getLength()-routeWantedToBuild.getNumberOfRequiredJokers();
		Farge routeColour = routeWantedToBuild.getColour();
		int numberOfDemandedJokers = routeWantedToBuild.getNumberOfRequiredJokers();
		int playersNumberOfJokers = hovud.getKvenSinTur().getNumberOfRemainingJokers();

		buildRoute(hovud, frame, routeWantedToBuild, normalCardsDemanded,
				routeColour, numberOfDemandedJokers, playersNumberOfJokers);
	}

	private void buildRoute(Core hovud, JFrame frame, Route routeWantedToBuild,	int normalCardsDemanded, 
			Farge routeColour, int numberOfDemandedJokers, int playersNumberOfJokers) throws RemoteException {
		if (playerCanBuildThisRoute(hovud.getKvenSinTur().getNumberOfCardsLeftInColour(routeColour), normalCardsDemanded, numberOfDemandedJokers, playersNumberOfJokers) ){
			tryToBuildRoute(hovud, frame, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers, routeColour);
		}
		else {
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

	private boolean playerCanBuildThisRoute(int playersCardsInThisColour, int kortKrevd, int krevdJokrar, int harjokrar) {
		return krevdJokrar <= harjokrar && (kortKrevd <= ( (harjokrar-krevdJokrar) + playersCardsInThisColour) );
	}

	private void tryToBuildRoute(Core hovud, JFrame frame, Route routeWantedToBuild, int kortKrevd, int numberOfDemandedJokers, Farge colour) throws RemoteException {
		if (hovud.getKvenSinTur().getGjenverandeTog() >= kortKrevd+numberOfDemandedJokers) {
			buildRoute(hovud, routeWantedToBuild, kortKrevd, numberOfDemandedJokers, colour);
		}
		else {
			JOptionPane.showMessageDialog(frame, "Du har ikkje nok tog att til å byggje denne ruta.");
		}
	}
}