package ttr.bygg;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.Infostrengar;
import ttr.rute.Route;
import ttr.spelar.IPlayer;

import javax.swing.*;

import com.google.inject.Inject;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ByggHjelpar {
	private TunnelBuildHelper tunnelBuildHelper;
	private EnoughCardsChecker enoughCardsChecker;

	//TODO fortener denne å bli splitta i ein kan-bygge-her-klasse og resten? evt. ein colour-finder-klasse

	@Inject
	public ByggHjelpar(TunnelBuildHelper tunnelBuildHelper, EnoughCardsChecker enoughCardsChecker) {
		this.tunnelBuildHelper = tunnelBuildHelper;
		this.enoughCardsChecker = enoughCardsChecker;
	}

	public ByggjandeInfo buildRoute(Route routeToBuild, int kortKrevd, int krevdJokrar, IPlayer buildingPlayer) throws HeadlessException, RemoteException {
		Colour colourToBuildIn = findColourToBuildIn(routeToBuild, kortKrevd, krevdJokrar, buildingPlayer);
		if (colourToBuildIn == null) { return null; }

		int jokers = chooseNumberOfJokersToUser(routeToBuild, colourToBuildIn, kortKrevd,	krevdJokrar, buildingPlayer);
		enoughCardsChecker.checkIfThePlayerHasEnoughCards(routeToBuild, colourToBuildIn, kortKrevd, krevdJokrar,	buildingPlayer, jokers);
		//TODO avbryt om ikkje sjekken over held
		buildingPlayer.bygg(routeToBuild);
		updatePlayersCards(colourToBuildIn, kortKrevd, krevdJokrar, buildingPlayer, jokers);

		return new ByggjandeInfo(buildingPlayer, jokers, colourToBuildIn);
	}
	
	public ByggjandeInfo buildTunnel(Table bord, Route routeToBuild, int kortKrevd, int krevdJokrar, IPlayer buildingPlayer) throws HeadlessException, RemoteException {
		return buildRoute(routeToBuild, kortKrevd + tunnelBuildHelper.checkIfTunnelShouldBeBuiltAndHowManyExtraCardsWillBeNeeded(bord, routeToBuild), krevdJokrar, buildingPlayer);
	}

	private Colour findColourToBuildIn(Route routeToBuild, int kortKrevd, int krevdJokrar, IPlayer buildingPlayer) throws RemoteException {
		if (routeToBuild.getColour() == Colour.valfri){
			return findPossibilitiesAndChooseWhichColourToBuildGreyRouteIn(buildingPlayer,krevdJokrar,kortKrevd);
		}
		return routeToBuild.getColour();
	}

	private Colour findPossibilitiesAndChooseWhichColourToBuildGreyRouteIn(IPlayer player, int numberOfDemandedJokers, int numberOfDemandedNormalCards) throws RemoteException {
		int extraJokers = player.getNumberOfRemainingJokers() - numberOfDemandedJokers;
		ArrayList<Colour> possibleColours = enoughCardsChecker.findPossibleColoursToBuildIn(player, numberOfDemandedNormalCards, extraJokers);
		if (possibleColours.size() > 0){
			return chooseColourToBuildIn(possibleColours);
		}
		return null;
	}

	private Colour chooseColourToBuildIn(ArrayList<Colour> mulegeFargar) {
		int colourPosition = mulegeFargar.size() + 1;
		while (colourPosition<0 || colourPosition > mulegeFargar.size()){
			colourPosition = JOptionPane.showOptionDialog(null, Infostrengar.VelFargeÅByggeILabel, Infostrengar.VelFargeÅByggeILabel, 
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, mulegeFargar.toArray(), mulegeFargar.get(0));
			if (colourPosition==-1){ return null; }
		}
		return mulegeFargar.get(colourPosition);
	}

	private int chooseNumberOfJokersToUser(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar) throws RemoteException {
		int jokers = 0;
		if (byggjandeSpelar.getNumberOfRemainingJokers() > 0) {
			do {
				jokers = velAntalJokrarDuVilBruke(bygd, byggjandeSpelar, colour);
			} while(enoughCardsChecker.numberOfChosenJokersNotOK(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers));
		}
		return jokers;
	}

	private int velAntalJokrarDuVilBruke(Route route, IPlayer player, Colour chosenColour) throws RemoteException {
		int playersNumberOfJokers = player.getNumberOfRemainingJokers();
		int howMany = -1;
		while (howMany < 0 || howMany > playersNumberOfJokers || howMany > route.getLength()) {
			String sendinn = Infostrengar.AntalJokrarStart +playersNumberOfJokers +" jokrar, " + player.getNumberOfCardsLeftInColour(chosenColour)
					+ " av fargen du skal byggje, og ruta er " +route.getLength() +" tog lang.";
			String usersInput = JOptionPane.showInputDialog(sendinn,0);
			howMany = Integer.parseInt(usersInput);
		}
		return howMany;
	}

	private void updatePlayersCards(Colour colour, int kortKrevd, int krevdJokrar, IPlayer buildingPlayer, int jokers) throws RemoteException {
		buildingPlayer.decrementCardsAt(colour, kortKrevd-(jokers-krevdJokrar));
		buildingPlayer.decrementCardsAt(Colour.valfri, jokers);
	}
}