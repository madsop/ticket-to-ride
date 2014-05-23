package ttr.kjerna;

import ttr.bord.Table;
import ttr.data.*;
import ttr.rute.Route;
import ttr.spelar.PlayerAndNetworkWTF;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

public abstract class CommunicationWithPlayersImpl implements CommunicationWithPlayers {
	protected ArrayList<PlayerAndNetworkWTF> players;

	public CommunicationWithPlayersImpl (ArrayList<PlayerAndNetworkWTF> players) {
		this.players = players;
	}

	public abstract void oppdaterAndreSpelarar(Farge colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException;
	public abstract ArrayList<PlayerAndNetworkWTF> createPlayersForLocalGame(Core hovud, Table bord);
	public abstract void sjekkOmFerdig(IMeldingarModell meldingarModell, PlayerAndNetworkWTF kvenSinTur, String speltittel, PlayerAndNetworkWTF minSpelar, Set<Route> ruter) throws RemoteException;


	protected void orientPlayersThatTheGameIsOver(IMeldingarModell meldingarModell) throws RemoteException {
		orientOthers(meldingarModell);
		for (PlayerAndNetworkWTF player : players){
			player.receiveMessage(Infostrengar.SpeletErFerdig);
		}
	}
	
	protected abstract void orientOthers(IMeldingarModell meldingarModell);

	//TODO Legg inn spelutgaave-spesifikk bonus her - lengst rute for Europe
	protected void addGameSpecificBonus(IMeldingarModell meldingarModell,	String speltittel, PlayerAndNetworkWTF minSpelar, int[] totalpoeng)	throws RemoteException {
		if (speltittel.equals(Nordic.tittel)){
			finnSpelarSomKlarteFlestOppdrag(totalpoeng,minSpelar,meldingarModell);
		}
	}

	private void finnSpelarSomKlarteFlestOppdrag(int[] totalpoeng, PlayerAndNetworkWTF minSpelar, IMeldingarModell meldingarModell) throws RemoteException {
		PlayerAndNetworkWTF playerWithMostMissionsAccomplished = getPlayerWithMostMissionsAccomplished(minSpelar);
		int bestNumberOfMissionsAccomplished = playerWithMostMissionsAccomplished.getAntalFullfoerteOppdrag();
		totalpoeng[playerWithMostMissionsAccomplished.getSpelarNummer()] = 10;

		orientNetwork(meldingarModell, playerWithMostMissionsAccomplished,	bestNumberOfMissionsAccomplished);
		for (PlayerAndNetworkWTF s : players){
			s.receiveMessage(playerWithMostMissionsAccomplished.getNamn() +" klarte flest oppdrag, " +bestNumberOfMissionsAccomplished);
		}
	}

	protected abstract void orientNetwork(IMeldingarModell meldingarModell,	PlayerAndNetworkWTF playerWithMostMissionsAccomplished,	int bestNumberOfMissionsAccomplished) throws RemoteException;

	private PlayerAndNetworkWTF getPlayerWithMostMissionsAccomplished(PlayerAndNetworkWTF myPlayer) throws RemoteException {
		PlayerAndNetworkWTF playerWithMostAccomplishedMissions = myPlayer;
		for (PlayerAndNetworkWTF player : players){
			if (player.getAntalFullfoerteOppdrag() > playerWithMostAccomplishedMissions.getAntalFullfoerteOppdrag()){
				playerWithMostAccomplishedMissions = player;
			}
		}
		return playerWithMostAccomplishedMissions;
	}

	protected String informTheOthersAboutMyPoints(IMeldingarModell messagesModel, PlayerAndNetworkWTF myPlayer, int[] totalpoeng) throws RemoteException {
		return " " + orientOthersAboutThisPlayersTotalPoints(totalpoeng, myPlayer, messagesModel);
	}


	protected void avsluttSpeletMedSuksess(PlayerAndNetworkWTF vinnar,String pointsString, IMeldingarModell meldingarModell) throws RemoteException {
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

	protected PlayerAndNetworkWTF reknUtPoengOgFinnVinnar(int[] totalpoeng, PlayerAndNetworkWTF player, int vinnarpoeng, PlayerAndNetworkWTF currentLeader, IMeldingarModell meldingarModell, Set<Route> ruter) throws RemoteException {
		PlayerAndNetworkWTF leiarNo = currentLeader;
		int thisPlayersPoints = reknUtPoeng(player,ruter);
		orientOthersAboutThisPlayersTotalPoints(totalpoeng, player,	meldingarModell);
		return checkIfThisPlayerLeads(player, vinnarpoeng, currentLeader, leiarNo, thisPlayersPoints);
	}

	protected int reknUtPoeng(PlayerAndNetworkWTF player, Set<Route> ruter) throws RemoteException {
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

	public void sendMessageAboutCard(boolean card, boolean random, Farge colour, String handlandespelarsNamn, Core hovud) throws RemoteException {
		String melding = handlandespelarsNamn + (card ? " trakk inn " + colour +"." : " trakk oppdrag.");

		localOrNetworkSpecificMessageStuff(hovud.getMinSpelar(), melding);
		
		for (PlayerAndNetworkWTF player : hovud.getSpelarar()){
			if (hovud.getKvenSinTur()==player){
				sendMessageToPlayer(card, random, handlandespelarsNamn, melding, player);
			}
		}
	}

	protected abstract void localOrNetworkSpecificMessageStuff(PlayerAndNetworkWTF myPlayer, String melding) throws RemoteException;

	protected void sendMessageToPlayer(boolean card, boolean random, String handlandespelarsNamn, String melding, PlayerAndNetworkWTF player) throws RemoteException {
		if (!random){
			player.receiveMessage(melding);
		}
		else if(card && random){
			player.receiveMessage(handlandespelarsNamn + " trakk tilfeldig.");
		}
	}

	public abstract void newCardPlacedOnTableInNetworkGame(PlayerAndNetworkWTF host, Farge nyFarge, int position, Core hovud) throws RemoteException;
}
