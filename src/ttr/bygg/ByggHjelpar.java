package ttr.bygg;

import ttr.bord.Table;
import ttr.data.Colour;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.GUI;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ByggHjelpar {
	private final GUI gui;
	
	//TODO fortener denne å bli splitta i ein kan-bygge-her-klasse og resten? evt. ein colour-finder-klasse
	
	public ByggHjelpar(GUI gui) {
		this.gui = gui;
	}

	public ByggjandeInfo bygg(Route routeToBuild, Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		Colour colourToBuildIn = findColourToBuildIn(routeToBuild, kortKrevd, krevdJokrar, buildingPlayer);
		if (colourToBuildIn == null) { return null; }
		
		int jokers = chooseNumberOfJokersToUser(routeToBuild, colourToBuildIn, kortKrevd,	krevdJokrar, buildingPlayer);
		checkIfThePlayerHasEnoughCards(routeToBuild, colourToBuildIn, kortKrevd, krevdJokrar,	buildingPlayer, jokers);
		buildingPlayer.bygg(routeToBuild);
		updatePlayersCards(colourToBuildIn, kortKrevd, krevdJokrar, buildingPlayer, jokers);

		return new ByggjandeInfo(buildingPlayer,jokers);
	}

	public ByggjandeInfo byggTunnel(Table bord, Route bygd, Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		Colour[] treTrekte = drawThreeRandomCards(bord);
		int ekstra = computeExtraNeededCards(bygd, treTrekte);

		if (askUserIfBuildAnyway(treTrekte, ekstra) == JOptionPane.OK_OPTION) {
			return bygg(bygd, colour, kortKrevd+ekstra, krevdJokrar, buildingPlayer);
		}
		return null;
	}

	private Colour[] drawThreeRandomCards(Table bord) {
		Colour[] treTrekte = new Colour[3];
		
		for (int i = 0; i < treTrekte.length; i++) {
			treTrekte[i] = bord.getRandomCardFromTheDeck(0);
			if (treTrekte[i] == null){
				JOptionPane.showMessageDialog((Component) gui, Infostrengar.TomtPåBordet);
				return null;
			}
		}
		return treTrekte;
	}

	private int computeExtraNeededCards(Route bygd, Colour[] treTrekte) {
		int extra = 0;
		for (Colour drawnCard : treTrekte) {
			if (drawnCard == Colour.valfri || drawnCard == bygd.getColour()) {
				extra++;
			}
		}
		return extra;
	}
	
	private Colour findColourToBuildIn(Route routeToBuild, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF buildingPlayer) throws RemoteException {
		if (routeToBuild.getColour() == Colour.valfri){
			return byggValfriFarge(buildingPlayer,krevdJokrar,kortKrevd); //TODO fix denne - bruk final på position
		}
		return routeToBuild.getColour();
	}

	private Colour byggValfriFarge(PlayerAndNetworkWTF player, int numberOfDemandedJokers, int numberOfDemandedNormalCards) throws RemoteException {
		int extraJokers = player.getNumberOfRemainingJokers() - numberOfDemandedJokers;
		System.out.println("ekstrajokrar: " +extraJokers);

		ArrayList<Colour> possibleColours = findPossibleColoursToBuildIn(player, numberOfDemandedNormalCards, extraJokers);

		if (possibleColours.size() > 0){
			return chooseColourToBuildIn(possibleColours);
		}
		return null;
	}

	private ArrayList<Colour> findPossibleColoursToBuildIn(PlayerAndNetworkWTF player, int numberOfDemandedNormalCards, int ekstrajokrar) throws RemoteException {
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
			colourPosition = JOptionPane.showOptionDialog((Component) gui, Infostrengar.VelFargeÅByggeILabel, Infostrengar.VelFargeÅByggeILabel, 
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, mulegeFargar.toArray(), mulegeFargar.get(0));
			if (colourPosition==-1){ return null; }
		}
		return mulegeFargar.get(colourPosition);
	}

	private boolean canBuildThisRouteInThisColour(PlayerAndNetworkWTF player, int numberOfDemandedNormalCards, int ekstrajokrar, Colour colour) throws RemoteException {
		return ((colour != Colour.valfri) && (player.getNumberOfCardsLeftInColour(colour) + ekstrajokrar) >= numberOfDemandedNormalCards && ekstrajokrar >= 0) 
				||
				(ekstrajokrar >= numberOfDemandedNormalCards);
	}

	 //TODO denne metoden må jo returnere og ev. sørge for å stoppe bygginga
	private void checkIfThePlayerHasEnoughCards(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int jokers)	throws RemoteException {
		if (jokers > byggjandeSpelar.getNumberOfRemainingJokers() || playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers)){
			if (bygd.getColour() != Colour.valfri){
				JOptionPane.showMessageDialog((Component) gui, Infostrengar.IkkjeNokKort);
			}
		}
	}

	private int chooseNumberOfJokersToUser(Route bygd, Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar) throws RemoteException {
		int jokers = 0;
		if (byggjandeSpelar.getNumberOfRemainingJokers() > 0) {
			do {
				jokers = velAntalJokrarDuVilBruke(bygd, byggjandeSpelar, colour);
			} while(numberOfChosenJokersNotOK(colour, kortKrevd, krevdJokrar, byggjandeSpelar, jokers));
		}
		return jokers;
	}

	private boolean numberOfChosenJokersNotOK(Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return playerHasLessJokersThanHeSays(byggjandeSpelar, chosenNumberOfJokers)
				|| chosenNumberOfJokers < krevdJokrar
				|| playerDoesNotHaveEnoughCardsInChosenColour(colour, kortKrevd,	krevdJokrar, byggjandeSpelar, chosenNumberOfJokers);
	}

	private boolean playerHasLessJokersThanHeSays(PlayerAndNetworkWTF byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfRemainingJokers() < chosenNumberOfJokers;
	}

	private boolean playerDoesNotHaveEnoughCardsInChosenColour(Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getNumberOfCardsLeftInColour(colour) < kortKrevd - (chosenNumberOfJokers-krevdJokrar);
	}

	private void updatePlayersCards(Colour colour, int kortKrevd, int krevdJokrar, PlayerAndNetworkWTF byggjandeSpelar, int jokers) throws RemoteException {
		byggjandeSpelar.decrementCardsAt(colour, kortKrevd-(jokers-krevdJokrar));
		byggjandeSpelar.decrementCardsAt(Colour.valfri, jokers);
	}

	private int askUserIfBuildAnyway(Colour[] treTrekte, int ekstra) {
		return JOptionPane.showConfirmDialog((Component) gui, Infostrengar.TunnelStartTekst +treTrekte[0] +", "
				+treTrekte[1] +" og " +treTrekte[2]	+". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
	}

	private int velAntalJokrarDuVilBruke(Route route, PlayerAndNetworkWTF player, Colour chosenColour) throws RemoteException {
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