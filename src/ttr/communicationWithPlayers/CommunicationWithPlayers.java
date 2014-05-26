package ttr.communicationWithPlayers;

import ttr.bord.Table;
import ttr.data.*;
import ttr.kjerna.Core;
import ttr.rute.Route;
import ttr.spelar.IPlayer;
import ttr.utgaave.nordic.Nordic;

import javax.swing.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class CommunicationWithPlayers {
	protected ArrayList<IPlayer> players;

	public CommunicationWithPlayers (ArrayList<IPlayer> players) {
		this.players = players;
	}

	public abstract void updateOtherPlayers(Colour colour, int kortKrevd, int jokrar, int krevdJokrar, String byggjandeNamn, Route bygd) throws RemoteException;
	public abstract ArrayList<IPlayer> createPlayersForLocalGame(Core hovud, Table bord);
	public abstract void sjekkOmFerdig(MeldingarModell meldingarModell, IPlayer kvenSinTur, String speltittel, IPlayer minSpelar) throws RemoteException;


	protected void orientPlayersThatTheGameIsOver(MeldingarModell meldingarModell) throws RemoteException {
		orientOthers(meldingarModell);
		for (IPlayer player : players){
			player.receiveMessage(Infostrengar.SpeletErFerdig);
		}
	}
	
	protected abstract void orientOthers(MeldingarModell meldingarModell);

	//TODO Legg inn spelutgaave-spesifikk bonus her - lengst rute for Europe
	protected void addGameSpecificBonus(MeldingarModell meldingarModell, String speltittel, IPlayer minSpelar, int[] totalpoeng) throws RemoteException {
		if (speltittel.equals(Nordic.tittel)){
			finnSpelarSomKlarteFlestOppdrag(totalpoeng,minSpelar,meldingarModell);
		}
	}

	private void finnSpelarSomKlarteFlestOppdrag(int[] totalpoeng, IPlayer minSpelar, MeldingarModell meldingarModell) throws RemoteException {
		IPlayer playerWithMostMissionsAccomplished = getPlayerWithMostMissionsAccomplished(minSpelar);
		int bestNumberOfMissionsAccomplished = playerWithMostMissionsAccomplished.getAntalFullfoerteOppdrag();
		totalpoeng[playerWithMostMissionsAccomplished.getSpelarNummer()] = 10;

		orientNetwork(meldingarModell, playerWithMostMissionsAccomplished,	bestNumberOfMissionsAccomplished);
		for (IPlayer player : players){
			player.receiveMessage(playerWithMostMissionsAccomplished.getNamn() +" klarte flest oppdrag, " +bestNumberOfMissionsAccomplished);
		}
	}

	protected abstract void orientNetwork(MeldingarModell meldingarModell,	IPlayer playerWithMostMissionsAccomplished,	int bestNumberOfMissionsAccomplished) throws RemoteException;

	private IPlayer getPlayerWithMostMissionsAccomplished(IPlayer myPlayer) throws RemoteException {
		IPlayer playerWithMostAccomplishedMissions = myPlayer;
		for (IPlayer player : players){
			if (player.getAntalFullfoerteOppdrag() > playerWithMostAccomplishedMissions.getAntalFullfoerteOppdrag()){
				playerWithMostAccomplishedMissions = player;
			}
		}
		return playerWithMostAccomplishedMissions;
	}

	protected String informTheOthersAboutMyPoints(MeldingarModell messagesModel, IPlayer myPlayer, int[] totalpoeng) throws RemoteException {
		return " " + orientOthersAboutThisPlayersTotalPoints(totalpoeng, myPlayer, messagesModel);
	}


	protected void avsluttSpeletMedSuksess(IPlayer vinnar,String pointsString, MeldingarModell meldingarModell) throws RemoteException {
		String vinnaren = vinnar.getNamn() +" vann spelet, gratulerer!";
		String poeng = pointsString + vinnaren;
		meldingarModell.nyMelding(vinnaren);
		for (IPlayer player : players){
			player.receiveMessage(vinnaren);
			player.showGameOverMessage(poeng);
		}
		JOptionPane.showMessageDialog(new JPanel(), poeng);
	}

	protected IPlayer reknUtPoengOgFinnVinnar(int[] totalpoeng, IPlayer player, int vinnarpoeng, 
			IPlayer currentLeader, MeldingarModell meldingarModell) throws RemoteException {
		IPlayer leiarNo = currentLeader;
		int thisPlayersPoints = reknUtPoeng(player);
		orientOthersAboutThisPlayersTotalPoints(totalpoeng, player,	meldingarModell);
		return checkIfThisPlayerLeads(player, vinnarpoeng, currentLeader, leiarNo, thisPlayersPoints);
	}

	protected int reknUtPoeng(IPlayer player) throws RemoteException {
		int poeng = player.getOppdragspoeng();
		for (Route route : player.getBygdeRuter()) {
			poeng += route.getValue();
		}
		return poeng;
	}

	private String orientOthersAboutThisPlayersTotalPoints(int[] totalpoeng, IPlayer player, MeldingarModell meldingarModell) throws RemoteException {
		String sp = player.getNamn() + " fekk " + totalpoeng[player.getSpelarNummer()] + " poeng. ";
		meldingarModell.nyMelding(sp);
		for (IPlayer otherPlayer : players) {
			otherPlayer.receiveMessage(sp);
		}
		return sp;
	}

	private IPlayer checkIfThisPlayerLeads(IPlayer player, int vinnarpoeng, IPlayer vinnar, IPlayer leiarNo, int thisPlayersPoints) throws RemoteException {
		if ( (thisPlayersPoints > vinnarpoeng) || ((vinnar != null && thisPlayersPoints == vinnarpoeng) && (vinnar.getOppdragspoeng() < player.getOppdragspoeng()))) {
			return player;
		}
		return leiarNo;
	}

	public void sendMessageAboutCard(boolean card, boolean random, Colour colour, String handlandespelarsNamn, Core hovud) throws RemoteException {
		String melding = handlandespelarsNamn + (card ? " trakk inn " + colour +"." : " trakk oppdrag.");

		localOrNetworkSpecificMessageStuff(hovud.getMinSpelar(), melding);
		
		for (IPlayer player : hovud.getSpelarar()){
			if (hovud.getKvenSinTur()==player){ //TODO eh, hæ?
				sendMessageToPlayer(card, random, handlandespelarsNamn, melding, player);
			}
		}
	}

	protected abstract void localOrNetworkSpecificMessageStuff(IPlayer myPlayer, String melding) throws RemoteException;

	protected void sendMessageToPlayer(boolean card, boolean random, String handlandespelarsNamn, String melding, IPlayer player) throws RemoteException {
		if (!random){
			player.receiveMessage(melding);
		}
		else if(card && random){
			player.receiveMessage(handlandespelarsNamn + " trakk tilfeldig.");
		}
	}

	public abstract void newCardPlacedOnTableInNetworkGame(IPlayer host, Colour nyFarge, int position, Core hovud) throws RemoteException;
}
