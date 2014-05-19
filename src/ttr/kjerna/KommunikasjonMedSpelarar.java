package ttr.kjerna;

import ttr.bord.IBord;
import ttr.data.*;
import ttr.rute.Route;
import ttr.spelar.ISpelar;
import ttr.spelar.PlayerNetworkClass;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


public class KommunikasjonMedSpelarar implements IKommunikasjonMedSpelarar {
	private final boolean nett;
	private ArrayList<ISpelar> players;

	public KommunikasjonMedSpelarar (boolean nett, ArrayList<ISpelar> spelarar) {
		this.nett = nett;
		this.players = spelarar;
	}

	public void oppdaterAndreSpelarar(int plass, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException {
		if (nett) {
			for (ISpelar s : players) {
				s.leggIStokken(plass, (kortKrevd-(jokrar-krevdJokrar)));
				s.leggIStokken(Konstantar.ANTAL_FARGAR-1,jokrar);
				s.faaMelding(byggjandeNamn + " bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
			}
		}
	}

	public ArrayList<ISpelar> createPlayersForLocalGame(IHovud hovud, IBord bord) {
		int antalSpelarar = addPlayers();
		try {
			return createPlayers(hovud, bord, antalSpelarar);
		} catch (RemoteException ignored) { }
		return new ArrayList<>();
	}

	private int addPlayers() {
		int numberOfPlayers = 0;
		Object[] legalNumberOfPlayers = {2,3};
		while ((numberOfPlayers != 2) && (numberOfPlayers != 3)) {
			numberOfPlayers = 2+ JOptionPane.showOptionDialog(null, Infostrengar.KorMangeMed, Infostrengar.AntalSpelarar, 0, 3, null, legalNumberOfPlayers, 2);
		}
		return numberOfPlayers;
	}

	private ArrayList<ISpelar> createPlayers(IHovud hovud, IBord bord, int antalSpelarar) throws HeadlessException, RemoteException {
		players = new ArrayList<>();
		for (int i = 1; i <= antalSpelarar; i++) {
			players.add(new PlayerNetworkClass(hovud,JOptionPane.showInputDialog(null,Infostrengar.SkrivInnSpelarnamn +i),bord));
		}
		return players;
	}

	public void sjekkOmFerdig(IMeldingarModell meldingarModell, ISpelar kvenSinTur, String speltittel, ISpelar minSpelar, Set<Route> ruter) throws RemoteException{
		if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
			orientPlayersThatTheGameIsOver(meldingarModell);

			int[] totalpoeng = new int[players.size() + (nett ? 1 : 0)];

			ISpelar vinnar = null;
			int vinnarpoeng = 0;
			addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

			String pointsString = Infostrengar.SpeletErFerdig;
			if (nett) { // TODO denne må da vera feil?
				totalpoeng[minSpelar.getSpelarNummer()] = reknUtPoeng(minSpelar,ruter);
				pointsString += informTheOthersAboutMyPoints(meldingarModell, minSpelar, totalpoeng);
				vinnar = minSpelar;
				vinnarpoeng = totalpoeng[minSpelar.getSpelarNummer()];
			}

			for (ISpelar player : players) {
				ISpelar leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell,ruter );
				vinnarpoeng = reknUtPoeng(leiar,ruter);
			}
			avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
		}
	}

	private void orientPlayersThatTheGameIsOver(IMeldingarModell meldingarModell) throws RemoteException {
		if (nett) {meldingarModell.nyMelding(Infostrengar.SpeletErFerdig);}
		for (ISpelar player : players){
			player.faaMelding(Infostrengar.SpeletErFerdig);
		}
	}

	//TODO Legg inn spelutgaave-spesifikk bonus her - lengst rute for Europe
	private void addGameSpecificBonus(IMeldingarModell meldingarModell,	String speltittel, ISpelar minSpelar, int[] totalpoeng)	throws RemoteException {
		if (speltittel.equals(Nordic.tittel)){
			finnSpelarSomKlarteFlestOppdrag(totalpoeng,minSpelar,meldingarModell);
		}
	}

	private void finnSpelarSomKlarteFlestOppdrag(int[] totalpoeng, ISpelar minSpelar, IMeldingarModell meldingarModell) throws RemoteException {
		ISpelar playerWithMostMissionsAccomplished = getPlayerWithMostMissionsAccomplished(minSpelar);
		int bestNumberOfMissionsAccomplished = playerWithMostMissionsAccomplished.getAntalFullfoerteOppdrag();
		totalpoeng[playerWithMostMissionsAccomplished.getSpelarNummer()] = 10;

		if (nett){
			meldingarModell.nyMelding(playerWithMostMissionsAccomplished.getNamn() + " klarte flest oppdrag, " + bestNumberOfMissionsAccomplished);
		}
		for (ISpelar s : players){
			s.faaMelding(playerWithMostMissionsAccomplished.getNamn() +" klarte flest oppdrag, " +bestNumberOfMissionsAccomplished);
		}
	}

	private ISpelar getPlayerWithMostMissionsAccomplished(ISpelar myPlayer) throws RemoteException {
		ISpelar playerWithMostAccomplishedMissions = myPlayer;
		for (ISpelar player : players){
			if (player.getAntalFullfoerteOppdrag() > playerWithMostAccomplishedMissions.getAntalFullfoerteOppdrag()){
				playerWithMostAccomplishedMissions = player;
			}
		}
		return playerWithMostAccomplishedMissions;
	}

