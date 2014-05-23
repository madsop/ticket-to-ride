package ttr.bygg;

import ttr.bord.Table;
import ttr.data.Farge;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ByggHjelpar implements IByggHjelpar {
	private final IGUI gui;
	
	//TODO fortener denne å bli splitta i ein kan-bygge-her-klasse og resten? evt. ein colour-finder-klasse
	
	public ByggHjelpar(IGUI gui) {
		this.gui = gui;
	}

	public ByggjandeInfo bygg(Route routeToBuild, Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		Farge colourToBuildIn = findColourToBuildIn(routeToBuild, kortKrevd, krevdJokrar, buildingPlayer);
		if (colourToBuildIn == null) { return null; }
		
		int jokers = chooseNumberOfJokersToUser(routeToBuild, colourToBuildIn, kortKrevd,	krevdJokrar, buildingPlayer);
		checkIfThePlayerHasEnoughCards(routeToBuild, colourToBuildIn, kortKrevd, krevdJokrar,	buildingPlayer, jokers);
		buildingPlayer.bygg(routeToBuild);
		updatePlayersCards(colourToBuildIn, kortKrevd, krevdJokrar, buildingPlayer, jokers);

		return new ByggjandeInfo(buildingPlayer,jokers);
	}

	public ByggjandeInfo byggTunnel(Table bord, Route bygd, Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		Farge[] treTrekte = drawThreeRandomCards(bord);
		int ekstra = computeExtraNeededCards(bygd, treTrekte);

		if (askUserIfBuildAnyway(treTrekte, ekstra) == JOptionPane.OK_OPTION) {
			return bygg(bygd, colour, kortKrevd+ekstra, krevdJokrar, buildingPlayer);
		}
		return null;
	}

	private Farge[] drawThreeRandomCards(Table bord) {
		Farge[] treTrekte = new Farge[3];
		
		for (int i = 0; i < treTrekte.length; i++) {
			treTrekte[i] = bord.getRandomCardFromTheDeck(0);
			if (treTrekte[i] == null){
				JOptionPane.showMessageDialog((Component) gui, Infostrengar.TomtPåBordet);
				return null;
			}
		}
		return treTrekte;
	}

	private int computeExtraNeededCards(Route bygd, Farge[] treTrekte) {
		int extra = 0;
		for (Farge drawnCard : treTrekte) {
			if (drawnCard == Farge.valfri || drawnCard == bygd.getColour()) {
				extra++;
			}
		}
		return extra;
	}
	
	private Farge findColourToBuildIn(Route routeToBuild, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		if (routeToBuild.getColour() == Farge.valfri){
			return byggValfriFarge(buildingPlayer,krevdJokrar,kortKrevd); //TODO fix denne - bruk final på position
		}
		return routeToBuild.getColour();
	}

	private Farge byggValfriFarge(PlayerAndNetworkWTF player, int numberOfDemandedJokers, int numberOfDemandedNormalCards) throws RemoteException {
		int extraJokers = player.getNumberOfRemainingJokers() - numberOfDemandedJokers;
		System.out.println("ekstrajokrar: " +extraJokers);

		ArrayList<Farge> possibleColours = findPossibleColoursToBuildIn(player, numberOfDemandedNormalCards, extraJokers);

		if (possibleColours.size() > 0){
			return chooseColourToBuildIn(possibleColours);
		}
		return null;
	}

	private ArrayList<Farge> findPossibleColoursToBuildIn(PlayerAndNetworkWTF player, int numberOfDemandedNormalCards, int ekstrajokrar) throws RemoteException {
		ArrayList<Farge> mulegeFargar = new ArrayList<>();
		for (Farge colour : Konstantar.FARGAR) {
			if (canBuildThisRouteInThisColour(player, numberOfDemandedNormalCards,	ekstrajokrar, colour)){
				mulegeFargar.add(colour);
			}
		}
		return mulegeFargar;
	}

	private Farge chooseColourToBuildIn(ArrayList<Farge> mulegeFargar) {
		int colourPosition = mulegeFargar.size() + 1;
		while (colourPosition<0 || colourPosition > mulegeFargar.size()){
			colourPosition = JOptionPane.showOptionDialog((Component) gui, Infostrengar.VelFargeÅByggeILabel, Infostrengar.VelFargeÅByggeILabel, 
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, mulegeFargar.toArray(), mulegeFargar.get(0));
			if (colourPosition==-1){ return null; }
		}
		return mulegeFargar.get(colourPosition);
	}

	private boolean canBuildThisRouteInThisColour(PlayerAndNetworkWTF player, int numberOfDemandedNormalCards, int ekstrajokrar, Farge colour) throws RemoteException {
		return ((colour != Farge.valfri) && (player.getNumberOfCardsLeftInColour(colour) + ekstrajokrar) >= numberOfDemandedNormalCards && ekstrajokrar >= 0) 
				||
				(ekstrajokrar >= numberOfDemandedNormalCards);
	}

	 //TODO denne metoden må jo returnere og ev. sørge for å stoppe bygginga
	private void checkIfThePlayerHasEnoughCards(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int jokers)	throws RemoteException {
		if (jokers > byggjandeSpelar.getNumberOfRemainingJokers() || playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers)){
			if (bygd.getColour() != Farge.valfri){
				JOptionPane.showMessageDialog((Component) gui, Infostrengar.IkkjeNokKort);
			}
		}
	}

	private int chooseNumberOfJokersToUser(Route bygd, Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar) throws RemoteException {
		int jokers = 0;
		if (byggjandeSpelar.getNumberOfRemainingJokers() > 0) {
			do {
				jokers = velAntalJokrarDuVilBruke(bygd, byggjandeSpelar, colour);
			} while(numberOfChosenJokersNotOK(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers));
		}
		return jokers;
	}

	private boolean numberOfChosenJokersNotOK(Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return playerHasLessJokersThanHeSays(byggjandeSpelar, chosenNumberOfJokers)
				|| chosenNumberOfJokers < krevdJokrar
				|| playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd,	krevdJokrar, byggjandeSpelar, chosenNumberOfJokers);
	}

	private boolean playerHasLessJokersThanHeSays(PlayerAndNetworkWTF byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfRemainingJokers() < chosenNumberOfJokers;
	}

	private boolean playerDoesNotHaveEnoughCardsInChosenColour(Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfCardsLeftInColour(colour) < kortKrevd - (chosenNumberOfJokers-krevdJokrar);
	}

	private void updatePlayersCards(Farge colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int jokers) throws RemoteException {
		byggjandeSpelar.decrementCardsAt(colour, kortKrevd-(jokers-krevdJokrar));
		byggjandeSpelar.decrementCardsAt(Farge.valfri, jokers);
	}

	private int askUserIfBuildAnyway(Farge[] treTrekte, int ekstra) {
		return JOptionPane.showConfirmDialog((Component) gui, Infostrengar.TunnelStartTekst +treTrekte[0] +", "
				+treTrekte[1] +" og " +treTrekte[2]	+". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
	}

	private int velAntalJokrarDuVilBruke(Route route, PlayerAndNetworkWTF player, Farge chosenColour) throws RemoteException {
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