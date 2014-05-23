package ttr.listeners;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;
import ttr.rute.Route;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.Set;

class BuildRouteHandler {

	public BuildRouteHandler(IHovud hovud, JFrame frame) throws RemoteException {
		Set<Route> notYetBuiltRoutes = hovud.findRoutesNotYetBuilt();

		Route routeWantedToBuild = letUserChooseRouteToBuild(frame, notYetBuiltRoutes);
		if (routeWantedToBuild == null) { return; }

		int normalCardsDemanded = routeWantedToBuild.getLength()-routeWantedToBuild.getNumberOfRequiredJokers();
		Farge routeColour = routeWantedToBuild.getColour();
		int numberOfDemandedJokers = routeWantedToBuild.getNumberOfRequiredJokers();

		int playersNumberOfJokers = hovud.getKvenSinTur().getNumberOfRemainingJokers();
	
		if (isGreyRoute(routeWantedToBuild)){
			buildRoute(hovud, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers, routeColour);
		}
		else if (playerCanBuildThisRoute(hovud.getKvenSinTur().getNumberOfCardsLeftInColour(routeColour), normalCardsDemanded, numberOfDemandedJokers, playersNumberOfJokers) ){
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

	private boolean isGreyRoute(Route routeWantedToBuild) {
		return routeWantedToBuild.getColour() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1];
	}

	private void buildRoute(IHovud hovud, Route routeWantedToBuild, int kortKrevd, int krevdJokrar, Farge colour) throws RemoteException {
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

	private void tryToBuildRoute(IHovud hovud, JFrame frame, Route routeWantedToBuild, int kortKrevd, int numberOfDemandedJokers, Farge colour) throws RemoteException {
		try {
			if (hovud.getKvenSinTur().getGjenverandeTog() >= kortKrevd+numberOfDemandedJokers) {
				buildRoute(hovud, routeWantedToBuild, kortKrevd, numberOfDemandedJokers, colour);
			}
			else {
				JOptionPane.showMessageDialog(frame, "Du har ikkje nok tog att til å byggje denne ruta.");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}
}