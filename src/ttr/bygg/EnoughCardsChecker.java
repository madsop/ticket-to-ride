package ttr.bygg;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ttr.data.Colour;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.rute.Route;
import ttr.spelar.IPlayer;

public class EnoughCardsChecker {
	private GUI gui;

	public EnoughCardsChecker(GUI gui) {
		this.gui = gui;
	}

	ArrayList<Colour> findPossibleColoursToBuildIn(IPlayer player, int numberOfDemandedNormalCards, int ekstrajokrar) throws RemoteException {
		ArrayList<Colour> mulegeFargar = new ArrayList<>();
		for (Colour colour : Konstantar.FARGAR) {
			if (canBuildThisRouteInThisColour(player, numberOfDemandedNormalCards,	ekstrajokrar, colour)){
				mulegeFargar.add(colour);
			}
		}
		return mulegeFargar;
	}

	//TODO denne metoden må jo returnere og ev. sørge for å stoppe bygginga
	void checkIfThePlayerHasEnoughCards(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int jokers) throws HeadlessException, RemoteException {
		if ( (jokers > byggjandeSpelar.getNumberOfRemainingJokers() || playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers))
				&& (bygd.getColour() != Colour.valfri)) {
			JOptionPane.showMessageDialog(gui, Infostrengar.IkkjeNokKort);
		}
	}

	boolean numberOfChosenJokersNotOK(Colour colour, int kortKrevd, int numberOfDemandedJokers, IPlayer byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfRemainingJokers() < chosenNumberOfJokers
				|| chosenNumberOfJokers < numberOfDemandedJokers
				|| playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd, numberOfDemandedJokers, byggjandeSpelar, chosenNumberOfJokers);
	}

	private boolean canBuildThisRouteInThisColour(IPlayer player, int numberOfDemandedNormalCards, int ekstrajokrar, Colour colour) throws RemoteException {
		return ((colour != Colour.valfri) && (player.getNumberOfCardsLeftInColour(colour) + ekstrajokrar) >= numberOfDemandedNormalCards && ekstrajokrar >= 0) 
				||
				(ekstrajokrar >= numberOfDemandedNormalCards);
	}

	private boolean playerDoesNotHaveEnoughCardsInChosenColour(Colour colour, int kortKrevd, int krevdJokrar, IPlayer byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfCardsLeftInColour(colour) < kortKrevd - (chosenNumberOfJokers-krevdJokrar);
	}
}
