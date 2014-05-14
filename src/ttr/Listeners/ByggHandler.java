package ttr.Listeners;

import ttr.data.Farge;
import ttr.data.Konstantar;
import ttr.kjerna.IHovud;
import ttr.rute.IRute;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.Set;

class ByggHandler {

	public ByggHandler(IHovud hovud, JFrame frame) throws RemoteException {
		Set<IRute> notYetBuiltRoutes = hovud.findRoutesNotYetBuilt();

		IRute routeWantedToBuild = letUserChooseRouteToBuild(frame, notYetBuiltRoutes);
		if (routeWantedToBuild == null) { return; }

		int[] playersCards = hovud.getKvenSinTur().getKort();
		int normalCardsDemanded = routeWantedToBuild.getLengde()-routeWantedToBuild.getAntaljokrar();
		Farge routeColour = routeWantedToBuild.getFarge();
		int numberOfDemandedJokers = routeWantedToBuild.getAntaljokrar();

		int position = Konstantar.finnPosisjonForFarg(routeColour);

		int playersNumberOfJokers = playersCards[playersCards.length-1];
	
		if (isGreyRoute(routeWantedToBuild)){
			buildRoute(hovud, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers, position);
		}
		else if (playerCanBuildThisRoute(playersCards, normalCardsDemanded, numberOfDemandedJokers, position, playersNumberOfJokers) ){
			tryToBuildRoute(hovud, frame, routeWantedToBuild, normalCardsDemanded, numberOfDemandedJokers, position);
		}
		else {
			JOptionPane.showMessageDialog(frame, "Synd, men du har ikkje nok kort til å byggje denne ruta enno. Trekk inn kort, du.");
		}

	}

	private boolean isGreyRoute(IRute routeWantedToBuild) {
		return routeWantedToBuild.getFarge() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1];
	}

	private void tryToBuildRoute(IHovud hovud, JFrame frame, IRute routeWantedToBuild, int kortKrevd, int numberOfDemandedJokers, int position) throws RemoteException {
		try {
			if (hovud.getKvenSinTur().getGjenverandeTog() >= kortKrevd+numberOfDemandedJokers) {
				buildRoute(hovud, routeWantedToBuild, kortKrevd, numberOfDemandedJokers, position);
			}
			else {
				JOptionPane.showMessageDialog(frame, "Du har ikkje nok tog att til å byggje denne ruta.");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}

	private void buildRoute(IHovud hovud, IRute routeWantedToBuild, int kortKrevd, int krevdJokrar, int plass) throws RemoteException {
		if (routeWantedToBuild.isTunnel()) {
			hovud.byggTunnel(routeWantedToBuild, plass, kortKrevd, krevdJokrar);
		}
		else {
			hovud.bygg(routeWantedToBuild, plass, kortKrevd, krevdJokrar);
		}
	}

	private boolean playerCanBuildThisRoute(int[] spelarensKort, int kortKrevd, int krevdJokrar, int plass, int harjokrar) {
		return krevdJokrar <= harjokrar && (kortKrevd <= ( (harjokrar-krevdJokrar) + spelarensKort[plass]) );
	}

	private IRute letUserChooseRouteToBuild(JFrame frame, Set<IRute> ruterArray) {
		return (IRute) JOptionPane.showInputDialog(frame, "Vel ruta du vil byggje", "Vel rute",
				JOptionPane.QUESTION_MESSAGE, null, ruterArray.toArray(), ruterArray.iterator().next());
	}
}