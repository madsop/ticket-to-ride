package ttr.bygg;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.rute.Route;
import ttr.spelar.IPlayer;

import javax.swing.*;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ByggHjelpar {
	private final GUI gui;
	private TunnelBuildHelper tunnelBuildHelper;
	
	//TODO fortener denne å bli splitta i ein kan-bygge-her-klasse og resten? evt. ein colour-finder-klasse
	
	public ByggHjelpar(GUI gui) {
		this.gui = gui;
		this.tunnelBuildHelper = new TunnelBuildHelper(gui);
	}

	public ByggjandeInfo bygg(Route routeToBuild, int kortKrevd, int krevdJokrar, IPlayer iPlayer) throws HeadlessException, RemoteException {
		Colour colourToBuildIn = findColourToBuildIn(routeToBuild, kortKrevd, krevdJokrar, iPlayer);
		if (colourToBuildIn == null) { return null; }
		
		int jokers = chooseNumberOfJokersToUser(routeToBuild, colourToBuildIn, kortKrevd,	krevdJokrar, iPlayer);
		checkIfThePlayerHasEnoughCards(routeToBuild, colourToBuildIn, kortKrevd, krevdJokrar,	iPlayer, jokers);
		iPlayer.bygg(routeToBuild);
		updatePlayersCards(colourToBuildIn, kortKrevd, krevdJokrar, iPlayer, jokers);

		return new ByggjandeInfo(iPlayer,jokers, colourToBuildIn);
	}
	public ByggjandeInfo byggTunnel(Table bord, Route routeToBuild, int kortKrevd, int krevdJokrar, IPlayer buildingPlayer) throws HeadlessException, RemoteException {
		return bygg(routeToBuild, kortKrevd + tunnelBuildHelper.checkIfTunnelShouldBeBuiltAndHowManyExtraCardsWillBeNeeded(bord, routeToBuild), krevdJokrar, buildingPlayer);
	}
	
	
	private Colour findColourToBuildIn(Route routeToBuild, int kortKrevd, int krevdJokrar, IPlayer buildingPlayer) throws RemoteException {
		if (routeToBuild.getColour() == Colour.valfri){
			return byggValfriFarge(buildingPlayer,krevdJokrar,kortKrevd);
		}
		return routeToBuild.getColour();
	}


	private Colour byggValfriFarge(IPlayer player, int numberOfDemandedJokers, int numberOfDemandedNormalCards) throws RemoteException {
		int extraJokers = player.getNumberOfRemainingJokers() - numberOfDemandedJokers;
		System.out.println("ekstrajokrar: " +extraJokers);

		ArrayList<Colour> possibleColours = findPossibleColoursToBuildIn(player, numberOfDemandedNormalCards, extraJokers);

		if (possibleColours.size() > 0){
			return chooseColourToBuildIn(possibleColours);
		}
		return null;
	}

	private ArrayList<Colour> findPossibleColoursToBuildIn(IPlayer player, int numberOfDemandedNormalCards, int ekstrajokrar) throws RemoteException {
		ArrayList<Colour> mulegeFargar = new ArrayList<>();
		for (Colour colour : Konstantar.FARGAR) {
			if (canBuildThisRouteInThisColour(player, numberOfDemandedNormalCards,	ekstrajokrar, colour)){
				mulegeFargar.add(colour);
			}
		}
		return mulegeFargar;
	}

	private Colour chooseColourToBuildIn(ArrayList<Colour> mulegeFargar) {
		int colourPosition = mulegeFargar.size() + 1;
		while (colourPosition<0 || colourPosition > mulegeFargar.size()){
			colourPosition = JOptionPane.showOptionDialog(gui, Infostrengar.VelFargeÅByggeILabel, Infostrengar.VelFargeÅByggeILabel, 
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, mulegeFargar.toArray(), mulegeFargar.get(0));
			if (colourPosition==-1){ return null; }
		}
		return mulegeFargar.get(colourPosition);
	}

	private boolean canBuildThisRouteInThisColour(IPlayer player, int numberOfDemandedNormalCards, int ekstrajokrar, Colour colour) throws RemoteException {
		return ((colour != Colour.valfri) && (player.getNumberOfCardsLeftInColour(colour) + ekstrajokrar) >= numberOfDemandedNormalCards && ekstrajokrar >= 0) 
				||
				(ekstrajokrar >= numberOfDemandedNormalCards);
	}

	 //TODO denne metoden må jo returnere og ev. sørge for å stoppe bygginga
	private void checkIfThePlayerHasEnoughCards(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int jokers) throws HeadlessException, RemoteException {
		if (jokers > byggjandeSpelar.getNumberOfRemainingJokers() || playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers)){
			if (bygd.getColour() != Colour.valfri){
				JOptionPane.showMessageDialog(gui, Infostrengar.IkkjeNokKort);
			}
		}
	}

	private int chooseNumberOfJokersToUser(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar) throws RemoteException {
		int jokers = 0;
		if (byggjandeSpelar.getNumberOfRemainingJokers() > 0) {
			do {
				jokers = velAntalJokrarDuVilBruke(bygd, byggjandeSpelar, colour);
			} while(numberOfChosenJokersNotOK(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers));
		}
		return jokers;
	}

	private boolean numberOfChosenJokersNotOK(Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return playerHasLessJokersThanHeSays(byggjandeSpelar, chosenNumberOfJokers)
				|| chosenNumberOfJokers < krevdJokrar
				|| playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd,	krevdJokrar, byggjandeSpelar, chosenNumberOfJokers);
	}

	private boolean playerHasLessJokersThanHeSays(IPlayer byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfRemainingJokers() < chosenNumberOfJokers;
	}

	private boolean playerDoesNotHaveEnoughCardsInChosenColour(Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfCardsLeftInColour(colour) < kortKrevd - (chosenNumberOfJokers-krevdJokrar);
	}

	private void updatePlayersCards(Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int jokers) throws RemoteException {
		byggjandeSpelar.decrementCardsAt(colour, kortKrevd-(jokers-krevdJokrar));
		byggjandeSpelar.decrementCardsAt(Colour.valfri, jokers);
	}


	private int velAntalJokrarDuVilBruke(Route route, IPlayer player, Colour chosenColour) throws RemoteException {
		int playersNumberOfJokers = player.getNumberOfRemainingJokers();
		int howMany = -1;
		while (howMany < 0 || howMany > playersNumberOfJokers || howMany > route.getLength()) {
			String sendinn = Infostrengar.AntalJokrarStart +playersNumberOfJokers +" jokrar, " +
					player.getNumberOfCardsLeftInColour(chosenColour)		
					+ " av fargen du skal byggje, og ruta er " +route.getLength() +" tog lang.";
			String usersInput = JOptionPane.showInputDialog(sendinn,0);
			howMany = Integer.parseInt(usersInput);
		}
		return howMany;
	}
}