package ttr.communicationWithPlayers;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ttr.data.Infostrengar;
import ttr.data.MeldingarModell;
import ttr.rute.Route;
import ttr.spelar.IPlayer;
import ttr.utgaave.nordic.Nordic;

public abstract class GameFinisher {
	protected ArrayList<IPlayer> players;

	public GameFinisher(ArrayList<IPlayer> players) {
		this.players = players;
	}
	
	protected abstract void orientOthers(MeldingarModell meldingarModell);
	protected abstract void orientNetwork(MeldingarModell meldingarModell,	IPlayer playerWithMostMissionsAccomplished,	int bestNumberOfMissionsAccomplished) throws RemoteException;

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

	protected void orientPlayersThatTheGameIsOver(MeldingarModell meldingarModell) throws RemoteException {
		orientOthers(meldingarModell);
		for (IPlayer player : players){
			player.receiveMessage(Infostrengar.SpeletErFerdig);
		}
	}

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
}
