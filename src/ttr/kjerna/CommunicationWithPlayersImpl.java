package ttr.kjerna;

import ttr.bord.Table;
import ttr.data.*;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.spelar.PlayerNetworkClass;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


public class CommunicationWithPlayersImpl implements CommunicationWithPlayers {
	private final boolean nett;
	private ArrayList<PlayerAndNetworkWTF> players;

	public CommunicationWithPlayersImpl (boolean nett, ArrayList<PlayerAndNetworkWTF> spelarar) {
		this.nett = nett;
		this.players = spelarar;
	}

	public void oppdaterAndreSpelarar(Farge colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException {
		if (nett) {
			for (PlayerAndNetworkWTF player : players) {
				player.leggIStokken(colour, (kortKrevd-(jokrar-krevdJokrar)));
				player.leggIStokken(Farge.valfri,jokrar);
				player.receiveMessage(byggjandeNamn + " bygde ruta " +bygd.getStart() + " - " +bygd.getEnd() + " i farge " + bygd.getColour());
			}
		}
	}

	public ArrayList<PlayerAndNetworkWTF> createPlayersForLocalGame(IHovud hovud, Table bord) {
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

	private ArrayList<PlayerAndNetworkWTF> createPlayers(IHovud hovud, Table bord, int antalSpelarar) throws HeadlessException, RemoteException {
		players = new ArrayList<>();
		for (int i = 1; i <= antalSpelarar; i++) {
			players.add(new PlayerNetworkClass(hovud,JOptionPane.showInputDialog(null,Infostrengar.SkrivInnSpelarnamn +i),bord));
		}
		return players;
	}

	public void sjekkOmFerdig(IMeldingarModell meldingarModell, PlayerAndNetworkWTF kvenSinTur, String speltittel, PlayerAndNetworkWTF minSpelar, Set<Route> ruter) throws RemoteException{
		if (kvenSinTur.getGjenverandeTog() < Konstantar.AVSLUTT_SPELET) {
			orientPlayersThatTheGameIsOver(meldingarModell);

			int[] totalpoeng = new int[players.size() + (nett ? 1 : 0)];

			PlayerAndNetworkWTF vinnar = null;
			int vinnarpoeng = 0;
			addGameSpecificBonus(meldingarModell, speltittel, minSpelar, totalpoeng);

			String pointsString = Infostrengar.SpeletErFerdig;
			if (nett) { // TODO denne må da vera feil?
				totalpoeng[minSpelar.getSpelarNummer()] = reknUtPoeng(minSpelar,ruter);
				pointsString += informTheOthersAboutMyPoints(meldingarModell, minSpelar, totalpoeng);
				vinnar = minSpelar;
				vinnarpoeng = totalpoeng[minSpelar.getSpelarNummer()];
			}

			for (PlayerAndNetworkWTF player : players) {
				PlayerAndNetworkWTF leiar = reknUtPoengOgFinnVinnar(totalpoeng,player,vinnarpoeng,vinnar,meldingarModell,ruter );
				vinnarpoeng = reknUtPoeng(leiar,ruter);
			}
			avsluttSpeletMedSuksess(vinnar,pointsString,meldingarModell);
		}
	}

	private void orientPlayersThatTheGameIsOver(IMeldingarModell meldingarModell) throws RemoteException {
		if (nett) {meldingarModell.nyMelding(Infostrengar.SpeletErFerdig);}
		for (PlayerAndNetworkWTF player : players){
			player.receiveMessage(Infostrengar.SpeletErFerdig);
		}
	}

	//TODO Legg inn spelutgaave-spesifikk bonus her - lengst rute for Europe
	private void addGameSpecificBonus(IMeldingarModell meldingarModell,	String speltittel, PlayerAndNetworkWTF minSpelar, int[] totalpoeng)	throws RemoteException {
		if (speltittel.equals(Nordic.tittel)){
			finnSpelarSomKlarteFlestOppdrag(totalpoeng,minSpelar,meldingarModell);
		}
	}

	private void finnSpelarSomKlarteFlestOppdrag(int[] totalpoeng, PlayerAndNetworkWTF minSpelar, IMeldingarModell meldingarModell) throws RemoteException {
		PlayerAndNetworkWTF playerWithMostMissionsAccomplished = getPlayerWithMostMissionsAccomplished(minSpelar);
		int bestNumberOfMissionsAccomplished = playerWithMostMissionsAccomplished.getAntalFullfoerteOppdrag();
		totalpoeng[playerWithMostMissionsAccomplished.getSpelarNummer()] = 10;

		if (nett){
			meldingarModell.nyMelding(playerWithMostMissionsAccomplished.getNamn() + " klarte flest oppdrag, " + bestNumberOfMissionsAccomplished);
		}
		for (PlayerAndNetworkWTF s : players){
			s.receiveMessage(playerWithMostMissionsAccomplished.getNamn() +" klarte flest oppdrag, " +bestNumberOfMissionsAccomplished);
		}
	}

	private PlayerAndNetworkWTF getPlayerWithMostMissionsAccomplished(PlayerAndNetworkWTF myPlayer) throws RemoteException {
		PlayerAndNetworkWTF playerWithMostAccomplishedMissions = myPlayer;
		for (PlayerAndNetworkWTF player : players){
			if (player.getAntalFullfoerteOppdrag() > playerWithMostAccomplishedMissions.getAntalFullfoerteOppdrag()){
				playerWithMostAccomplishedMissions = player;
			}
		}
		return playerWithMostAccomplishedMissions;
	}

	private String informTheOthersAboutMyPoints(IMeldingarModell messagesModel, PlayerAndNetworkWTF myPlayer, int[] totalpoeng) throws RemoteException {
		return " " + orientOthersAboutThisPlayersTotalPoints(totalpoeng, myPlayer, messagesModel);
	}


	private void avsluttSpeletMedSuksess(PlayerAndNetworkWTF vinnar,String pointsString, IMeldingarModell meldingarModell) throws RemoteException {
		String poeng = pointsString;
		String vinnaren = vinnar.getNamn() +" vann spelet, gratulerer!";
		poeng += vinnaren;
		meldingarModell.nyMelding(vinnaren);
		for (PlayerAndNetworkWTF player : players){
			player.receiveMessage(vinnaren);
			player.showGameOverMessage(poeng);
		}
		JOptionPane.showMessageDialog(new JPanel(), poeng);
	}

	private PlayerAndNetworkWTF reknUtPoengOgFinnVinnar(int[] totalpoeng, PlayerAndNetworkWTF player, int vinnarpoeng, PlayerAndNetworkWTF currentLeader, IMeldingarModell meldingarModell, Set<Route> ruter) throws RemoteException {
		PlayerAndNetworkWTF leiarNo = currentLeader;
		int thisPlayersPoints = reknUtPoeng(player,ruter);
		orientOthersAboutThisPlayersTotalPoints(totalpoeng, player,	meldingarModell);
		return checkIfThisPlayerLeads(player, vinnarpoeng, currentLeader, leiarNo, thisPlayersPoints);
	}

	private int reknUtPoeng(PlayerAndNetworkWTF player, Set<Route> ruter) throws RemoteException {
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

	private String orientOthersAboutThisPlayersTotalPoints(int[] totalpoeng, PlayerAndNetworkWTF player, IMeldingarModell meldingarModell) throws RemoteException {
		String sp = player.getNamn() + " fekk " + totalpoeng[player.getSpelarNummer()] + " poeng. ";
		meldingarModell.nyMelding(sp);
		for (PlayerAndNetworkWTF otherPlayer : players) {
			otherPlayer.receiveMessage(sp);
		}
		return sp;
	}

	private PlayerAndNetworkWTF checkIfThisPlayerLeads(PlayerAndNetworkWTF player, int vinnarpoeng, PlayerAndNetworkWTF vinnar, PlayerAndNetworkWTF leiarNo, int thisPlayersPoints) throws RemoteException {
		if ( (thisPlayersPoints > vinnarpoeng) || ((vinnar != null && thisPlayersPoints == vinnarpoeng) && (vinnar.getOppdragspoeng() < player.getOppdragspoeng()))) {
			return player;
		}
		return leiarNo;
	}

	public void sendMessageAboutCard(boolean card, boolean random, Farge colour, String handlandespelarsNamn, boolean nett, IHovud hovud) throws RemoteException{
		String melding = handlandespelarsNamn;
		melding += card ? " trakk inn " + colour +"." : " trakk oppdrag.";

		if (nett){
			hovud.getMinSpelar().receiveMessage(melding);
		}

		for (PlayerAndNetworkWTF player : hovud.getSpelarar()){
			if (nett || hovud.getKvenSinTur()==player){
				sendMessageToPlayer(card, random, handlandespelarsNamn, melding, player);
			}
		}
	}

	private void sendMessageToPlayer(boolean card, boolean random, String handlandespelarsNamn, String melding, PlayerAndNetworkWTF player) throws RemoteException {
		if (!random){
			player.receiveMessage(melding);
		}
		else if(card && random){
			player.receiveMessage(handlandespelarsNamn + " trakk tilfeldig.");
		}
	}

	public void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF host, Farge nyFarge, int position, IHovud hovud) throws RemoteException{
		if (iAmHost(host, hovud)){
			orientPlayersAboutNewCardOnTable(nyFarge, position, hovud.getSpelarar());
		}
		else {
			orientPlayersAndHostAboutNewCardOnTable(host, nyFarge, position, hovud.getSpelarar(), hovud.getMinSpelar());
		}
	}

	private void orientPlayersAboutNewCardOnTable(Farge nyFarge, int position, Collection<PlayerAndNetworkWTF> players) throws RemoteException {
		for (PlayerAndNetworkWTF player : players){
			// metode for å legge kortet host nettopp trakk på plass i på bordet hos spelar s
			player.putCardOnTable(nyFarge,position);
		}
	}

	private void orientPlayersAndHostAboutNewCardOnTable(PlayerAndNetworkWTF host, Farge nyFarge, int position, ArrayList<PlayerAndNetworkWTF> players, PlayerAndNetworkWTF myPlayer) throws RemoteException {
		myPlayer.putCardOnTable(nyFarge, position);
		for (PlayerAndNetworkWTF player : players){
			if (!host.getNamn().equals(player.getNamn())){
				player.putCardOnTable(nyFarge, position);
			}
		}
	}

	private boolean iAmHost(PlayerAndNetworkWTF vert, IHovud hovud) throws RemoteException {
		return vert.getNamn().equals(hovud.getMinSpelar().getNamn());
	}
}