	private String informTheOthersAboutMyPoints(IMeldingarModell messagesModel, ISpelar myPlayer, int[] totalpoeng) throws RemoteException {
		return " " + orientOthersAboutThisPlayersTotalPoints(totalpoeng, myPlayer, messagesModel);
	}


	private void avsluttSpeletMedSuksess(ISpelar vinnar,String pointsString, IMeldingarModell meldingarModell) throws RemoteException {
		String poeng = pointsString;
		String vinnaren = vinnar.getNamn() +" vann spelet, gratulerer!";
		poeng += vinnaren;
		meldingarModell.nyMelding(vinnaren);
		for (ISpelar player : players){
			player.faaMelding(vinnaren);
			player.visSpeletErFerdigmelding(poeng);
		}
		JOptionPane.showMessageDialog(new JPanel(), poeng);
	}

	private ISpelar reknUtPoengOgFinnVinnar(int[] totalpoeng, ISpelar player, int vinnarpoeng, ISpelar currentLeader, IMeldingarModell meldingarModell, Set<Route> ruter) throws RemoteException {
		ISpelar leiarNo = currentLeader;
		int thisPlayersPoints = reknUtPoeng(player,ruter);
		orientOthersAboutThisPlayersTotalPoints(totalpoeng, player,	meldingarModell);
		return checkIfThisPlayerLeads(player, vinnarpoeng, currentLeader, leiarNo, thisPlayersPoints);
	}

	private int reknUtPoeng(ISpelar player, Set<Route> ruter) throws RemoteException {
		int poeng = player.getOppdragspoeng();
		for (int j = 0; j < player.getBygdeRuterSize(); j++) {
			for (Route route : ruter) {
				if (player.getBygdeRuterId(j) == route.getRouteId()) {
					poeng += route.getValue();
				}
			}
		}
		return poeng;
	}

	private String orientOthersAboutThisPlayersTotalPoints(int[] totalpoeng, ISpelar player, IMeldingarModell meldingarModell) throws RemoteException {
		String sp = player.getNamn() + " fekk " + totalpoeng[player.getSpelarNummer()] + " poeng. ";
		meldingarModell.nyMelding(sp);
		for (ISpelar otherPlayer : players) {
			otherPlayer.faaMelding(sp);
		}
		return sp;
	}

	private ISpelar checkIfThisPlayerLeads(ISpelar player, int vinnarpoeng, ISpelar vinnar, ISpelar leiarNo, int thisPlayersPoints) throws RemoteException {
		if ( (thisPlayersPoints > vinnarpoeng) || ((vinnar != null && thisPlayersPoints == vinnarpoeng) && (vinnar.getOppdragspoeng() < player.getOppdragspoeng()))) {
			return player;
		}
		return leiarNo;
	}

	public void sendMessageAboutCard(boolean card, boolean random, Farge colour, String handlandespelarsNamn, boolean nett, IHovud hovud) throws RemoteException{
		String melding = handlandespelarsNamn;
		melding += card ? " trakk inn " + colour +"." : " trakk oppdrag.";

		if (nett){
			hovud.getMinSpelar().faaMelding(melding);
		}

		for (ISpelar player : hovud.getSpelarar()){
			if (nett || hovud.getKvenSinTur()==player){
				sendMessageToPlayer(card, random, handlandespelarsNamn, melding, player);
			}
		}
	}

	private void sendMessageToPlayer(boolean card, boolean random, String handlandespelarsNamn, String melding, ISpelar player) throws RemoteException {
		if (!random){
			player.faaMelding(melding);
		}
		else if(card && random){
			player.faaMelding(handlandespelarsNamn + " trakk tilfeldig.");
		}
	}

	public void newCardPlacedOnTableInNetworkGame(ISpelar host, Farge nyFarge, int position, IHovud hovud) throws RemoteException{
		if (iAmHost(host, hovud)){
			orientPlayersAboutNewCardOnTable(nyFarge, position, hovud.getSpelarar());
		}
		else {
			orientPlayersAndHostAboutNewCardOnTable(host, nyFarge, position, hovud.getSpelarar(), hovud.getMinSpelar());
		}
	}

	private void orientPlayersAboutNewCardOnTable(Farge nyFarge, int position, Collection<ISpelar> players) throws RemoteException {
		for (ISpelar player : players){
			// metode for å legge kortet host nettopp trakk på plass i på bordet hos spelar s
			player.putCardOnTable(nyFarge,position);
		}
	}

	private void orientPlayersAndHostAboutNewCardOnTable(ISpelar host, Farge nyFarge, int position, ArrayList<ISpelar> players, ISpelar myPlayer) throws RemoteException {
		myPlayer.putCardOnTable(nyFarge, position);
		for (ISpelar player : players){
			if (!host.getNamn().equals(player.getNamn())){
				player.putCardOnTable(nyFarge, position);
			}
		}
	}

	private boolean iAmHost(ISpelar vert, IHovud hovud) throws RemoteException {
		return vert.getNamn().equals(hovud.getMinSpelar().getNamn());
	}
}
