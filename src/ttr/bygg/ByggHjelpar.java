package ttr.bygg;

import ttr.bord.IBord;
import ttr.data.Farge;
import ttr.data.Infostrengar;
import ttr.data.Konstantar;
import ttr.gui.IGUI;
import ttr.rute.IRoute;
import ttr.spelar.ISpelar;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ByggHjelpar implements IByggHjelpar {
	private final IGUI gui;
	private Farge valdFarge;
	private final boolean nett;

	public ByggHjelpar(IGUI gui, boolean nett) {
		this.gui = gui;
		this.nett = nett;
	}

	private int byggValfriFarge(ISpelar player, int numberOfDemandedJokers, int numberOfDemandedNormalCards) throws RemoteException {
		ArrayList<Farge> mulegeFargar = new ArrayList<>();
		int ekstrajokrar = player.getKort()[Konstantar.ANTAL_FARGAR-1]-numberOfDemandedJokers;
		System.out.println("ekstrajokrar: " +ekstrajokrar);
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++){
			if (canBuildThisRouteInThisColour(player, numberOfDemandedNormalCards,	ekstrajokrar, i)){
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
		return Konstantar.finnPosisjonForFarg(mulegeFargar.get(colourPosition));
	}

	private boolean canBuildThisRouteInThisColour(ISpelar player, int numberOfDemandedNormalCards, int ekstrajokrar, int i)	throws RemoteException {
		return ((i != Konstantar.ANTAL_FARGAR-1) && (player.getKort()[i] + ekstrajokrar) >= numberOfDemandedNormalCards && ekstrajokrar >= 0) 
				||
				(ekstrajokrar >= numberOfDemandedNormalCards);
	}

	public ByggjandeInfo bygg(IRoute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException {
		ISpelar byggjandeSpelar = nett ? minSpelar : kvenSinTur;

		if (bygd.getColour() == Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1]){
			plass = byggValfriFarge(byggjandeSpelar,krevdJokrar,kortKrevd); //TODO fix denne - bruk final på plass
			if (plass == -1) { return null; }
		}
		else {
			valdFarge = bygd.getColour();
		}

		int jokers = chooseNumberOfJokersToUser(bygd, plass, kortKrevd,	krevdJokrar, byggjandeSpelar);

		byggjandeSpelar.bygg(bygd);

		checkIfThePlayerHasEnoughCards(bygd, plass, kortKrevd, krevdJokrar,	byggjandeSpelar, jokers);
		plass = updatePlass(plass);
		updatePlayersCards(plass, kortKrevd, krevdJokrar, byggjandeSpelar, jokers);

		return new ByggjandeInfo(byggjandeSpelar,jokers);
	}

	private void checkIfThePlayerHasEnoughCards(IRoute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int jokers)	throws RemoteException {
		if (jokers > byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] || playerDoesNotHaveEnoughCardsInChosenColour(plass, kortKrevd, krevdJokrar, byggjandeSpelar, jokers)){
			if (routeIsNotJokerColoured(bygd)){
				JOptionPane.showMessageDialog((Component) gui, Infostrengar.IkkjeNokKort);
			}
		}
	}

	private int updatePlass(int plass) {
		for (int i = 0; i < Konstantar.ANTAL_FARGAR; i++){
			if(Konstantar.FARGAR[i]==valdFarge){
				return i;
			}
		}
		return plass;
	}

	private boolean routeIsNotJokerColoured(IRoute bygd) {
		return bygd.getColour() != Konstantar.FARGAR[Konstantar.ANTAL_FARGAR-1];
	}

	private int chooseNumberOfJokersToUser(IRoute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar) throws RemoteException {
		int jokers = 0;
		if (byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] > 0) {
			do {
				jokers = velAntalJokrarDuVilBruke(bygd, byggjandeSpelar,valdFarge);
			} while(numberOfChosenJokersNotOK(plass, kortKrevd, krevdJokrar, byggjandeSpelar, jokers));
		}
		return jokers;
	}

	private boolean numberOfChosenJokersNotOK(int plass, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return playerHasLessJokersThanHeSays(byggjandeSpelar, chosenNumberOfJokers)
				|| chosenNumberOfJokers < krevdJokrar
				|| playerDoesNotHaveEnoughCardsInChosenColour(plass, kortKrevd,	krevdJokrar, byggjandeSpelar, chosenNumberOfJokers);
	}

	private boolean playerHasLessJokersThanHeSays(ISpelar byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] < chosenNumberOfJokers;
	}

	private boolean playerDoesNotHaveEnoughCardsInChosenColour(int plass, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int chosenNumberOfJokers) throws RemoteException {
		return byggjandeSpelar.getKort()[plass] < kortKrevd - (chosenNumberOfJokers-krevdJokrar);
	}

	private void updatePlayersCards(int plass, int kortKrevd, int krevdJokrar, ISpelar byggjandeSpelar, int jokers) throws RemoteException {
		byggjandeSpelar.getKort()[plass] -= (kortKrevd-(jokers-krevdJokrar));
		byggjandeSpelar.getKort()[Konstantar.ANTAL_FARGAR-1] -= jokers;
	}


	public ByggjandeInfo byggTunnel(IBord bord, IRoute bygd, int plass, int kortKrevd, int krevdJokrar, ISpelar minSpelar, ISpelar kvenSinTur) throws RemoteException {
		Farge[] treTrekte = new Farge[3];
		int ekstra = 0;
		for (int i = 0; i < treTrekte.length; i++) {
			treTrekte[i] = bord.getTilfeldigKortFråBordet(0, false);
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
			return bygg(bygd, plass, kortKrevd+ekstra, krevdJokrar, minSpelar,kvenSinTur);
		}
		return null;
	}

	private int askUserIfBuildAnyway(Farge[] treTrekte, int ekstra) {
		return JOptionPane.showConfirmDialog((Component) gui, Infostrengar.TunnelStartTekst +treTrekte[0] +", "
				+treTrekte[1] +" og " +treTrekte[2]	+". Altså må du betale " +ekstra +" ekstra kort. Vil du det?");
	}

	private int velAntalJokrarDuVilBruke(IRoute route, ISpelar player, Farge chosenColour) throws RemoteException {
		int playersNumberOfJokers = player.getKort()[Konstantar.ANTAL_FARGAR-1];
		int howMany = -1;
		while (howMany < 0 || howMany > playersNumberOfJokers || howMany > route.getLength()) {
			String sendinn = Infostrengar.AntalJokrarStart +playersNumberOfJokers +" jokrar, " +player.getKort()[Konstantar.finnPosisjonForFarg(chosenColour)] 
					+ " av fargen du skal byggje, og ruta er " +route.getLength() +" tog lang.";
			String usersInput = JOptionPane.showInputDialog(sendinn,0);
			howMany = Integer.parseInt(usersInput);
		}
		return howMany;
	}
}
