package ttr.bygg;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.rute.Route;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ByggHjelpar implements IByggHjelpar {
	private final IGUI gui;
	private Farge valdFarge; // TODO denne må vekk
	private final boolean nett;

	public ByggHjelpar(IGUI gui, boolean nett) {
		this.gui = gui;
		this.nett = nett;
	}

	private int byggValfriFarge(ISpelar player, int numberOfDemandedJokers, int numberOfDemandedNormalCards) throws RemoteException {
		ArrayList<Farge> mulegeFargar = new ArrayList<>();
		int ekstrajokrar = player.getNumberOfRemainingJokers()-numberOfDemandedJokers;
		System.out.println("ekstrajokrar: " +ekstrajokrar);
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++){
			if (canBuildThisRouteInThisColour(player, numberOfDemandedNormalCards,	ekstrajokrar, Konstantar.FARGAR[i])){
				mulegeFargar.add(Konstantar.FARGAR[i]);
			}
		}

		if (mulegeFargar.size() > 0){
			return chooseColourToBuildIn(mulegeFargar);
		}
		return -1;
	}

	private int chooseColourToBuildIn(ArrayList<Farge> mulegeFargar) {
		int colourPosition = mulegeFargar.size() + 1;
		while (colourPosition<0 || colourPosition > mulegeFargar.size()){
			colourPosition = JOptionPane.showOptionDialog((Component) gui, Infostrengar.VelFargeÅByggeILabel, Infostrengar.VelFargeÅByggeILabel, 
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, mulegeFargar.toArray(), mulegeFargar.get(0));

			if (colourPosition==-1){ return -1;	}
		}
		return Konstantar.finnPosisjonForFarge(mulegeFargar.get(colourPosition));
	}

	private boolean canBuildThisRouteInThisColour(ISpelar player, int numberOfDemandedNormalCards, int ekstrajokrar, Farge colour) throws RemoteException {
		return ((colour != Farge.valfri) && (player.getNumberOfCardsLeftInColour(colour) + ekstrajokrar) >= numberOfDemandedNormalCards && ekstrajokrar >= 0) 
				||
				(ekstrajokrar >= numberOfDemandedNormalCards);
	}

	public ByggjandeInfo bygg(Route routeToBuild, Farge colour, int kortKrevd, int krevdJokrar, ISpelar myPlayer, ISpelar kvenSinTur) throws RemoteException {
		ISpelar buildingPlayer = nett ? myPlayer : kvenSinTur;
		int position = Konstantar.finnPosisjonForFarge(colour);
		if (routeToBuild.getColour() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
			position = byggValfriFarge(buildingPlayer,krevdJokrar,kortKrevd); //TODO fix denne - bruk final på position
			if (position == -1) { return null; }
		}
		else {
			valdFarge = routeToBuild.getColour();
		}

		int jokers = chooseNumberOfJokersToUser(routeToBuild, Konstantar.FARGAR[position], kortKrevd,	krevdJokrar, buildingPlayer);
		checkIfThePlayerHasEnoughCards(routeToBuild, Konstantar.FARGAR[position], kortKrevd, krevdJokrar,	buildingPlayer, jokers);
		buildingPlayer.bygg(routeToBuild);
		updatePlayersCards(Konstantar.FARGAR[position], kortKrevd, krevdJokrar, buildingPlayer, jokers);

		return new ByggjandeInfo(buildingPlayer,jokers, position);
	}

	 //TODO denne metoden må jo returnere og ev. sørge for å stoppe bygginga
	private void checkIfThePlayerHasEnoughCards(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int jokers)	throws RemoteException {
		if (jokers > byggjandeSpelar.getNumberOfRemainingJokers() || playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers)){
			if (routeIsNotJokerColoured(bygd)){
				JOptionPane.showMessageDialog((Component) gui, Infostrengar.IkkjeNokKort);
			}
		}
	}

	private boolean routeIsNotJokerColoured(Route bygd) {
		return bygd.getColour() != Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1];
	}

	private int chooseNumberOfJokersToUser(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar) throws RemoteException {
		int jokers = 0;
		if (byggjandeSpelar.getNumberOfRemainingJokers() > 0) {
			do {
				jokers = velAntalJokrarDuVilBruke(bygd, byggjandeSpelar,valdFarge);
			} while(numberOfChosenJokersNotOK(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers));
		}
		return jokers;
	}

	private boolean numberOfChosenJokersNotOK(Farge colour, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return playerHasLessJokersThanHeSays(byggjandeSpelar, chosenNumberOfJokers)
				|| chosenNumberOfJokers < krevdJokrar
				|| playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd,	krevdJokrar, byggjandeSpelar, chosenNumberOfJokers);
	}

	private boolean playerHasLessJokersThanHeSays(ISpelar byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfRemainingJokers() < chosenNumberOfJokers;
	}

	private boolean playerDoesNotHaveEnoughCardsInChosenColour(Farge colour, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfCardsLeftInColour(colour) < kortKrevd - (chosenNumberOfJokers-krevdJokrar);
	}

	private void updatePlayersCards(Farge colour, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int jokers) throws RemoteException {
		byggjandeSpelar.decrementCardsAt(colour, kortKrevd-(jokers-krevdJokrar));
		byggjandeSpelar.decrementCardsAt(Farge.valfri, jokers);
	}


	public ByggjandeInfo byggTunnel(IBord bord, Route bygd, Farge colour, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException {
		Farge[] treTrekte = new Farge[3];
		int ekstra = 0;
		for (int i = 0; i < treTrekte.length; i++) {
			treTrekte[i] = bord.getRandomCardFromTheDeckAndPutOnTable(0, false);
			if (treTrekte[i] == null){
				JOptionPane.showMessageDialog((Component) gui, Infostrengar.TomtPåBordet);
				return null;
			}
			if (treTrekte[i] == Farge.valfri || treTrekte[i] == bygd.getColour()) {
				ekstra++;
			}
		}

		int byggLell = askUserIfBuildAnyway(treTrekte, ekstra);
		if (byggLell == JOptionPane.OK_OPTION) {
			return bygg(bygd, colour, kortKrevd+ekstra, krevdJokrar, minSpelar,kvenSinTur);
		}
		return null;
	}

	private int askUserIfBuildAnyway(Farge[] treTrekte, int ekstra) {
		return JOptionPane.showConfirmDialog((Component) gui, Infostrengar.TunnelStartTekst +treTrekte[0] +", "
				+treTrekte[1] +" og " +treTrekte[2]	+". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
	}

	private int velAntalJokrarDuVilBruke(Route route, ISpelar player, Farge chosenColour) throws RemoteException {
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
